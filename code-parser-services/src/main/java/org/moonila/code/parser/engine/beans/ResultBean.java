package org.moonila.code.parser.engine.beans;

import org.moonila.code.parser.engine.measure.StmtCtx;

import java.util.List;

public class ResultBean {

    private StmtCtx nodeBean;

    private List<Kind> kindList;

    public ResultBean(List<Kind> kindList, StmtCtx nodeBean) {
        this.nodeBean = nodeBean;
        this.kindList = kindList;
    }

    public StmtCtx getNodeBean() {
        return nodeBean;
    }

    public void setNodeBean(StmtCtx nodeBean) {
        this.nodeBean = nodeBean;
    }

    public List<Kind> getKindList() {
        return kindList;
    }

    public void setKindList(List<Kind> kindList) {
        this.kindList = kindList;
    }
}
