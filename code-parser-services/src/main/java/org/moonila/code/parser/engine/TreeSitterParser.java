package org.moonila.code.parser.engine;

import ai.serenade.treesitter.Node;
import ai.serenade.treesitter.Parser;
import ai.serenade.treesitter.Tree;
import ai.serenade.treesitter.TreeCursor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.moonila.code.parser.engine.beans.Kind;
import org.moonila.code.parser.engine.beans.KindType;
import org.moonila.code.parser.engine.beans.NodeBean;
import org.moonila.code.parser.engine.beans.ResultBean;
import org.moonila.code.parser.engine.lng.LngParser;
import org.moonila.code.parser.engine.lng.LngStmtEnum;
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

    private static final String NB_IF_MSR = "NB_IF";
    private static final String NB_FOR_MSR = "NB_FOR";
    private static final String NB_DO_MSR = "NB_DO";

    private static final String NB_TRY_MSR = "NB_TRY";
    private static final String NB_CATCH_MSR = "NB_CATCH";
    private static final String NB_SWITCH_CASE_MSR = "NB_SWITCH_CASE";

    private static final String NB_SWITCH_MSR = "NB_SWITCH";
    private static final String NB_WHILE_MSR = "NB_WHILE";

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

    public String parseFile(File srcFilePath, LngParser lngParser) throws ParserException {
        String result;
        try {
            result = new ObjectMapper().writeValueAsString(generateResultBean(srcFilePath, lngParser));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ParserException(e.getMessage());
        }

        return result;
    }

    public ResultBean generateResultBean(File srcFilePath, LngParser lngParser) throws ParserException {
        List<Kind> kindList = new ArrayList<>();
        Kind kind = new Kind();
        kind.setName(srcFilePath.getName());
        kind.setKindType(KindType.FILE);
        kindList.add(kind);

        try (Parser parser = new Parser()) {
            parser.setLanguage(lngParser.getLngTreeSitter());
            String source = Files.readString(srcFilePath.toPath(), StandardCharsets.UTF_8);
            try (Tree tree = parser.parseString(source)) {
                Node currNode;
                try (TreeCursor cursor = tree.getRootNode().walk()) {
                    currNode = cursor.getCurrentNode();
                    // System.out.println(currNode.getNodeString());
                }
                NodeBean parent = new NodeBean();
                NodeBean nodeBean = processChild(currNode, parent, source, true, lngParser, false);
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
                .filter(measure -> measure.getName().equals(NB_SWITCH_CASE_MSR))
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
                .filter(measure -> measure.getName().equals(NB_IF_MSR))
                .mapToLong(o -> o.getValue()).sum();

        long forValue = measures.stream()
                .filter(measure -> measure.getName().equals(NB_FOR_MSR))
                .mapToLong(o -> o.getValue()).sum();
        long doValue = measures.stream()
                .filter(measure -> measure.getName().equals(NB_DO_MSR))
                .mapToLong(o -> o.getValue()).sum();
        long whileValue = measures.stream()
                .filter(measure -> measure.getName().equals(NB_WHILE_MSR))
                .mapToLong(o -> o.getValue()).sum();
        long caseValue = measures.stream()
                .filter(measure -> measure.getName().equals(NB_SWITCH_MSR))
                .mapToLong(o -> o.getValue()).sum();
        long catchValue = measures.stream()
                .filter(measure -> measure.getName().equals(NB_CATCH_MSR))
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

    private NodeBean processChild(Node currNode, NodeBean parent, String source, boolean isFirst,
            LngParser lngParser, boolean searchSubElmt) {
        NodeBean nodeBean = new NodeBean();
        String type = currNode.getType();
        LngStmtEnum value = lngParser.getLngStmtEnum(currNode, searchSubElmt);

        boolean isFct = false;
        if (value != null) {
            isFct = LngStmtEnum.FCT_STMT == value;
            nodeBean.setName(type);
            if (isFct) {
                nodeBean.setType(KindType.FUNCTION);
            } else {
                searchSubElmt = false;
                switch (value) {
                    case IF_STMT:
                        updateMeasure(parent.getMeasureList(), NB_IF_MSR, "Number of if");
                        searchSubElmt = true;
                        break;
                    case FOR_STMT:
                        updateMeasure(parent.getMeasureList(), NB_FOR_MSR, "Number of for");
                        break;
                    case DO_STMT:
                        updateMeasure(parent.getMeasureList(), NB_DO_MSR, "Number of do/while");
                        break;
                    case WHILE_STMT:
                        updateMeasure(parent.getMeasureList(), NB_WHILE_MSR, "Number of while");
                        break;
                    case SWITCH_STMT:
                        updateMeasure(parent.getMeasureList(), NB_SWITCH_MSR, "Number of switch");
                        break;
                    case SWITCH_CASE_STMT:
                        updateMeasure(parent.getMeasureList(), NB_SWITCH_CASE_MSR, "Number of switch cases");
                        searchSubElmt = true;
                        break;
                    case TRY_STMT:
                        updateMeasure(parent.getMeasureList(), NB_TRY_MSR, "Number of try");
                        searchSubElmt = true;
                        break;
                    case CATCH_STMT:
                        updateMeasure(parent.getMeasureList(), NB_CATCH_MSR, "Number of catch");
                        break;
                    case ELSE_STMT:
                        updateMeasure(parent.getMeasureList(), "NB_ELSE", "Number of else");
                        break;
                    case THEN_STMT:
                        updateMeasure(parent.getMeasureList(), "NB_THEN", "Number of then");
                        break;
                    case CASE_BODY_STMT:
                        updateMeasure(parent.getMeasureList(), "NB_CASE_BODY", "Number of case body");
                        break;
                    case TRY_BODY_STMT:
                        updateMeasure(parent.getMeasureList(), "NB_TRY_BODY", "Number of try body");
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
            nodeBean.setDescription(type);
            nodeBean.setType(KindType.TOKEN);
        }

        nodeBean.setStartLine(currNode.getRange().startRow + 1);
        nodeBean.setEndLine(currNode.getRange().endRow + 1);
        if (currNode.getChildCount() > 0) {
            List<NodeBean> child = new ArrayList<>();
            nodeBean.setChild(child);
            for (int i = 0; i < currNode.getChildCount(); i++) {
                if (isFct) {
                    child.add(processChild(currNode.getChild(i), nodeBean, source, false, lngParser, searchSubElmt));
                } else {
                    child.add(processChild(currNode.getChild(i), parent, source, false, lngParser, searchSubElmt));
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

}
