package exam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;

class ParserTest {
    private Parser r;

    @BeforeEach
    void setUp() {
        r = new Parser();
    }

//    @Test
//    void factCheck() {
//        assertAll("Parser",
//                () -> assertTrue(r.validateFact("осень")),
//                () -> assertTrue(r.validateFact("_осень")),
//                () -> assertFalse(r.validateFact("1осень")),
//                () -> assertFalse(r.validateFact("_")),
//                () -> assertFalse(r.validateFact("_1")),
//                () -> assertFalse(r.validateFact("ос$ень")),
//                () -> assertFalse(r.validateFact("_:осень")),
//                () -> assertFalse(r.validateFact("_ос%ень"))
//        );
//    }

    @Test
    void regular(){
        String s = "осень1";
        Pattern p = Pattern.compile("^[_][\\d]*$");
        Matcher m = p.matcher(s);
        System.out.println(m.matches());
//        String string = "Осень && Мокро || kek -> Грязно";
//        String[] aOPb = string.split("\\|{2}|&{2}");
//        System.out.println(aOPb[1]);
//        System.out.println(Arrays.toString(aOPb));
    }

//    @Test
//    void readFactsFail() {
//        String lineFail = "Осень,жарко, V jrhj";
//
//        try {
//            r.parseFacts(lineFail);
//            fail();
//        } catch (InputException e) {
//            System.out.println(e.getProblem());
//        }
//    }
//
//    @Test
//    void readFactsNotFail() {
//
//        String lineNorm = "Осень,жарко, _темно, Мокро";
//
//        try {
//            r.parseFacts(lineNorm);
//        } catch (InputException e) {
//            fail();
//        }
//    }

    @Test
    void readRulesNotFail() {
        try {
            System.out.println(r.parseRule("Осень &&  Мокро || kek-> Грязно"));
            System.out.println(r.parseRule("Осень-> Мокро"));
            System.out.println(r.parseRule("Осень|| Мокро -> Прохладно"));
            System.out.println(r.parseRule("Осень && Мокро -> Грязно"));
            System.out.println(r.parseRule("A&&D||f&&H->Z"));

        } catch (InputException e) {
            System.out.println(e.getProblem());
//            fail();
        }

    }

    @Test
    void readRulesFail() {
        try {
            r.parseRule("Осень && Мокро -> _");
            fail();
        } catch (InputException e) {
            System.out.println(e.getProblem());
        }

    }

    @Test
    void readFile() {
        File file = new File("src\\test.resources\\test.txt");

        try {
            r.parseFile(file.getAbsolutePath());
        } catch (InputException | IOException e) {
            e.printStackTrace();
        }
    }


}