package org.moonila.code.parser.services;

import org.apache.commons.io.FilenameUtils;
import org.moonila.code.parser.engine.ParserException;
import org.moonila.code.parser.engine.TreeSitterParser;
import org.moonila.code.parser.engine.beans.ResultBean;
import org.moonila.code.parser.engine.lng.Cparser;
import org.moonila.code.parser.engine.lng.CppParser;
import org.moonila.code.parser.engine.lng.JavaParser;
import org.moonila.code.parser.engine.lng.JsParser;
import org.moonila.code.parser.engine.lng.LanguageEnum;
import org.moonila.code.parser.engine.lng.LngParser;
import org.moonila.code.parser.engine.lng.PyParser;
import org.moonila.code.parser.engine.lng.TsParser;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ParserServicesImpl implements ParserServices {

    @Override
    public String parseFile(File src) throws ParserException {
        String result;
        try {
            ResultBean resultBean = TreeSitterParser.getInstance().parseFile(src, getLanguage(getFileExtension(src.getName())));
            result = new ObjectMapper().writeValueAsString(resultBean);
        } catch (ParserException e) {
            System.out.println("an error occurred " + e.getMessage());
            throw e;
        } catch (JsonProcessingException e) {
            System.out.println("an error occurred " + e.getMessage());
            throw new ParserException(e.getMessage(), e.getCause());
        }

        return result;
    }

    @Override
    public String parseDir(String srcDir) throws ParserException {
        String result;
        Path start = Paths.get(srcDir);
        List<ResultBean> allFiles = new ArrayList<>();
        TreeSitterParser treeSitterParser = TreeSitterParser.getInstance();
        try (Stream<Path> paths = Files.walk(start)) {
            paths.forEach(path -> {
                File src = path.toFile();
                if (src.isFile()) {
                    try {
                        LanguageEnum ext = getFileExtension(src.getName());
                        if (ext != LanguageEnum.UNKNOWN) {
                            allFiles.add(treeSitterParser.parseFile(src, getLanguage(ext)));
                        }
                    } catch (ParserException e) {
                       System.out.println(e.getMessage());
                    }
                }
            });

        } catch (IOException e) {
            System.out.println("an error occurred " + e.getMessage());
            throw new ParserException(e.getMessage(), e.getCause());
        }

        System.out.println("Number of files processed: "+allFiles.size());
        try {
            result = new ObjectMapper().writeValueAsString(allFiles);
        } catch (JsonProcessingException e) {
            throw new ParserException(e.getMessage(), e.getCause());
        }
        return result;

    }

    public LngParser getLanguage(LanguageEnum language) {
        LngParser lngVal = null;
        switch (language) {
            case JAVA -> lngVal = new JavaParser();
            case TS -> lngVal = new TsParser();
            case JS -> lngVal = new JsParser();
            case C -> lngVal = new Cparser();
            case CPP -> lngVal = new CppParser();
            case PY -> lngVal = new PyParser();
            default -> {
            }
        }
        return lngVal;
    }

    private LanguageEnum getFileExtension(String fileName) {
        return LanguageEnum.getByExtension(FilenameUtils.getExtension(fileName));
    }
}
