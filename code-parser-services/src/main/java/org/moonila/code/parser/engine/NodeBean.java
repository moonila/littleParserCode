package org.moonila.code.parser.engine;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.moonila.code.parser.engine.measure.Measure;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeBean {

    private String name;
    private String description;
    private List<NodeBean> child;

    private List<Measure> measureList;

    private KindType type;

    private int startLine;

    private int endLine;


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

    public List<Measure> getMeasureList() {
        if (this.measureList == null) {
            this.measureList = new ArrayList<>();
        }
        return measureList;
    }

    public void addMeasure(Measure measure) {
        if (this.measureList == null) {
            this.measureList = new ArrayList<>();
        }
        measureList.add(measure);
    }

    public KindType getType() {
        return type;
    }

    public void setType(KindType type) {
        this.type = type;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }
}
