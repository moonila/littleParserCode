package org.moonila.code.parser.engine;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;
import ai.serenade.treesitter.Parser;
import ai.serenade.treesitter.Tree;
import ai.serenade.treesitter.TreeCursor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.moonila.code.parser.engine.measure.Measure;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class TreeSitterParser {

    private List<Kind> kindList;

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
        kindList = new ArrayList<>();
        String result = null;
        try {
            result = new ObjectMapper().writeValueAsString(generateResultBean(srcFilePath, language));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ParserException(e.getMessage());
        }

        return result;
    }

    public ResultBean generateResultBean(File srcFilePath, LanguageEnum language) throws ParserException {
        kindList = new ArrayList<>();
        String result;
        System.out.println("File to be parsed " + srcFilePath.getAbsolutePath());
        Kind kind = new Kind();
        kind.setName(srcFilePath.getName());
        kind.setKindType(KindType.FILE);
        kind.setNbLines(getLineCount(srcFilePath));
        kindList.add(kind);

        try (Parser parser = new Parser()) {
            parser.setLanguage(getLanguage(language));
            String source = Files.readString(srcFilePath.toPath(), StandardCharsets.UTF_8);
            try (Tree tree = parser.parseString(source)) {
                Node currNode;
                try (TreeCursor cursor = tree.getRootNode().walk()) {
                    currNode = cursor.getCurrentNode();
                }
                NodeBean nodeBean = processChild(currNode, source, true, kind);
                ResultBean resultBean = new ResultBean();
                resultBean.setKindList(kindList);
                resultBean.setNodeBean(nodeBean);
                return resultBean;
            }
        } catch (IOException | UnsatisfiedLinkError e) {
            e.printStackTrace();
            throw new ParserException(e.getMessage());
        }
    }

    private static long getLineCount(File file) {
        try (Stream<String> lines = Files.lines(file.toPath())) {
            return lines.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private NodeBean processChild(Node currNode, String source, boolean isFirst, Kind kind) {
        NodeBean nodeBean = new NodeBean();
        String value = currNode.getType();
        boolean isFct = isFunction(value);
        Kind fctKind = null;
        if (currNode.isNamed()) {
            nodeBean.setName(value);
            if (isFct) {
                fctKind = new Kind();
                fctKind.setKindType(KindType.FUNCTION);
                fctKind.setName(source.substring(currNode.getStartByte(), currNode.getEndByte()));
                fctKind.setNbLines(currNode.getRange().startRow - currNode.getRange().endRow);
                kindList.add(fctKind);
                updateMeasure(kind.getMeasureList(), "NB_FCT");
            } else {
                switch (value) {
                    case "if_statement" -> updateMeasure(kind.getMeasureList(), "NB_IF");
                    case "for_statement" -> updateMeasure(kind.getMeasureList(), "NB_FOR");
                    case "do_statement" -> updateMeasure(kind.getMeasureList(), "NB_DO");
                    case "while_statement" -> updateMeasure(kind.getMeasureList(), "NB_WHILE");
                }
            }
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
                if (isFct) {
                    child.add(processChild(currNode.getChild(i), source, false, fctKind));
                } else {
                    child.add(processChild(currNode.getChild(i), source, false, kind));
                }
            }
        }
        return nodeBean;
    }

    private void updateMeasure(List<Measure> measures, String name) {
        Measure measureTmp = measures.stream()
                .filter(measure -> name.equals(measure.getName()))
                .findAny()
                .orElse(null);
        if (measureTmp == null) {
            measureTmp = new Measure();
            measureTmp.setName(name);
            measures.add(measureTmp);
        }
        measureTmp.setValue(measureTmp.getValue() + 1);
    }

    private boolean isFunction(String type) {
        return type.equals("method_declaration")
                || type.equals("function_definition")
                || type.equals("function")
                || type.equals("method_definition")
                || type.equals("function_declarator");
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
