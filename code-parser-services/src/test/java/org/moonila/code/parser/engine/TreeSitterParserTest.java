package org.moonila.code.parser.engine;

import org.junit.jupiter.api.Test;
import org.moonila.code.parser.engine.measure.Measure;

import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class TreeSitterParserTest {

    @Test
    void treeSitterParserJava() {
        String currentDirectory = new File("").getAbsolutePath();
        try {
            String fileName = "src/test/resources/code/ClSample.java";
            ResultBean resultBean = new TreeSitterParser().generateResultBean(new File(currentDirectory, fileName),
                    LanguageEnum.JAVA);
            assertNotNull(resultBean);
            assertEquals(3,resultBean.getKindList().size());
            Kind kindFile = resultBean.getKindList().get(0);
            assertEquals("FILE", kindFile.getKindType().name());
            assertEquals(36, kindFile.getNbLines());
            assertEquals(1, kindFile.getMeasureList().size());
            assertEquals("NB_FCT", kindFile.getMeasureList().get(0).getName());
            assertEquals(2, kindFile.getMeasureList().get(0).getValue());
            Kind kindFct1 = resultBean.getKindList().get(1);
            assertEquals("FUNCTION", kindFct1.getKindType().name());
            assertEquals(22, kindFct1.getNbLines());
            assertEquals(3, kindFct1.getMeasureList().size());
            Measure ifMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "NB_IF".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(ifMeasure);
            assertEquals(3, ifMeasure.getValue());
            assertEquals("Number of if and else if", ifMeasure.getDescription());

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

            Kind kindFct2 = resultBean.getKindList().get(2);
            assertEquals("FUNCTION", kindFct2.getKindType().name());
            assertEquals(8, kindFct2.getNbLines());
            assertEquals(1, kindFct2.getMeasureList().size());
            Measure switchMeasure = kindFct2.getMeasureList().stream()
                    .filter(measure -> "NB_SWITCH".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(switchMeasure);
            assertEquals(1, switchMeasure.getValue());
            assertEquals("Number of switch", switchMeasure.getDescription());

        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void treeSitterParserC() {
        String currentDirectory = new File("").getAbsolutePath();
        try {
            String fileName = "src/test/resources/code/ItineraireMetro.c";
            ResultBean resultBean = new TreeSitterParser().generateResultBean(new File(currentDirectory, fileName),
                    LanguageEnum.C);
            assertNotNull(resultBean);

            assertEquals(2,resultBean.getKindList().size());
            Kind kindFile = resultBean.getKindList().get(0);
            assertEquals("FILE", kindFile.getKindType().name());
            assertEquals(81, kindFile.getNbLines());
            assertEquals(1, kindFile.getMeasureList().size());
            assertEquals("NB_FCT", kindFile.getMeasureList().get(0).getName());
            assertEquals(1, kindFile.getMeasureList().get(0).getValue());
            Kind kindFct1 = resultBean.getKindList().get(1);
            assertEquals("FUNCTION", kindFct1.getKindType().name());
            assertEquals(63, kindFct1.getNbLines());
            assertEquals(4, kindFct1.getMeasureList().size());
            Measure ifMeasure = kindFct1.getMeasureList().stream()
                    .filter(measure -> "NB_IF".equals(measure.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(ifMeasure);
            assertEquals(6, ifMeasure.getValue());
            assertEquals("Number of if and else if", ifMeasure.getDescription());

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

        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }
}
