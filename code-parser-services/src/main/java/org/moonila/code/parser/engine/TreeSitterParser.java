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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        try {
            result = new ObjectMapper().writeValueAsString(generateResultBean(srcFilePath, language));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ParserException(e.getMessage());
        }

        return result;
    }

    public ResultBean generateResultBean(File srcFilePath, LanguageEnum language) throws ParserException {
        List<Kind> kindList = new ArrayList<>();
        Kind kind = new Kind();
        kind.setName(srcFilePath.getName());
        kind.setKindType(KindType.FILE);
        kindList.add(kind);

        try (Parser parser = new Parser()) {
            parser.setLanguage(getLanguage(language));
            String source = Files.readString(srcFilePath.toPath(), StandardCharsets.UTF_8);
            try (Tree tree = parser.parseString(source)) {
                Node currNode;
                try (TreeCursor cursor = tree.getRootNode().walk()) {
                    currNode = cursor.getCurrentNode();
                    //System.out.println(currNode.getNodeString());
                }
                NodeBean parent = new NodeBean();
                NodeBean nodeBean = processChild(currNode, parent, source, true);
                kind.setStartLine(nodeBean.getStartLine());
                kind.setEndLine(nodeBean.getEndLine());

                List<NodeBean> functions = getChild(nodeBean);
                Measure measureTmp = new Measure();
                measureTmp.setName("NB_FCT");
                measureTmp.setDescription("Number of functions");
                measureTmp.setValue((long) functions.size());
                kind.addMeasure(measureTmp);

                for (NodeBean fct : functions) {
                    Kind kindFct = new Kind();
                    kindFct.setKindType(fct.getType());
                    kindFct.setStartLine(fct.getStartLine());
                    kindFct.setEndLine(fct.getEndLine());
                    kindFct.getMeasureList().addAll(fct.getMeasureList());
                    String fctName = getNodeBeanName(fct.getDescription());
                    kindFct.setName(fctName);
                    kindList.add(kindFct);

                    Measure measureCC = countComplexitCyclomatic(fct.getMeasureList());
                    kindFct.addMeasure(measureCC);

                    
                   // double npatValue = Math.pow(2, (measureCC.getValue() - 1));
                    Measure measureNpath = countNpath(fct.getMeasureList(), (measureCC.getValue() - 1));
                    kindFct.addMeasure(measureNpath);
                }

                return new ResultBean(kindList, nodeBean);
            }
        } catch (IOException | UnsatisfiedLinkError e) {
            e.printStackTrace();
            throw new ParserException(e.getMessage());
        }
    }

    private Measure countNpath(List<Measure> measures, long measureCC) {
        long elseValue = measures.stream()
                .filter(measure -> measure.getName().equals("NB_ELSE"))
                .mapToLong(o -> o.getValue()).sum();
        long caseValue = measures.stream()
                .filter(measure -> measure.getName().equals("NB_SWITCH_CASE"))
                .mapToLong(o -> o.getValue()).sum();

        long npatValue = measureCC * (2 * (elseValue + caseValue));
        Measure measureNpath = new Measure();
        measureNpath.setName("COUNT_NPATH");
        measureNpath.setDescription("The number of acyclic execution paths");
        measureNpath.setValue(npatValue);

        return measureNpath;
    }

    private Measure countComplexitCyclomatic(List<Measure> measures) {
        long ifValue = measures.stream()
                .filter(measure -> measure.getName().equals("NB_IF"))
                .mapToLong(o -> o.getValue()).sum();

        long forValue = measures.stream()
                .filter(measure -> measure.getName().equals("NB_FOR"))
                .mapToLong(o -> o.getValue()).sum();
        long doValue = measures.stream()
                .filter(measure -> measure.getName().equals("NB_DO"))
                .mapToLong(o -> o.getValue()).sum();
        long whileValue = measures.stream()
                .filter(measure -> measure.getName().equals("NB_WHILE"))
                .mapToLong(o -> o.getValue()).sum();
        long caseValue = measures.stream()
                .filter(measure -> measure.getName().equals("NB_SWITCH"))
                .mapToLong(o -> o.getValue()).sum();
        long catchValue = measures.stream()
                .filter(measure -> measure.getName().equals("NB_CATCH"))
                .mapToLong(o -> o.getValue()).sum();

        Measure measureCC = new Measure();
        measureCC.setName("COUNT_CC");
        measureCC.setDescription("Cyclomatic Complexity");
        long ccValue = ifValue + forValue + doValue + whileValue + caseValue + catchValue;
        measureCC.setValue(ccValue + 1);

        return measureCC;
    }

    private List<NodeBean> getChild(NodeBean nodeBean) {
        List<NodeBean> functions = new ArrayList<>();
        if (nodeBean.getChild() != null && !nodeBean.getChild().isEmpty()) {
            functions = new ArrayList<>(nodeBean.getChild()
                    .stream()
                    .filter(nodeTmp -> KindType.FUNCTION.equals(nodeTmp.getType())).toList());
            for (NodeBean nodeTmp : nodeBean.getChild()) {
                functions.addAll(getChild(nodeTmp));
            }
        }
        return functions;
    }

    private String getNodeBeanName(String description) {
        String name = null;
        Pattern pattern = Pattern.compile("^(.*?)\\{");
        String tmpSrt = description.replace("\n", "");
        Matcher matcher = pattern.matcher(tmpSrt);
        if (matcher.find()) {
            name = matcher.group(0).replace("{", "").trim();
        }
        if (name == null) {
            name = description;
        }
        return name;
    }

    private NodeBean processChild(Node currNode, NodeBean parent, String source, boolean isFirst) {
        NodeBean nodeBean = new NodeBean();
        String value = currNode.getType();
        boolean isFct = isFunction(value);
        if (currNode.isNamed()) {
            nodeBean.setName(value);
            if (isFct) {
                nodeBean.setType(KindType.FUNCTION);
            } else {
                switch (value) {
                    case "if_statement":
                        if (currNode.getParent().getType().equals("if_statement")) {
                            updateMeasure(parent.getMeasureList(), "NB_ELSE_IF", "Number of else if");
                        } else {
                            updateMeasure(parent.getMeasureList(), "NB_IF", "Number of if");
                        }
                        break;
                    case "for_statement":
                        updateMeasure(parent.getMeasureList(), "NB_FOR", "Number of for");
                        break;
                    case "do_statement":
                        updateMeasure(parent.getMeasureList(), "NB_DO", "Number of do/while");
                        break;
                    case "while_statement":
                        updateMeasure(parent.getMeasureList(), "NB_WHILE", "Number of while");
                        break;
                    case "switch_expression":
                        updateMeasure(parent.getMeasureList(), "NB_SWITCH", "Number of switch");
                        break;
                    case "switch_rule":
                        updateMeasure(parent.getMeasureList(), "NB_SWITCH_CASE", "Number of switch cases");
                        break;
                    case "catch_type":
                        updateMeasure(parent.getMeasureList(), "NB_CATCH", "Number of catch");
                        break;
                    default:
                        break;
                }
                nodeBean.setType(KindType.STATEMENT);
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
            nodeBean.setType(KindType.TOKEN);
            if (value.equals("else")) {
                updateMeasure(parent.getMeasureList(), "NB_ELSE", "Number of else");
            }
        }
        nodeBean.setStartLine(currNode.getRange().startRow + 1);
        nodeBean.setEndLine(currNode.getRange().endRow + 1);
        if (currNode.getChildCount() > 0) {
            List<NodeBean> child = new ArrayList<>();
            nodeBean.setChild(child);
            for (int i = 0; i < currNode.getChildCount(); i++) {
                if (isFct) {
                    child.add(processChild(currNode.getChild(i), nodeBean, source, false));
                } else {
                    child.add(processChild(currNode.getChild(i), parent, source, false));
                }
            }
        }
        return nodeBean;
    }

    private void updateMeasure(List<Measure> measures, String name, String description) {
        Measure measureTmp = measures.stream()
                .filter(measure -> name.equals(measure.getName()))
                .findAny()
                .orElse(null);
        if (measureTmp == null) {
            measureTmp = new Measure();
            measureTmp.setName(name);
            measureTmp.setDescription(description);
            measures.add(measureTmp);
        }
        measureTmp.setValue(measureTmp.getValue() + 1);
    }

    private boolean isFunction(String type) {
        return type.equals("method_declaration")
                || type.equals("function_definition")
                || type.equals("function")
                || type.equals("method_definition");
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
