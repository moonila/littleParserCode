package org.moonila.code.parser.engine;

import org.junit.jupiter.api.Test;
import org.moonila.code.parser.engine.beans.Kind;
import org.moonila.code.parser.engine.beans.ResultBean;
import org.moonila.code.parser.engine.lng.LanguageEnum;
import org.moonila.code.parser.engine.lng.LngParser;
import org.moonila.code.parser.engine.measure.Measure;
import org.moonila.code.parser.services.ParserServicesImpl;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class TreeSitterParserTest {

    @Test
    void treeSitterParserJava() {
        String currentDirectory = new File("").getAbsolutePath();
        try {
            LngParser lngParser = new ParserServicesImpl().getLanguage(LanguageEnum.JAVA);
            String fileName = "src/test/resources/code/ClSample.java";
            ResultBean resultBean = TreeSitterParser.getInstance().parseFile(
                    new File(currentDirectory, fileName), lngParser);
            assertNotNull(resultBean);
            assertEquals(3, resultBean.getKindList().size());
            Kind kindFile = resultBean.getKindList().get(0);
            assertEquals("FILE", kindFile.getKindType().name());
            assertEquals(60, kindFile.getNbLines());
            assertEquals(1, kindFile.getMeasureList().size());
            assertEquals("NB_FCT", kindFile.getMeasureList().get(0).getName());
            assertEquals(2, kindFile.getMeasureList().get(0).getValue());

            Kind kindFct1 = resultBean.getKindList().get(1);
            assertEquals("public void mainFct(String val1)", kindFct1.getName());
            assertEquals("FUNCTION", kindFct1.getKindType().name());
            assertEquals(5, kindFct1.getStartLine());
            assertEquals(26, kindFct1.getEndLine());
            assertEquals(21, kindFct1.getNbLines());
            assertEquals(7, kindFct1.getMeasureList().size());

            Measure ifMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "NB_IF".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(ifMeasure);
            assertEquals(3, ifMeasure.getValue());
            assertEquals("Number of if", ifMeasure.getDescription());

            Measure thenMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "NB_THEN".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(thenMeasure);
            assertEquals(3, thenMeasure.getValue());
            assertEquals("Number of then", thenMeasure.getDescription());

            Measure elseMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "NB_ELSE".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(elseMeasure);
            assertEquals(3, elseMeasure.getValue());
            assertEquals("Number of else", elseMeasure.getDescription());

            Measure forMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "NB_FOR".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(forMeasure);
            assertEquals(1, forMeasure.getValue());
            assertEquals("Number of for", forMeasure.getDescription());

            Measure whileMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "NB_WHILE".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(whileMeasure);
            assertEquals(1, whileMeasure.getValue());
            assertEquals("Number of while", whileMeasure.getDescription());

            Measure ccMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "COUNT_CC".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(ccMeasure);
            assertEquals(6, ccMeasure.getValue());
            assertEquals("Cyclomatic Complexity", ccMeasure.getDescription());

            Measure npathMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "COUNT_NPATH".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(npathMeasure);
            //assertEquals(24, npathMeasure.getValue());
            assertEquals("The number of acyclic execution paths", npathMeasure.getDescription());

            Kind kindFct2 = resultBean.getKindList().get(2);
            assertEquals("public int mainFct2(String val1)", kindFct2.getName());
            assertEquals("FUNCTION", kindFct2.getKindType().name());
            assertEquals(28, kindFct2.getStartLine());
            assertEquals(59, kindFct2.getEndLine());
            assertEquals(31, kindFct2.getNbLines());
            assertEquals(11, kindFct2.getMeasureList().size());
            Measure switchMeasure = kindFct2.getMeasureList().stream()
                    .filter(measure -> "NB_SWITCH".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(switchMeasure);
            assertEquals(1, switchMeasure.getValue());
            assertEquals("Number of switch", switchMeasure.getDescription());

            Measure caseMeasure = kindFct2.getMeasureList().stream()
                    .filter(measure -> "NB_SWITCH_CASE".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(caseMeasure);
            assertEquals(4, caseMeasure.getValue());
            assertEquals("Number of switch case", caseMeasure.getDescription());

            Measure tryMeasure = kindFct2.getMeasureList().stream()
                    .filter(measure -> "NB_TRY".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(tryMeasure);
            assertEquals(1, tryMeasure.getValue());
            assertEquals("Number of try", tryMeasure.getDescription());

            Measure tryBodyMeasure = kindFct2.getMeasureList().stream()
                    .filter(measure -> "NB_TRY_BODY".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(tryBodyMeasure);
            assertEquals(1, tryBodyMeasure.getValue());
            assertEquals("Number of try body", tryBodyMeasure.getDescription());

            Measure catchMeasure = kindFct2.getMeasureList().stream()
                    .filter(measure -> "NB_CATCH".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(catchMeasure);
            assertEquals(1, catchMeasure.getValue());
            assertEquals("Number of catch", catchMeasure.getDescription());

            Measure ccMeasure2 = kindFct2.getMeasureList().stream()
                    .filter(measure -> "COUNT_CC".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(ccMeasure2);
            assertEquals(7, ccMeasure2.getValue());
            assertEquals("Cyclomatic Complexity", ccMeasure2.getDescription());

            Measure npathMeasure2 = kindFct2.getMeasureList().stream()
                    .filter(measure -> "COUNT_NPATH".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(npathMeasure2);
            //assertEquals(9, npathMeasure2.getValue());
            assertEquals("The number of acyclic execution paths", npathMeasure2.getDescription());

        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void treeSitterParserJavaNpat() {
        String currentDirectory = new File("").getAbsolutePath();
        try {
            LngParser lngParser = new ParserServicesImpl().getLanguage(LanguageEnum.JAVA);
            String fileName = "src/test/resources/code/Npat.java";
            ResultBean resultBean = TreeSitterParser.getInstance().parseFile(
                    new File(currentDirectory, fileName), lngParser);
            assertNotNull(resultBean);
            assertEquals(2, resultBean.getKindList().size());
            Kind kindFile = resultBean.getKindList().get(0);
            assertEquals("FILE", kindFile.getKindType().name());
            assertEquals(43, kindFile.getNbLines());
            assertEquals(1, kindFile.getMeasureList().size());
            assertEquals("NB_FCT", kindFile.getMeasureList().get(0).getName());
            assertEquals(1, kindFile.getMeasureList().get(0).getValue());

            Kind switchM = resultBean.getKindList().get(1);
            assertEquals("public int switchM(String val1)", switchM.getName());
            assertEquals("FUNCTION", switchM.getKindType().name());
            assertEquals(4, switchM.getStartLine());
            assertEquals(42, switchM.getEndLine());
            assertEquals(38, switchM.getNbLines());
            assertEquals(8, switchM.getMeasureList().size());

            Measure switchMeasure = switchM.getMeasureList().stream()
                    .filter(measure -> "NB_SWITCH".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(switchMeasure);
            assertEquals(3, switchMeasure.getValue());
            assertEquals("Number of switch", switchMeasure.getDescription());

            Measure caseMeasure = switchM.getMeasureList().stream()
                    .filter(measure -> "NB_SWITCH_CASE".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(caseMeasure);
            assertEquals(8, caseMeasure.getValue());
            assertEquals("Number of switch case", caseMeasure.getDescription());

            Measure caseBodyMeasure = switchM.getMeasureList().stream()
                    .filter(measure -> "NB_CASE_BODY".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(caseBodyMeasure);
            assertEquals(3, caseBodyMeasure.getValue());
            assertEquals("Number of case body", caseBodyMeasure.getDescription());

            Measure ccMeasure = switchM.getMeasureList().stream()
                    .filter(measure -> "COUNT_CC".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(ccMeasure);
            assertEquals(6, ccMeasure.getValue());
            assertEquals("Cyclomatic Complexity", ccMeasure.getDescription());

            Measure npathMeasure = switchM.getMeasureList().stream()
                    .filter(measure -> "COUNT_NPATH".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(npathMeasure);
           // assertEquals(20, npathMeasure.getValue());
            assertEquals("The number of acyclic execution paths", npathMeasure.getDescription());

        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void treeSitterParserC() {
        String currentDirectory = new File("").getAbsolutePath();
        try {
            String fileName = "src/test/resources/code/ItineraireMetro.c";
            LngParser lngParser = new ParserServicesImpl().getLanguage(LanguageEnum.C);
            ResultBean resultBean = TreeSitterParser.getInstance().parseFile(
                    new File(currentDirectory, fileName), lngParser);
            assertNotNull(resultBean);

            assertEquals(2, resultBean.getKindList().size());
            Kind kindFile = resultBean.getKindList().get(0);
            assertEquals("FILE", kindFile.getKindType().name());
            assertEquals(80, kindFile.getNbLines());
            assertEquals(1, kindFile.getMeasureList().size());
            assertEquals("NB_FCT", kindFile.getMeasureList().get(0).getName());
            assertEquals(1, kindFile.getMeasureList().get(0).getValue());

            Kind kindFct1 = resultBean.getKindList().get(1);
            assertEquals("int main(int argc, char *argv[])", kindFct1.getName());
            assertEquals("FUNCTION", kindFct1.getKindType().name());
            assertEquals(18, kindFct1.getStartLine());
            assertEquals(81, kindFct1.getEndLine());
            assertEquals(63, kindFct1.getNbLines());
            assertEquals(7, kindFct1.getMeasureList().size());
            Measure ifMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "NB_IF".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(ifMeasure);
            assertEquals(6, ifMeasure.getValue());
            assertEquals("Number of if", ifMeasure.getDescription());

            Measure elseMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "NB_ELSE".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(elseMeasure);
            assertEquals(4, elseMeasure.getValue());
            assertEquals("Number of else", elseMeasure.getDescription());

            Measure forMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "NB_FOR".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(forMeasure);
            assertEquals(2, forMeasure.getValue());
            assertEquals("Number of for", forMeasure.getDescription());

            Measure whileMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "NB_WHILE".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(whileMeasure);
            assertEquals(1, whileMeasure.getValue());
            assertEquals("Number of while", whileMeasure.getDescription());

            Measure doMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "NB_DO".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(doMeasure);
            assertEquals(1, doMeasure.getValue());
            assertEquals("Number of do/while", doMeasure.getDescription());

            Measure ccMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "COUNT_CC".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(ccMeasure);
            assertEquals(11, ccMeasure.getValue());
            assertEquals("Cyclomatic Complexity", ccMeasure.getDescription());

            Measure npathMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "COUNT_NPATH".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(npathMeasure);
           // assertEquals(480, npathMeasure.getValue());
            assertEquals("The number of acyclic execution paths", npathMeasure.getDescription());

        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }
}
