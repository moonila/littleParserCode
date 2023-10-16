package org.moonila.code.parser.engine.lng;

import java.util.List;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;

public class TsParser implements LngParser {

    List<StmtConf> allStmtConfs;

    public TsParser() {
        allStmtConfs = StmtReadProp.getStmtConf("parser/config/ts_stmt.properties");
    }

    @Override
    public long getLngTreeSitter() {
        return Languages.typescript();
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
