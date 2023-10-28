package org.moonila.code.parser.engine.lng;

import java.util.Arrays;

public enum LanguageEnum {
    JAVA(new String[]{"java", ".java"}),
    C(new String[]{".c", "c", "h", ".h"}),
    CPP(new String[]{".cpp", "cpp"}),

    TS(new String[]{".ts", "ts"}),
    JS(new String[]{".js", "js"}),
    PY(new String[]{".py", "py"}),
    UNKNOWN(null);

    private String[] extensions;

    private LanguageEnum(String[] extensions) {
        this.extensions = extensions;
    }

    public static LanguageEnum getByExtension(String extension) {
        return Arrays.stream(values()).filter(it -> it.containsExt(extension)).findAny().orElse(LanguageEnum.UNKNOWN);
    }

    public boolean containsExt(String extension) {
        boolean result = false;
        if (this.extensions != null) {
            result = Arrays.asList(extensions).contains(extension);
        }
        return result;
    }

}
