package org.moonila.code.parser.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.moonila.code.parser.Utilities;
import org.moonila.code.parser.engine.ParserException;
import org.moonila.code.parser.engine.TreeSitterParser;
import org.moonila.code.parser.engine.beans.ResultBean;
import org.moonila.code.parser.engine.lng.LanguageEnum;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.*;

public class FileProcessor {
    private ExecutorService pool;

    public FileProcessor() {
    }

    public void process(String inputPath, String outputPath) {
        pool = Executors.newWorkStealingPool(5);
        processFolder(inputPath, outputPath);
        pool.shutdown();
        try {
            if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                System.out.println("Timeout exceeded, all files may not be parsed");
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void processFolder(String inputPath, String outputPath) {
        File inputFolder = new File(inputPath);
        TreeSitterParser treeSitterParser = TreeSitterParser.getInstance();
        for (String filename : inputFolder.list()) {
            String filePath = inputFolder.toPath().resolve(filename).toString();
            File src = new File(filePath);
            String writeTo = new File(outputPath).toPath().resolve(filename).toString();
            if (new File(filePath).isDirectory()) {
                pool.execute(() -> processFolder(filePath, writeTo));
            } else {
                pool.submit(() -> {
                    LanguageEnum ext = Utilities.getFileExtension(src.getName());
                    if (ext != LanguageEnum.UNKNOWN) {
                        try {
                            ResultBean resultBean = treeSitterParser.parseFile(src, Utilities.getLanguage(ext));
                            String result = new ObjectMapper().writeValueAsString(resultBean);
                            File fileR = new File(writeTo + ".json");
                            if (!fileR.getParentFile().exists()) {
                                fileR.getParentFile().mkdirs();
                            }
                            Files.writeString(fileR.toPath(), result);
                        } catch (ParserException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
    }
}
