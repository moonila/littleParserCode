package org.moonila.code.parser.engine;

import org.junit.jupiter.api.Test;

import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class TreeSitterParserTest {

    @Test
    void treeSitterParserJava() {
        String currentDirectory = new File("").getAbsolutePath();
        try {
            String fileName = "src/main/java/org/moonila/code/parser/controller/ParserController.java";
            ResultBean resultBean = new TreeSitterParser().generateResultBean(new File(currentDirectory, fileName),
                    LanguageEnum.JAVA);
            assertNotNull(resultBean);
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void treeSitterParserC() {
        String currentDirectory = new File("").getAbsolutePath();
        try {
            String fileName = "src/test/resources/code/ItineraireMetro.c";
            ResultBean resultBean = new TreeSitterParser().generateResultBean(new File(currentDirectory, fileName),
                    LanguageEnum.C);
            assertNotNull(resultBean);
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }
}
