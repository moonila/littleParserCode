package org.moonila.code.parser.engine;

import org.junit.jupiter.api.Test;
import org.moonila.code.parser.services.ParserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.File;

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
}
