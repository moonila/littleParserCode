package org.moonila.code.parser.engine.lng;

import java.util.List;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;

public class PyParser implements LngParser {

    List<StmtConf> allStmtConfs;

    public PyParser() {
        allStmtConfs = StmtReadProp.getStmtConf("parser/config/py_stmt.properties");
    }

    @Override
    public long getLngTreeSitter() {
        return Languages.python();
    }

    @Override
    public LngStmtEnum getLngStmtEnum(Node currNode, boolean searchSubElmt) {
        return StmtReadProp.getStmtConfsByName(currNode, allStmtConfs, searchSubElmt);
    }
}
