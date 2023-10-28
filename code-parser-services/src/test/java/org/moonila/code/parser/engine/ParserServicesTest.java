package org.moonila.code.parser.engine;

import org.junit.jupiter.api.Test;
import org.moonila.code.parser.services.ParserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ParserServicesTest {

    @Autowired
    private ParserServices parserServices;

    @Test
    void treeSitterParserJava() {
        String currentDirectory = new File("").getAbsolutePath();
        try {
            String fileName = "src/test/resources/code/IfElseCls.java";
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
            File src = new File(currentDirectory, fileName);
            String result = parserServices.parseFile(src);
            System.out.println(result);
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void treeSitterParserDir() {
        String currentDirectory = new File("").getAbsolutePath();
            String fileDir = new File(currentDirectory, "src/test/resources/code/").getAbsolutePath();
            File jsonParent = new File(currentDirectory, "/target/scrDir");
            if (!jsonParent.exists()) {
                jsonParent.mkdirs();
            }
             parserServices.parseDir(fileDir, jsonParent.getAbsolutePath());
            assertTrue(jsonParent.exists());
            File clsampleJson = new File(jsonParent.getAbsoluteFile(), "ClSample.java.json");
            assertTrue(clsampleJson.exists());
    }

}
