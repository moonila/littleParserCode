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
        try {
           String fileName = "/home/sandra/Projets/parser-code/code-parser-services/src/main/java/org/moonila/code/parser/controller/ParserController.java";
           String result = parserServices.parseFile(new File(fileName));
           System.out.println(result);
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }
}
