package org.moonila.code.parser.services;

import org.moonila.code.parser.Utilities;
import org.moonila.code.parser.engine.ParserException;
import org.moonila.code.parser.engine.TreeSitterParser;
import org.moonila.code.parser.engine.beans.ResultBean;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

@Service
public class ParserServicesImpl implements ParserServices {

    @Override
    public String parseFile(File src) throws ParserException {
        String result;
        try {
            ResultBean resultBean = TreeSitterParser.getInstance().parseFile(src,
                    Utilities.getLanguage(Utilities.getFileExtension(src.getName())));
            result = new ObjectMapper().writeValueAsString(resultBean);
        } catch (ParserException e) {
            System.out.println("an error occurred " + e.getMessage());
            throw e;
        } catch (JsonProcessingException e) {
            System.out.println("an error occurred " + e.getMessage());
            throw new ParserException(e.getMessage(), e.getCause());
        }

        return result;
    }


    @Override
    public void parseDir(String srcDir, String outDir) {
        new FileProcessor().process(srcDir, outDir);
    }
}
