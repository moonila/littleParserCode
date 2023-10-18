package org.moonila.code.parser.engine.measure;

import java.util.ArrayList;
import java.util.List;

public class NpatCtx {

    private boolean add;
    private boolean multiply;
    private double value;
    private List<NpatCtx> npatCtxList;

    public boolean isAdd() {
        return this.add;
    }

    public boolean getAdd() {
        return this.add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isMultiply() {
        return this.multiply;
    }

    public boolean getMultiply() {
        return this.multiply;
    }

    public void setMultiply(boolean multiply) {
        this.multiply = multiply;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void addNpatCtx(NpatCtx npatCtx) {
        if (npatCtxList == null) {
            npatCtxList = new ArrayList<>();
        }
        npatCtxList.add(npatCtx);
    }

    public List<NpatCtx> getNpatCtxList() {
        return this.npatCtxList;
    }

    public void setNpatCtxList(List<NpatCtx> npatCtxList) {
        this.npatCtxList = npatCtxList;
    }
}
