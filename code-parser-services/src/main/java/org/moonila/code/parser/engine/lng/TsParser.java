package org.moonila.code.parser.engine.lng;

import java.util.List;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;

public class TsParser implements LngParser {

    private List<StmtConf> allStmtConfs;

    public TsParser() {
        allStmtConfs = StmtUtils.readLngProp("parser/config/ts_stmt.properties");
    }

    @Override
    public long getLngTreeSitter() {
        return Languages.typescript();
    }

    @Override
    public LngStmtEnum getLngStmtEnum(Node currNode) {
        return StmtUtils.getStmt(currNode, allStmtConfs);
    }

    @Override
    public boolean isStmt(Node currNode, LngStmtEnum stmtParent, LngStmtEnum stmtToSearch) {
        return StmtUtils.getStmtByName(currNode, allStmtConfs, stmtParent, stmtToSearch);
    }
}
