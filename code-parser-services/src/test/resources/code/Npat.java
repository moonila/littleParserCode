package code;

public class Npat {
    public int switchM(String val1) {

        switch (config.getBuildType()) {
            case APPLY_MODEL:
            case APPLY_MODEL_DUP:
                if (branch != null) {
                    throw new IllegalArgumentException();
                }
                break;

            default:
                if (branch == null) {
                    throw new IllegalArgumentException();
                }
                break;
        }

        switch (val1) {
            case "if_statement" -> updateMeasure(kind.getMeasureList(), "NB_IF");
            case "for_statement" -> updateMeasure(kind.getMeasureList(), "NB_FOR");
            case "do_statement" -> updateMeasure(kind.getMeasureList(), "NB_DO");
            case "while_statement" -> updateMeasure(kind.getMeasureList(), "NB_WHILE");
        }

        switch (getRdbms()) {
            case POSTGRES:
                parser = new PostgreSQLParser();
                break;

            case ORACLE:
                parser = new OracleSQLParser();
                break;

            default:
                throw new SQLException("Unsupported database");
        }

        return 0;
    }
}
