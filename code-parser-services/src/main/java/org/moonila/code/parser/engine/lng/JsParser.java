package org.moonila.code.parser.engine.lng;

import java.util.List;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;

public class JsParser implements LngParser {

    private List<StmtConf> allStmtConfs;

    public JsParser() {
        allStmtConfs = StmtUtils.readLngProp("parser/config/js_stmt.properties");
    }

    @Override
    public long getLngTreeSitter() {
        return Languages.javascript();
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
        return LanguageEnum.JS;
    }
}
