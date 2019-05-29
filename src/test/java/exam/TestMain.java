package exam;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestMain {

    private ByteArrayOutputStream output;
    private PrintStream old;
    private static File testDir;

    @BeforeAll
    static void setUp() {
        testDir = new File("src/test/resources");
    }


    @BeforeEach
    void setUpStreams() {
        old = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void cleanUpStreams() {
        System.setOut(old);
    }

    @Test
    void mainSQL() {
        testSQL("name", "A, B, E, F, G, N");
    }

//    @Test
//    void mainSQL2() {
//        testSQL("34", "A, B, E, F, G, N");
//    }

    @Test
    void mainXml() {
        testXml("xmltest", "A, B, D, N");
    }

    @Test
    void mainXml1() {
        testXml("xmltest1", "A, B, E, F, G, N");
    }

    @Test
    void mainGood() {
        testTxt("goodtest", "A, B, C, �����, �����, f, G, I, J, K, L, M, ������, �����, ���������, _�����");
    }

    @Test
    void mainGood1() {
        testTxt("goodtest1", "A, B, C, �����, f, G, I, J, K, L, M, �����1, ������, �����, ���������, Z, _�����");
    }

    @Test
    void mainGood2() {
        testTxt("goodtest2", "A, B, C, �����, �����, f, G, I, J, K, L, M, ������, �����, ���������, Z, _�����");
    }

    @Test
    void mainGood3() {
        testTxt("goodtest3", "A, B, E, F, G, N");
    }

    @Test
    void mainBad() {
        testTxt("badtest", "��������� ������" + System.lineSeparator());
    }

    @Test
    void mainBad1() {
        testTxt("badtest1", "��������� ������" + System.lineSeparator());
    }

    @Test
    void mainBad2() {
        testTxt("badtest2", "��������� ������" + System.lineSeparator());
    }

    @Test
    void mainBad3() {
        testTxt("badtest3", "��������� ������" + System.lineSeparator());
    }

    @Test
    void mainBad4() {
        testTxt("badtest4", "��������� ������" + System.lineSeparator());
    }

    @Test
    void mainBad5() {
        testTxt("badtest5", "��������� ������" + System.lineSeparator());
    }

    @Test
    void mainBad6() {
        testTxt("badtest6", "��������� ������" + System.lineSeparator());
    }

    @Test
    void mainBad7() {
        testTxt("badtest7", "��������� ������" + System.lineSeparator());
    }

    @Test
    void mainBad8() {
        testTxt("badtest8", "��������� ������" + System.lineSeparator());
    }

    @Test
    void mainBad9() {
        testTxt("badtest9", "��������� ������" + System.lineSeparator());
    }

    @Test
    void mainBad10() {
        testTxt("badtest10", "��������� ������" + System.lineSeparator());
    }

    @Test
    void mainBad11() {
        testTxt("badtest11", "��������� ������" + System.lineSeparator());
    }

    @Test
    void mainNoArgs() {
        String[] args = {};

        Main.main(args);
        String template = "�������� ������� ���������" + System.lineSeparator();
        assertEquals(template, output.toString());
    }

//    @Test
//    void mainArgs() {
//        String[] args = {"-txt", "-f", "rules.txt", "-c", "xml", "name", "XmlModel.xsd"};
//
//        Main.main(args);
//        String template = "�������� ������� ���������" + System.lineSeparator();
//        assertEquals(template, output.toString());
//    }

    void testTxt(String filename, String template) {
        String[] args = {"-itype=txt", "-ifile=" + testDir.getAbsolutePath() + File.separator + filename + ".txt", "-d"};

        Main.main(args);

        assertEquals(template, output.toString());
    }

    void testXml(String filename, String template) {
        String[] args = {"-itype=xml", "-ifile=" + testDir.getAbsolutePath() + File.separator + filename + ".xml", "-d"};

        Main.main(args);

        assertEquals(template, output.toString());
    }

    void testSQL(String model, String template) {
        String[] args = {"-itype=sql", "-imodel=name", "-ifile=" + "resources/SQLConfiguration.xml", "-d"};

        Main.main(args);

        assertEquals(template, output.toString());
    }


}