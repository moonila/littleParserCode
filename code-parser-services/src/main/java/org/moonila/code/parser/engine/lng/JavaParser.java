package org.moonila.code.parser.engine.lng;

import java.util.List;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;

public class JavaParser implements LngParser {

    List<StmtConf> allStmtConfs;

    public JavaParser() {
        allStmtConfs = StmtReadProp.getStmtConf("parser/config/java_stmt.properties");
    }

    @Override
    public long getLngTreeSitter() {
        return Languages.java();
    }

    @Override
    public LngStmtEnum getLngStmtEnum(Node currNode, boolean searchSubElmt) {
        return StmtReadProp.getStmtConfsByName(currNode, allStmtConfs, searchSubElmt);
    }

}
