package org.moonila.code.parser.engine;

import org.moonila.code.parser.engine.measure.Measure;

import java.util.ArrayList;
import java.util.List;

public class Kind {

    private String name;
    private KindType kindType;

    private List<Measure> measureList;

    private long nbLines;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KindType getKindType() {
        return kindType;
    }

    public void setKindType(KindType kindType) {
        this.kindType = kindType;
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

    public long getNbLines() {
        return nbLines;
    }

    public void setNbLines(long nbLines) {
        this.nbLines = nbLines;
    }
}
