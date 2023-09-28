package code;

public class ClSample {

    public void mainFct(String val1) {
        if (val1 != null) {
            System.out.println(val1);
        } else {
            val1 = "toto";
        }
        while (val1.length() != 5) {
            val1 = val1 + "v";
        }

        for (int i = 0; i < val1.length(); i++) {
            val1 = val1 + i;
        }

        if (val1.length() > 10) {
            System.out.println(val1);
        } else if (val1.length() == 5) {
            System.out.println(val1);
        } else {
            System.out.println(val1);
        }
    }

    public void mainFct2(String val1) {
        switch (val1) {
            case "if_statement" -> updateMeasure(kind.getMeasureList(), "NB_IF");
            case "for_statement" -> updateMeasure(kind.getMeasureList(), "NB_FOR");
            case "do_statement" -> updateMeasure(kind.getMeasureList(), "NB_DO");
            case "while_statement" -> updateMeasure(kind.getMeasureList(), "NB_WHILE");
        }
    }
}
