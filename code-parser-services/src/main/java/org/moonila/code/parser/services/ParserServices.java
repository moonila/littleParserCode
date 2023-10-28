package org.moonila.code.parser.services;

import org.moonila.code.parser.engine.ParserException;

import java.io.File;

public interface ParserServices {
    String parseFile(File src) throws ParserException;

    void parseDir(String srcDir,String outDir);
}
