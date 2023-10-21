package org.moonila.code.parser.engine.measure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StmtCtx {

    private String stmtName;
    @JsonIgnore
    private boolean add;
    @JsonIgnore
    private boolean multiply;
    @JsonIgnore
    private boolean elseStmt;
    @JsonIgnore
    private boolean thenStmt;
    @JsonIgnore
    private boolean loopStmt;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<StmtCtx> stmtCtxList;

    public boolean isAdd() {
        return this.add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isMultiply() {
        return this.multiply;
    }

    public void setMultiply(boolean multiply) {
        this.multiply = multiply;
    }

    public void addStmtCtx(StmtCtx stmtCtx) {
        if (stmtCtxList == null) {
            stmtCtxList = new ArrayList<>();
        }
        stmtCtxList.add(stmtCtx);
    }

    @JsonProperty("stmtChild")
    public List<StmtCtx> getStmtCtxList() {
        return this.stmtCtxList;
    }

    public boolean isLoopStmt() {
        return loopStmt;
    }

    public void setLoopStmt(boolean loopStmt) {
        this.loopStmt = loopStmt;
    }

    @JsonProperty("name")
    public String getStmtName() {
        return stmtName;
    }

    public void setStmtName(String stmtName) {
        this.stmtName = stmtName;
    }

    public boolean isElseStmt() {
        return elseStmt;
    }

    public void setElseStmt(boolean elseStmt) {
        this.elseStmt = elseStmt;
    }

    public boolean isThenStmt() {
        return thenStmt;
    }

    public void setThenStmt(boolean thenStmt) {
        this.thenStmt = thenStmt;
    }
}
