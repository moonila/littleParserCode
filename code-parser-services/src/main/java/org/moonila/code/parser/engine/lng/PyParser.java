package org.moonila.code.parser.engine.lng;

import java.util.List;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;

public class PyParser implements LngParser {

    private List<StmtConf> allStmtConfs;

    public PyParser() {
        allStmtConfs = StmtReadProp.getStmtConf("parser/config/py_stmt.properties");
    }

    @Override
    public long getLngTreeSitter() {
        return Languages.python();
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
