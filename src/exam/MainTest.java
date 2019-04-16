package exam;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

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
    void mainGood() {

        test("goodtest", "A, B, C, жарко, Осень, f, G, I, J, K, L, M, Грязно, Мокро, Прохладно, _темно");
    }

    @Test
    void mainGood1() {

        test("goodtest1", "A, B, C, Осень, f, G, I, J, K, L, M, жарко1, Грязно, Мокро, Прохладно, Z, _темно");
    }

    @Test
    void mainGood2() {

        test("goodtest2", "A, B, C, жарко, Осень, f, G, I, J, K, L, M, Грязно, Мокро, Прохладно, Z, _темно");
    }

    @Test
    void mainBad() {
        test("badtest", "Ошибка формата данных в строке [14]." + System.lineSeparator());
    }

    @Test
    void mainBad1() {
        test("badtest1", "Ошибка формата данных в строке [12]." + System.lineSeparator());
    }

    @Test
    void mainBad2() {
        test("badtest2", "Ошибка формата данных в строке [12]." + System.lineSeparator());
    }

    @Test
    void mainBad3() {
        test("badtest3", "Ошибка формата данных в строке [1]." + System.lineSeparator());
    }

    @Test
    void mainBad4() {
        test("badtest4", "Ошибка формата данных в строке [2]." + System.lineSeparator());
    }

    @Test
    void mainBad5() {
        test("badtest5", "Ошибка формата данных в строке [3]." + System.lineSeparator());
    }

    @Test
    void mainBad6() {
        test("badtest6", "Ошибка формата данных в строке [6]." + System.lineSeparator());
    }

    @Test
    void mainBad7() {
        test("badtest7", "Ошибка формата данных в строке [11]." + System.lineSeparator());
    }

    @Test
    void mainBad8() {
        test("badtest8", "Ошибка формата данных в строке [13]." + System.lineSeparator());
    }

    @Test
    void mainBad9() {
        test("badtest9", "Ошибка формата данных в строке [2]." + System.lineSeparator());
    }

    @Test
    void mainBad10() {
        test("badtest10", "Ошибка формата данных в строке [2]." + System.lineSeparator());
    }

    @Test
    void mainBad11() {
        test("badtest11", "Ошибка формата данных в строке [1]." + System.lineSeparator());
    }

    @Test
    void mainNoArgs() {
//        createFile(goodFile);
        String[] args = {};

        Main.main(args);
        String template = "Отсутствует имя файла. Введите имя файла в качестве параметра при вызове утилиты." + System.lineSeparator();
        assertEquals(template, output.toString());
    }

    void test(String filename, String template) {
        String[] args = {testDir.getAbsolutePath() + File.separator + filename + ".txt"};

        Main.main(args);

        assertEquals(template, output.toString());
    }

    static File createFile(String s) {
        File file = new File("src\\test.resources\\test.txt");

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