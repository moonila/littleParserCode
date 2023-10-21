package org.moonila.code.parser.engine.measure;

import java.util.ArrayList;
import java.util.List;

import org.moonila.code.parser.engine.lng.LngParser;
import org.moonila.code.parser.engine.lng.LngStmtEnum;

import ai.serenade.treesitter.Node;

public class MeasureUtils {

    public static StmtCtx countStmtMeasure(LngStmtEnum lngStmt, List<Measure> measureList,
                                           Node currNode, LngParser lngParser) {
        StmtCtx stmtCtx = new StmtCtx();
        switch (lngStmt) {
            case IF_STMT -> {
                stmtCtx.setAdd(true);
                updateMeasure(measureList, MeasureEnum.NB_IF);
                boolean containsThen = lngParser.isStmt(currNode, LngStmtEnum.IF_STMT,
                        LngStmtEnum.THEN_STMT);
                if (containsThen) {
                    stmtCtx.setThenStmt(true);
                    updateMeasure(measureList, MeasureEnum.NB_THEN);
                }
            }
            case FOR_STMT -> {
                stmtCtx.setMultiply(true);
                stmtCtx.setLoopStmt(true);
                updateMeasure(measureList, MeasureEnum.NB_FOR);
            }
            case DO_STMT -> {
                stmtCtx.setMultiply(true);
                stmtCtx.setLoopStmt(true);
                updateMeasure(measureList, MeasureEnum.NB_DO);
            }
            case WHILE_STMT -> {
                stmtCtx.setMultiply(true);
                stmtCtx.setLoopStmt(true);
                updateMeasure(measureList, MeasureEnum.NB_WHILE);
            }
            case SWITCH_STMT -> {
                stmtCtx.setAdd(true);
                updateMeasure(measureList, MeasureEnum.NB_SWITCH);
            }
            case SWITCH_CASE_STMT -> {
                updateMeasure(measureList, MeasureEnum.NB_SWITCH_CASE);
                boolean containsBody = lngParser.isStmt(currNode, LngStmtEnum.SWITCH_CASE_STMT,
                        LngStmtEnum.CASE_BODY_STMT);
                if (containsBody) {
                    stmtCtx.setThenStmt(true);
                    stmtCtx.setMultiply(true);
                    updateMeasure(measureList, MeasureEnum.NB_CASE_BODY);
                }
            }
            case TRY_STMT -> {
                stmtCtx.setAdd(true);
                updateMeasure(measureList, MeasureEnum.NB_TRY);
                boolean containsTry = lngParser.isStmt(currNode, LngStmtEnum.TRY_STMT,
                        LngStmtEnum.TRY_BODY_STMT);
                if (containsTry) {
                    updateMeasure(measureList, MeasureEnum.NB_TRY_BODY);
                }
            }
            case CATCH_STMT -> {
                stmtCtx.setAdd(true);
                updateMeasure(measureList, MeasureEnum.NB_CATCH);
            }
            case ELSE_STMT -> {
                stmtCtx.setMultiply(true);
                stmtCtx.setElseStmt(true);
                updateMeasure(measureList, MeasureEnum.NB_ELSE);
            }
            case FINALLY_STMT -> {
                stmtCtx.setMultiply(true);
                updateMeasure(measureList, MeasureEnum.NB_FINALLY);
            }
            case DEFAULT_STMT -> {
                stmtCtx.setMultiply(true);
                updateMeasure(measureList, MeasureEnum.NB_DEFAULT);
            }
            default -> {
            }
        }

        stmtCtx.setStmtName(lngStmt.getStmtProp());
        return stmtCtx;
    }

    public static Measure countNbFct(long fctSize) {
        Measure measureTmp = new Measure();
        measureTmp.setName(MeasureEnum.NB_FCT.name());
        measureTmp.setDescription(MeasureEnum.NB_FCT.getMeasureDesc());
        measureTmp.setValue(fctSize);
        return measureTmp;
    }

    public static double countNpat(StmtCtx stmtCtx, boolean add) {
        List<Double> results = new ArrayList<>();
        if (stmtCtx.getStmtCtxList() != null) {
            for (StmtCtx nCtx : stmtCtx.getStmtCtxList()) {
                double value;
                if (nCtx.isAdd()) {
                    value = countNpat(nCtx, true);
                } else if (nCtx.isLoopStmt()) {
                    value = 1 + countNpat(nCtx, false);
                } else if (nCtx.isMultiply()) {
                    value = Math.max(1, countNpat(nCtx, false));
                } else {
                    value = countNpat(nCtx, add);
                }
                if (value > 0) {
                    results.add(value);
                }
            }
        }
        if (stmtCtx.isAdd() && stmtCtx.isThenStmt() && !stmtCtx.isElseStmt()) {
            results.add(1.0);
        }

        return results.isEmpty() ? 0 : (add ? add(results) : multiply(results));
    }

    public static Double multiply(List<Double> results) {
        return results.stream().reduce(1.0, (x, y) -> x * y);
    }

    public static Double add(List<Double> results) {
        return results.stream().reduce(0.0, Double::sum);
    }

    public static Measure countComplexityCyclomatic(List<Measure> measures) {
        long ifValue = measures.stream()
                .filter(measure -> measure.getName().equals(MeasureEnum.NB_IF.name()))
                .mapToLong(Measure::getValue).sum();

        long forValue = measures.stream()
                .filter(measure -> measure.getName().equals(MeasureEnum.NB_FOR.name()))
                .mapToLong(Measure::getValue).sum();
        long doValue = measures.stream()
                .filter(measure -> measure.getName().equals(MeasureEnum.NB_DO.name()))
                .mapToLong(Measure::getValue).sum();
        long whileValue = measures.stream()
                .filter(measure -> measure.getName().equals(MeasureEnum.NB_WHILE.name()))
                .mapToLong(Measure::getValue).sum();
        long caseValue = measures.stream()
                .filter(measure -> measure.getName().equals(MeasureEnum.NB_SWITCH.name()))
                .mapToLong(Measure::getValue).sum();
        long catchValue = measures.stream()
                .filter(measure -> measure.getName().equals(MeasureEnum.NB_CATCH.name()))
                .mapToLong(Measure::getValue).sum();

        Measure measureCC = new Measure();
        measureCC.setName(MeasureEnum.COUNT_CC.name());
        measureCC.setDescription(MeasureEnum.COUNT_CC.getMeasureDesc());
        long ccValue = ifValue + forValue + doValue + whileValue + caseValue + catchValue;
        measureCC.setValue(ccValue + 1);

        return measureCC;
    }

    private static void updateMeasure(List<Measure> measures, MeasureEnum msrEnum) {
        Measure measureTmp = measures.stream()
                .filter(measure -> msrEnum.name().equals(measure.getName()))
                .findAny()
                .orElse(null);
        if (measureTmp == null) {
            measureTmp = new Measure();
            measureTmp.setName(msrEnum.name());
            measureTmp.setDescription(msrEnum.getMeasureDesc());
            measures.add(measureTmp);
        }
        measureTmp.setValue(measureTmp.getValue() + 1);
    }
}
