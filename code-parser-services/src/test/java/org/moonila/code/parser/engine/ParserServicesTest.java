package org.moonila.code.parser.engine;

import org.junit.jupiter.api.Test;
import org.moonila.code.parser.services.ParserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;

@SpringBootTest
public class ParserServicesTest {

    @Autowired
    private ParserServices parserServices;

    @Test
    void treeSitterParserJava() {
        String currentDirectory = new File("").getAbsolutePath();
        try {
            String fileName = "src/main/java/org/moonila/code/parser/controller/ParserController.java";
            String result = parserServices.parseFile(new File(currentDirectory, fileName));
            System.out.println(result);
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void treeSitterParserC() {
        String currentDirectory = new File("").getAbsolutePath();
        try {
            String fileName = "src/test/resources/code/ItineraireMetro.c";
            String result = parserServices.parseFile(new File(currentDirectory, fileName));
            System.out.println(result);
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void treeSitterParserDir() {
        String currentDirectory = new File("").getAbsolutePath();
        try {
            String fileDir = new File(currentDirectory, "src/test/resources/code/").getAbsolutePath();
            System.out.println(new Date());
            String result = parserServices.parseDir(fileDir);
            File jsonParent = new File(currentDirectory, "/target/scrDir");
            if(!jsonParent.exists()){
                jsonParent.mkdirs();
            }
            File jsonFile = new File(jsonParent.getAbsolutePath(), "result.json");
            Files.writeString(jsonFile.toPath(), result);
        } catch (ParserException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
