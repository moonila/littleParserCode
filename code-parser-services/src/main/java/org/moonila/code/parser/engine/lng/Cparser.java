package org.moonila.code.parser.engine.lng;

import java.util.List;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;

public class Cparser implements LngParser {

     List<StmtConf> allStmtConfs;

     public Cparser() {
        allStmtConfs = StmtReadProp.getStmtConf("parser/config/c_stmt.properties");
    }

    @Override
    public long getLngTreeSitter() {
        return Languages.c();
    }

    @Override
    public LngStmtEnum getLngStmtEnum(Node currNode, boolean searchSubElmt) {
        return StmtReadProp.getStmtConfsByName(currNode, allStmtConfs, searchSubElmt);
    }    
}
