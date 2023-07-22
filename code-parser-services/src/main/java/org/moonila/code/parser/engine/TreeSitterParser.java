package org.moonila.code.parser.engine;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;
import ai.serenade.treesitter.Parser;
import ai.serenade.treesitter.Tree;
import ai.serenade.treesitter.TreeCursor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TreeSitterParser {

    static {
        initNativeLib();
    }

    public TreeSitterParser() {

    }

    public static void initNativeLib() {
        String libName;
        if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            libName = "libjava-tree-sitter.dll";
        } else {
            libName = "libjava-tree-sitter.so";
        }
        String tmpdir = System.getProperty("java.io.tmpdir");
        File lib = new File(tmpdir, libName);
        String libPath = lib.getAbsolutePath();
        if (!lib.exists()) {
            System.out.println("Save native lib in " + tmpdir);
            try (InputStream inStrm = TreeSitterParser.class.getClassLoader().getResourceAsStream(libName)) {
                if (inStrm != null) {

                    FileUtils.copyInputStreamToFile(inStrm, lib);
                } else {
                    throw new ParserException("The library " + libName + " is not found");
                }
            } catch (IOException | ParserException e) {
                e.printStackTrace();
            }
        }
        try {
            System.load(lib.getAbsolutePath());
            System.out.println("The library used is " + libPath);
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }

    public String parseFile(File srcFilePath, LanguageEnum language) throws ParserException {
        String result;
        System.out.println("File to be parsed " + srcFilePath.getAbsolutePath());
        try (Parser parser = new Parser()) {
            parser.setLanguage(getLanguage(language));
            String source = Files.readString(srcFilePath.toPath(), StandardCharsets.UTF_8);
            try (Tree tree = parser.parseString(source)) {
                Node currNode;
                try (TreeCursor cursor = tree.getRootNode().walk()) {
                    currNode = cursor.getCurrentNode();
                }
                NodeBean nodeBean = processChild(currNode, source, true);
                ObjectMapper mapper = new ObjectMapper();
                result = mapper.writeValueAsString(nodeBean);
            }
        } catch (IOException | UnsatisfiedLinkError e) {
            e.printStackTrace();
            throw new ParserException(e.getMessage());
        }
        return result;
    }


    private NodeBean processChild(Node currNode, String source, boolean isFirst) {
        NodeBean nodeBean = new NodeBean();
        String value = currNode.getType();
        if (currNode.isNamed()) {
            nodeBean.setName(value);
            if (!isFirst) {
                String text = source.substring(currNode.getStartByte(), currNode.getEndByte());
                if (!text.isEmpty()) {
                    nodeBean.setDescription(text);
                }
            }
        } else {
            nodeBean.setName("token");
            nodeBean.setDescription(value);
        }

        if (currNode.getChildCount() > 0) {
            List<NodeBean> child = new ArrayList<>();
            nodeBean.setChild(child);
            for (int i = 0; i < currNode.getChildCount(); i++) {
                child.add(processChild(currNode.getChild(i), source, false));
            }
        }
        return nodeBean;
    }

    private long getLanguage(LanguageEnum language) {
        long lngVal = 0;
        switch (language) {
            case JAVA -> lngVal = Languages.java();
            case TS -> lngVal = Languages.typescript();
            case JS -> lngVal = Languages.javascript();
            case C -> lngVal = Languages.c();
            case CPP -> lngVal = Languages.cpp();
            case PY -> lngVal = Languages.python();
            default -> {
            }
        }
        return lngVal;
    }

}
