package org.moonila.code.parser.engine.lng;

import java.util.List;

public class StmtConf {

    private LngStmtEnum name;
    private List<String> tokens;

    public LngStmtEnum getName() {
        return this.name;
    }

    public void setName(LngStmtEnum name) {
        this.name = name;
    }

    public List<String> getTokens() {
        return this.tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

}
