package org.moonila.code.parser.engine.measure;

import java.util.List;

import org.moonila.code.parser.engine.lng.LngParser;
import org.moonila.code.parser.engine.lng.LngStmtEnum;

import ai.serenade.treesitter.Node;

public class MeasureUtils {

    public static void countStmtMeasure(LngStmtEnum lngStmt, List<Measure> measureList,
            Node currNode, LngParser lngParser, NpatCtx npatCtx) {
        double value = npatCtx != null ? npatCtx.getValue() : 0.0;
        switch (lngStmt) {
            case IF_STMT:
                value += +1;
                npatCtx.setAdd(true);
                updateMeasure(measureList, MeasureEnum.NB_IF);
                boolean containsThen = lngParser.isStmt(currNode, LngStmtEnum.IF_STMT,
                        LngStmtEnum.THEN_STMT);
                if (containsThen) {
                    updateMeasure(measureList, MeasureEnum.NB_THEN);
                }
                break;
            case FOR_STMT:
                value += 1;
                npatCtx.setMultiply(true);
                updateMeasure(measureList, MeasureEnum.NB_FOR);
                break;
            case DO_STMT:
                value += 1;
                npatCtx.setMultiply(true);
                updateMeasure(measureList, MeasureEnum.NB_DO);
                break;
            case WHILE_STMT:
                value += 1;
                npatCtx.setMultiply(true);
                updateMeasure(measureList, MeasureEnum.NB_WHILE);
                break;
            case SWITCH_STMT:
                value += 1;
                npatCtx.setAdd(true);
                updateMeasure(measureList, MeasureEnum.NB_SWITCH);
                break;
            case SWITCH_CASE_STMT:
                value += 1;
                npatCtx.setMultiply(true);
                updateMeasure(measureList, MeasureEnum.NB_SWITCH_CASE);
                boolean containsBody = lngParser.isStmt(currNode, LngStmtEnum.SWITCH_CASE_STMT,
                        LngStmtEnum.CASE_BODY_STMT);
                if (containsBody) {
                    updateMeasure(measureList, MeasureEnum.NB_CASE_BODY);
                }
                break;
            case TRY_STMT:
                value += 1;
                npatCtx.setAdd(true);
                updateMeasure(measureList, MeasureEnum.NB_TRY);
                boolean containsTry = lngParser.isStmt(currNode, LngStmtEnum.TRY_STMT,
                        LngStmtEnum.TRY_BODY_STMT);
                if (containsTry) {
                    updateMeasure(measureList, MeasureEnum.NB_TRY_BODY);
                }
                break;
            case CATCH_STMT:
                value += 1;
                npatCtx.setAdd(true);
                updateMeasure(measureList, MeasureEnum.NB_CATCH);
                break;
            case ELSE_STMT:
                value += 1;
                npatCtx.setMultiply(true);
                updateMeasure(measureList, MeasureEnum.NB_ELSE);
                break;
            case FINALLY_STMT:
                value += 1;
                npatCtx.setMultiply(true);
                updateMeasure(measureList, MeasureEnum.NB_FINALLY);
                break;
            case DEFAULT_STMT:
                value += 1;
                npatCtx.setMultiply(true);
                updateMeasure(measureList, MeasureEnum.NB_DEFAULT);
            default:
                break;
        }
        if (npatCtx != null) {
            npatCtx.setValue(value);
        }
    }

    public static Measure countNbFct(long fctSize) {
        Measure measureTmp = new Measure();
        measureTmp.setName(MeasureEnum.NB_FCT.name());
        measureTmp.setDescription(MeasureEnum.NB_FCT.getMeasureDesc());
        measureTmp.setValue(fctSize);
        return measureTmp;
    }

    public static double countNpat(NpatCtx npatCtx) {
        double value = 0;
        if (npatCtx.getNpatCtxList() != null) {
            for (NpatCtx nCtx : npatCtx.getNpatCtxList()) {
                value = countNpat(nCtx);
            }
            value = value + Math.pow(2, (npatCtx.getNpatCtxList().size()));
        }

        return value;
    }

    public static Double multiply(List<Double> results) {
        return results.stream().reduce(1.0, (x, y) -> x * y);
    }

    public static Double add(List<Double> results) {
        return results.stream().reduce(0.0, Double::sum);
    }

    public static Measure countComplexitCyclomatic(List<Measure> measures) {
        long ifValue = measures.stream()
                .filter(measure -> measure.getName().equals(MeasureEnum.NB_IF.name()))
                .mapToLong(o -> o.getValue()).sum();

        long forValue = measures.stream()
                .filter(measure -> measure.getName().equals(MeasureEnum.NB_FOR.name()))
                .mapToLong(o -> o.getValue()).sum();
        long doValue = measures.stream()
                .filter(measure -> measure.getName().equals(MeasureEnum.NB_DO.name()))
                .mapToLong(o -> o.getValue()).sum();
        long whileValue = measures.stream()
                .filter(measure -> measure.getName().equals(MeasureEnum.NB_WHILE.name()))
                .mapToLong(o -> o.getValue()).sum();
        long caseValue = measures.stream()
                .filter(measure -> measure.getName().equals(MeasureEnum.NB_SWITCH.name()))
                .mapToLong(o -> o.getValue()).sum();
        long catchValue = measures.stream()
                .filter(measure -> measure.getName().equals(MeasureEnum.NB_CATCH.name()))
                .mapToLong(o -> o.getValue()).sum();

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
