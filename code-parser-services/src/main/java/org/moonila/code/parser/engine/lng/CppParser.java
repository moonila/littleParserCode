package org.moonila.code.parser.engine.lng;

import java.util.List;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;

public class CppParser implements LngParser {

    private List<StmtConf> allStmtConfs;

    public CppParser() {
        allStmtConfs = StmtUtils.readLngProp("parser/config/cpp_stmt.properties");
    }

    @Override
    public long getLngTreeSitter() {
        return Languages.cpp();
    }

    @Override
    public LngStmtEnum getLngStmtEnum(Node currNode) {
        return StmtUtils.getStmt(currNode, allStmtConfs);
    }

    @Override
    public boolean isStmt(Node currNode, LngStmtEnum stmtParent, LngStmtEnum stmtToSearch) {
        return StmtUtils.getStmtByName(currNode, allStmtConfs, stmtParent, stmtToSearch);
    }

    @Override
    public LanguageEnum getLngEnum() {
        return LanguageEnum.CPP;
    }
}
