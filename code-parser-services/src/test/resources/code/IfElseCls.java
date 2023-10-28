package code;

public class IfElseCls {
    public void mainFct(String val1) {
        if (val1 != null) {
            System.out.println(val1);
        }
        if (val1 != null) {
            System.out.println(val1);
        } else {
            val1 = "toto";
        }

        if (val1.length() > 10) {
            System.out.println(val1);
        } else if (val1.length() == 5) {
            System.out.println(val1);
        } else {
            System.out.println(val1);
        }

        if (val1.length() > 10) {
            System.out.println(val1);
        } else if (val1.length() == 5) {
            if (val1 != null) {
                System.out.println(val1);
            } else {
                val1 = "toto";
            }
        } else if (val1.length() == 6) {
            System.out.println(val1);
        } else {
            System.out.println(val1);
        }
    }
}