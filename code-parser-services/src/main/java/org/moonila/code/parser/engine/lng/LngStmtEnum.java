package org.moonila.code.parser.engine.lng;

import java.util.stream.Stream;

public enum LngStmtEnum {

    BLCK_STMT("block"), BREAK_STMT("break"),
    CATCH_STMT("catch"), CASE_BODY_STMT("caseBody"), CLS_STMT("class"), CONTINUE_STMT("continue"),
    DEFAULT_STMT("default"), DO_STMT("do"),
    ELSE_STMT("else"), EMPTY_STMT("empty"),
    FCT_STMT("fct"), FILE_STMT("file"), FINALLY_STMT("finally"), FOR_STMT("for"),
    GOTO_STMT("goto"),
    IF_STMT("if"), ITFC_STMT("interface"),
    LABEL_STMT("label"),
    RETURN_STMT("return"),
    SWITCH_CASE_STMT("case"), SWITCH_STMT("switch"),
    THEN_STMT("then"), THROW_STMT("throw"), TRY_BODY_STMT("tryBody"), TRY_STMT("try"),
    WHILE_STMT("while");

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
