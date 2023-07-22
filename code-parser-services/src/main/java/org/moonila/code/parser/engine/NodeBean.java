package org.moonila.code.parser.engine;

import java.util.List;

public class NodeBean {

    String name;
    String description;

    List<NodeBean> child;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<NodeBean> getChild() {
        return child;
    }

    public void setChild(List<NodeBean> child) {
        this.child = child;
    }
}
