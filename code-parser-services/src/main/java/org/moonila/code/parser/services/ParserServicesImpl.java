package org.moonila.code.parser.services;

import org.apache.commons.io.FilenameUtils;
import org.moonila.code.parser.engine.LanguageEnum;
import org.moonila.code.parser.engine.ParserException;
import org.moonila.code.parser.engine.TreeSitterParser;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ParserServicesImpl implements ParserServices {

    @Override
    public String parseFile(File src) throws ParserException {
        String result;
        try {
            result = new TreeSitterParser().parseFile(src, getFileExtension(src.getName()));
        } catch (ParserException e) {
            System.out.println("an error occurred " + e.getMessage());
            throw e;
        }

        return result;
    }

    private LanguageEnum getFileExtension(String fileName) {
        return LanguageEnum.getByExtension(FilenameUtils.getExtension(fileName));
    }
}
