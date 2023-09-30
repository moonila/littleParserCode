package org.moonila.code.parser.engine;

import java.util.List;

public class ResultBean {

    public ResultBean(List<Kind> kindList, NodeBean nodeBean) {
        this.nodeBean = nodeBean;
        this.kindList = kindList;
    }

    private NodeBean nodeBean;

    private List<Kind> kindList;

    public NodeBean getNodeBean() {
        return nodeBean;
    }

    public void setNodeBean(NodeBean nodeBean) {
        this.nodeBean = nodeBean;
    }

    public List<Kind> getKindList() {
        return kindList;
    }

    public void setKindList(List<Kind> kindList) {
        this.kindList = kindList;
    }
}
