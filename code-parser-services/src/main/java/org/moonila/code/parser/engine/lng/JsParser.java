package org.moonila.code.parser.engine.lng;

import java.util.List;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;

public class JsParser implements LngParser {

    List<StmtConf> allStmtConfs;

    public JsParser() {
        allStmtConfs = StmtReadProp.getStmtConf("parser/config/js_stmt.properties");
    }

    @Override
    public long getLngTreeSitter() {
        return Languages.javascript();
    }

    @Override
    public LngStmtEnum getLngStmtEnum(Node currNode, boolean searchSubElmt) {
        return StmtReadProp.getStmtConfsByName(currNode, allStmtConfs, searchSubElmt);
    }

}
