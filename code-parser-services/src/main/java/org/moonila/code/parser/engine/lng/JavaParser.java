package org.moonila.code.parser.engine.lng;

import java.util.List;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;

public class JavaParser implements LngParser {

    private List<StmtConf> allStmtConfs;

    public JavaParser() {
        allStmtConfs = StmtReadProp.getStmtConf("parser/config/java_stmt.properties");
    }

    @Override
    public long getLngTreeSitter() {
        return Languages.java();
    }

    @Override
    public LngStmtEnum getLngStmtEnum(Node currNode) {
        return StmtReadProp.getStmtConfsByName(currNode, allStmtConfs);
    }

    @Override
    public boolean isStmt(Node currNode, LngStmtEnum stmtParent, LngStmtEnum stmtToSearch) {
        return StmtReadProp.getStmtByName(currNode, allStmtConfs, stmtParent, stmtToSearch);
    }

}
