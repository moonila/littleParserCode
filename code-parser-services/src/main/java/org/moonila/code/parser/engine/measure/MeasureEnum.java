package org.moonila.code.parser.engine.measure;

public enum MeasureEnum {

    NB_IF("Number of if"), NB_FOR("Number of for"), NB_DO("Number of do/while"),
    NB_TRY("Number of try"), NB_CATCH("Number of catch"), NB_SWITCH_CASE("Number of switch case"),
    NB_SWITCH("Number of switch"), NB_WHILE("Number of while"), NB_THEN("Number of then"),
    NB_ELSE("Number of else"), NB_FCT("Number of function"), NB_FINALLY("Number of finally"),
    NB_DEFAULT("Number of default"), NB_CASE_BODY("Number of case body"),
    NB_TRY_BODY("Number of try body"), COUNT_NPATH("The number of acyclic execution paths"),
    COUNT_CC("Cyclomatic Complexity");

    private final String measureDesc;

    MeasureEnum(String measureDesc) {
        this.measureDesc = measureDesc;
    }

    public String getMeasureDesc() {
        return measureDesc;
    }

}
