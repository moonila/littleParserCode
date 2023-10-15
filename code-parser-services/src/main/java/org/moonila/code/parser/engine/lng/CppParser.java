package org.moonila.code.parser.engine.lng;

import java.util.List;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;

public class CppParser implements LngParser {

    List<StmtConf> allStmtConfs;

    public CppParser() {
        allStmtConfs = StmtReadProp.getStmtConf("parser/config/cpp_stmt.properties");
    }

    @Override
    public long getLngTreeSitter() {
        return Languages.cpp();
    }

    @Override
    public LngStmtEnum getLngStmtEnum(Node currNode, boolean searchSubElmt) {
        return StmtReadProp.getStmtConfsByName(currNode, allStmtConfs, searchSubElmt);
    }

}
