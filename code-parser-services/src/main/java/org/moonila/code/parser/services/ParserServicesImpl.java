package org.moonila.code.parser.services;

import org.apache.commons.io.FilenameUtils;
import org.moonila.code.parser.engine.ParserException;
import org.moonila.code.parser.engine.TreeSitterParser;
import org.moonila.code.parser.engine.lng.Cparser;
import org.moonila.code.parser.engine.lng.CppParser;
import org.moonila.code.parser.engine.lng.JavaParser;
import org.moonila.code.parser.engine.lng.JsParser;
import org.moonila.code.parser.engine.lng.LanguageEnum;
import org.moonila.code.parser.engine.lng.LngParser;
import org.moonila.code.parser.engine.lng.PyParser;
import org.moonila.code.parser.engine.lng.TsParser;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ParserServicesImpl implements ParserServices {

    @Override
    public String parseFile(File src) throws ParserException {
        String result;
        try {
            result = new TreeSitterParser().parseFile(src, getLanguage(getFileExtension(src.getName())));
        } catch (ParserException e) {
            System.out.println("an error occurred " + e.getMessage());
            throw e;
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
