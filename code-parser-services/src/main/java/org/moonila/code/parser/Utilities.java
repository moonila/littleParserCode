package org.moonila.code.parser;

import org.apache.commons.io.FilenameUtils;
import org.moonila.code.parser.engine.lng.*;
import org.moonila.code.parser.engine.ParserException;

public class Utilities {

    public static LngParser getLanguage(LanguageEnum language) throws ParserException {
        LngParser lngVal = null;
        switch (language) {
            case JAVA -> lngVal = new JavaParser();
            case TS -> lngVal = new TsParser();
            case JS -> lngVal = new JsParser();
            case C -> lngVal = new Cparser();
            case CPP -> lngVal = new CppParser();
            case PY -> lngVal = new PyParser();
            case UNKNOWN -> throw new ParserException("The file language is unknown");
            default -> {
            }
        }
        return lngVal;
    }

    public static LanguageEnum getFileExtension(String fileName) {
        return LanguageEnum.getByExtension(FilenameUtils.getExtension(fileName));
    }
}
