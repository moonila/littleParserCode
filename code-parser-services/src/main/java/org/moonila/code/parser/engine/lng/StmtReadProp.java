package org.moonila.code.parser.engine.lng;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import ai.serenade.treesitter.Node;

public class StmtReadProp {

    static public List<StmtConf> getStmtConf(String propName) {
        List<StmtConf> stmtConfs = new ArrayList<>();

        ClassLoader loader = StmtReadProp.class.getClassLoader();
        Properties stmProperties = new Properties();
        try (InputStream loaderStr = loader.getResourceAsStream(propName)) {
            stmProperties.load(loaderStr);
            LngStmtEnum.stream().forEach(e -> {
                StmtConf stmtConf = initStmtConf(e, stmProperties);
                if (stmtConf != null) {
                    stmtConfs.add(stmtConf);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stmtConfs;
    }

    static public LngStmtEnum getStmtConfsByName(Node currNode, List<StmtConf> allStmtConfs, boolean searchSubElmt) {
        LngStmtEnum result = null;
        String value = currNode.getType();
        List<StmtConf> subList;
        if (searchSubElmt) {
            String valTmp = currNode.getParent().getType() + "/" + value;
            subList = allStmtConfs.stream().filter(
                    it -> (it.getTokens().stream().filter(t -> valTmp.matches(t)).findAny().orElse(null)) != null)
                    .collect(Collectors.toList());
        } else {
            subList = allStmtConfs.stream().filter(it -> it.getTokens().contains(value))
                    .collect(Collectors.toList());
        }

        if (subList.size() == 0 && !searchSubElmt) {
            String valTmp = findNodePath(currNode);
            subList = allStmtConfs.stream().filter(
                    it -> (it.getTokens().stream().filter(t -> valTmp.endsWith(t)).findAny().orElse(null)) != null)
                    .collect(Collectors.toList());
        }
        if (subList.size() == 1) {
            result = subList.get(0).getName();
        }

        return result;
    }

    private static String findNodePath (Node currNode) {
        String path = null;
        if(!currNode.getParent().isNull()) {
            path = findNodePath(currNode.getParent());
        }

        if(path != null){
            path += "/" + currNode.getType();
        } else {
            path = currNode.getType();
        }

        return path;
    }

    private static StmtConf initStmtConf(LngStmtEnum propName, Properties properties) {
        StmtConf stmtConf = null;
        String stmtProp = properties.getProperty(propName.getStmtProp());
        if (stmtProp != null) {
            stmtConf = new StmtConf();
            stmtConf.setName(propName);
            stmtConf.setTokens(getTokens(stmtProp));
        }

        return stmtConf;
    }

    private static List<String> getTokens(String value) {
        List<String> tokens = new ArrayList<>();
        String[] elements = value.split(";");
        for (String element : elements) {
            if (element.startsWith("tokens:")) {
                String[] tokensTmp = element.replace("tokens:", "").trim().split(",");
                tokens.addAll(Arrays.asList(tokensTmp));
            }
        }

        return tokens;
    }

}
