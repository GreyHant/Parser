package exam;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

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
        testSQL("2", "a, b, c, d, e, f");
    }
    @Test
    void mainSQL2() {
        testSQL("34", "A, B, E, F, G, N");
    }

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
        testTxt("goodtest", "A, B, C, жарко, Осень, f, G, I, J, K, L, M, Грязно, Мокро, Прохладно, _темно");
    }

    @Test
    void mainGood1() {
        testTxt("goodtest1", "A, B, C, Осень, f, G, I, J, K, L, M, жарко1, Грязно, Мокро, Прохладно, Z, _темно");
    }

    @Test
    void mainGood2() {
        testTxt("goodtest2", "A, B, C, жарко, Осень, f, G, I, J, K, L, M, Грязно, Мокро, Прохладно, Z, _темно");
    }

    @Test
    void mainGood3() {
        testTxt("goodtest3", "A, B, E, F, G, N");
    }

    @Test
    void mainBad() {
        testTxt("badtest", "Ошибка формата данных в строке [14]." + System.lineSeparator());
    }

    @Test
    void mainBad1() {
        testTxt("badtest1", "Ошибка формата данных в строке [12]." + System.lineSeparator());
    }

    @Test
    void mainBad2() {
        testTxt("badtest2", "Ошибка формата данных в строке [12]." + System.lineSeparator());
    }

    @Test
    void mainBad3() {
        testTxt("badtest3", "Ошибка формата данных в строке [1]." + System.lineSeparator());
    }

    @Test
    void mainBad4() {
        testTxt("badtest4", "Ошибка формата данных в строке [2]." + System.lineSeparator());
    }

    @Test
    void mainBad5() {
        testTxt("badtest5", "Ошибка формата данных в строке [3]." + System.lineSeparator());
    }

    @Test
    void mainBad6() {
        testTxt("badtest6", "Ошибка формата данных в строке [6]." + System.lineSeparator());
    }

    @Test
    void mainBad7() {
        testTxt("badtest7", "Ошибка формата данных в строке [11]." + System.lineSeparator());
    }

    @Test
    void mainBad8() {
        testTxt("badtest8", "Ошибка формата данных в строке [13]." + System.lineSeparator());
    }

    @Test
    void mainBad9() {
        testTxt("badtest9", "Ошибка формата данных в строке [2]." + System.lineSeparator());
    }

    @Test
    void mainBad10() {
        testTxt("badtest10", "Ошибка формата данных в строке [2]." + System.lineSeparator());
    }

    @Test
    void mainBad11() {
        testTxt("badtest11", "Ошибка формата данных в строке [1]." + System.lineSeparator());
    }

    @Test
    void mainNoArgs() {
//        createFile(goodFile);
        String[] args = {};

        Main.main(args);
        String template = "Отсутствует тип данных. Введите тип данных в качестве параметра при вызове утилиты." + System.lineSeparator();
        assertEquals(template, output.toString());
    }

    void testTxt(String filename, String template) {
        String[] args = {"txt", testDir.getAbsolutePath() + File.separator + filename + ".txt"};

        Main.main(args);

        assertEquals(template, output.toString());
    }

    void testXml(String filename, String template) {
        String[] args = {"xml", testDir.getAbsolutePath() + File.separator + filename + ".xml"};

        Main.main(args);

        assertEquals(template, output.toString());
    }

    void testSQL(String idModel, String template) {
        String[] args = {"sql", idModel};

        Main.main(args);

        assertEquals(template, output.toString());
    }

    static File createFile(String s) {
        File file = new File("src\\testTxt.resources\\testTxt.txt");

        try {
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(s);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}