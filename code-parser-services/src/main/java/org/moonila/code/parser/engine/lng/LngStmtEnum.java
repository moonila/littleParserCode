package org.moonila.code.parser.engine.lng;

import java.util.stream.Stream;

public enum LngStmtEnum {

    CATCH_STMT("catch"), SWITCH_CASE_STMT("case"), SWITCH_STMT("switch"), WHILE_STMT("while"),
    FOR_STMT("for"), IF_STMT("if"), ELSE_STMT("else"), THEN_STMT("then"), DO_STMT("do"),
    TRY_STMT("try"), FCT_STMT("fct"), CLS_STMT("class"), ITFC_STMT("interface"), BLCK_STMT("block"),
    EMTPY_STMT("empty"), FINALLY_STMT("finally"), THROW_STMT("throw"), CONTINUE_STMT("continue"),
    RETURN_STMT("return"), BREAK_STMT("break"), DEFAULT_STMT("default"), LOOP_STMT("loop"),
    TRY_BODY_STMT("tryBody"), CASE_BODY_STMT("caseBody");

    private final String stmtProp;

    LngStmtEnum(String stmtProp) {
        this.stmtProp = stmtProp;
    }

    public String getStmtProp() {
        return stmtProp;
    }

    public static Stream<LngStmtEnum> stream() {
        return Stream.of(LngStmtEnum.values()); 
    }

}
