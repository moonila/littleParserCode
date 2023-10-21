package org.moonila.code.parser.engine.lng;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import ai.serenade.treesitter.Node;

public class StmtUtils {

    static public List<StmtConf> readLngProp(String propName) {
        List<StmtConf> stmtConfs = new ArrayList<>();

        ClassLoader loader = StmtUtils.class.getClassLoader();
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

    static public LngStmtEnum getStmt(Node currNode, List<StmtConf> allStmtConfs) {
        LngStmtEnum result = null;
        String value = currNode.getType();
        List<StmtConf> subList = allStmtConfs.stream()
                .filter(it -> it.getTokens().contains(value))
                .collect(Collectors.toList());

        if (subList.size() == 0) {
            String valTmp = findNodePath(currNode);
            subList = allStmtConfs.stream().filter(
                    it -> (it.getTokens().stream()
                            .filter(t -> valTmp.endsWith(t))
                            .findAny().orElse(null)) != null)
                    .collect(Collectors.toList());
        }
        if (subList.size() == 0 && !currNode.getParent().isNull()) {
            String valTmp = currNode.getParent().getType() + "/" + value;
            subList = allStmtConfs.stream().filter(
                    it -> (it.getTokens().stream()
                            .filter(t -> valTmp.matches(t))
                            .findAny()
                            .orElse(null)) != null)
                    .collect(Collectors.toList());
        }
        if (subList.size() == 1) {
            result = subList.get(0).getName();
        }

        return result;
    }

    public static boolean getStmtByName(Node currNode, List<StmtConf> allStmtConfs,
            LngStmtEnum stmtParent, LngStmtEnum stmtToSearch) {
        StmtConf stmtConfToSearch = allStmtConfs.stream()
                .filter(it -> it.getName().equals(stmtToSearch))
                .findFirst()
                .orElse(null);
        StmtConf stmtConfToParent = allStmtConfs.stream()
                .filter(it -> it.getName().equals(stmtParent))
                .findFirst()
                .orElse(null);
        boolean result = false;
        String value = currNode.getType();
        boolean startsWithParent = stmtConfToParent.getTokens().stream()
                .filter(it -> it.startsWith(value)).findFirst().orElse(null) != null;
        for (String token : stmtConfToSearch.getTokens()) {
            if (startsWithParent && !result) {
                result = compareNode(currNode, value, token);
            } else {
                for (String parentToken : stmtConfToParent.getTokens()) {
                    String firstParent = parentToken.split("/")[0];
                    if (token.startsWith(firstParent)) {
                        Node parent = getNodeParent(currNode, firstParent);
                        if (parent != null) {
                            result = compareNode(parent, firstParent, token);
                        }
                    }
                    if (result) {
                        break;
                    }
                }
            }
            if (result) {
                break;
            }
        }
        return result;
    }

    private static Node getNodeParent(Node curNode, String firstParent) {
        Node result = null;
        if (!curNode.getParent().isNull()) {
            if (curNode.getParent().getType().equals(firstParent)) {
                result = curNode.getParent();
            } else {
                result = getNodeParent(curNode.getParent(), firstParent);
            }
        }
        return result;
    }

    private static boolean compareNode(Node currNode, String value, String token) {
        boolean result = false;
        for (int i = 0; i < currNode.getChildCount(); i++) {
            String name = value + "/" + currNode.getChild(i).getType();
            if (!result && token.equals(name)) {
                result = true;
            }
            if (!result && name.matches(token)) {
                result = true;
            }
        }
        return result;
    }

    private static String findNodePath(Node currNode) {
        String path = null;
        if (!currNode.getParent().isNull()) {
            path = findNodePath(currNode.getParent());
        }

        if (path != null) {
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
