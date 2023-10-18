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
import org.moonila.code.parser.engine.measure.MeasureEnum;
import org.moonila.code.parser.engine.measure.MeasureUtils;
import org.moonila.code.parser.engine.measure.NpatCtx;

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
                NodeBean nodeBean = processChild(currNode, parent, source, true, lngParser, null);
                kind.setStartLine(nodeBean.getStartLine());
                kind.setEndLine(nodeBean.getEndLine());

                List<NodeBean> functions = getChild(nodeBean);
                kind.addMeasure(MeasureUtils.countNbFct((long) functions.size()));

                for (NodeBean fct : functions) {
                    Kind kindFct = new Kind();
                    kindFct.setKindType(fct.getType());
                    kindFct.setStartLine(fct.getStartLine());
                    kindFct.setEndLine(fct.getEndLine());
                    kindFct.getMeasureList().addAll(fct.getMeasureList());
                    String fctName = getNodeBeanName(fct.getDescription());
                    kindFct.setName(fctName);
                    kindList.add(kindFct);

                    Measure measureCC = MeasureUtils.countComplexitCyclomatic(fct.getMeasureList());
                    kindFct.addMeasure(measureCC);

                    double npatValue = MeasureUtils.countNpat(fct.getNpatCtx());
                    Measure measureNpath = new Measure();
                    measureNpath.setName(MeasureEnum.COUNT_NPATH.name());
                    measureNpath.setDescription(MeasureEnum.COUNT_NPATH.getMeasureDesc());
                    measureNpath.setValue((long) npatValue);
                    kindFct.addMeasure(measureNpath);
                }

                return new ResultBean(kindList, nodeBean);
            }
        } catch (IOException | UnsatisfiedLinkError e) {
            e.printStackTrace();
            throw new ParserException(e.getMessage());
        }
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
            LngParser lngParser, NpatCtx npatCtx) {
        NodeBean nodeBean = new NodeBean();
        String type = currNode.getType();
        LngStmtEnum value = lngParser.getLngStmtEnum(currNode);
        boolean isFct = false;
        if (value != null) {
            isFct = LngStmtEnum.FCT_STMT == value;
            nodeBean.setName(type);
            if (isFct) {
                npatCtx = new NpatCtx();
                nodeBean.setNpatCtx(npatCtx);
                // measureNpath.setName(MeasureEnum.COUNT_NPATH.name());
                // measureNpath.setDescription(MeasureEnum.COUNT_NPATH.getMeasureDesc());
                // nodeBean.addMeasure(measureNpath);
                nodeBean.setType(KindType.FUNCTION);
            } else {
                nodeBean.setType(KindType.STATEMENT);
                MeasureUtils.countStmtMeasure(value, parent.getMeasureList(), currNode,
                        lngParser, npatCtx);
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
                NpatCtx newNpatCtx;
                if (npatCtx != null) {
                    newNpatCtx = new NpatCtx();
                    npatCtx.addNpatCtx(newNpatCtx);
                } else {
                    newNpatCtx = npatCtx;
                }

                if (isFct) {
                    child.add(processChild(currNode.getChild(i), nodeBean, source, false, lngParser, newNpatCtx));
                } else {
                    child.add(processChild(currNode.getChild(i), parent, source, false, lngParser, newNpatCtx));
                }
            }
        }
        return nodeBean;
    }

}
