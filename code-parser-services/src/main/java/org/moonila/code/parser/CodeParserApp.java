package org.moonila.code.parser;

import org.moonila.code.parser.engine.TreeSitterParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodeParserApp {

    static {
        TreeSitterParser.initNativeLib();
    }

    public static void main(String[] args) {
        SpringApplication.run(CodeParserApp.class, args);
    }

}
