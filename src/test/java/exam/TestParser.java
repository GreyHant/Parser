package exam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TestParser {
    private Parser r;

    @BeforeEach
    void setUp() {
        r = new Parser();
    }

//    @Test
//    void factCheck() {
//        assertAll("Parser",
//                () -> assertTrue(r.validateFact("�����")),
//                () -> assertTrue(r.validateFact("_�����")),
//                () -> assertFalse(r.validateFact("1�����")),
//                () -> assertFalse(r.validateFact("_")),
//                () -> assertFalse(r.validateFact("_1")),
//                () -> assertFalse(r.validateFact("��$���")),
//                () -> assertFalse(r.validateFact("_:�����")),
//                () -> assertFalse(r.validateFact("_��%���"))
//        );
//    }

    @Test
    void regular() {
        String s = "�����1";
        Pattern p = Pattern.compile("^[_][\\d]*$");
        Matcher m = p.matcher(s);
        System.out.println(m.matches());
//        String string = "����� && ����� || kek -> ������";
//        String[] aOPb = string.split("\\|{2}|&{2}");
//        System.out.println(aOPb[1]);
//        System.out.println(Arrays.toString(aOPb));
    }

//    @Test
//    void readFactsFail() {
//        String lineFail = "�����,�����, V jrhj";
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
//        String lineNorm = "�����,�����, _�����, �����";
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
//            System.out.println(r.parseRule("����� &&  ����� || kek-> ������"));
//            System.out.println(r.parseRule("D||(A&& (B || C))-> E"));
//            Rule rule = r.parseRule("(A&&(B||C))&&(D||E)->N");
            Rule rule = r.parseRule("(A|| B) && C-> H");
            Rule rule1 = r.parseRule("(A&&(B||C))&&(D||E)->N");
//            System.out.println(rule);
            Set<String> facts = new HashSet<>();
            facts.add("A");
            facts.add("B");
            List<Rule> rules = new ArrayList<>();
            rules.add(rule);
            rules.add(rule1);
            Model model = new Model(facts, rules);
            JAXBContext context = JAXBContext.newInstance(Model.class);

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            m.marshal(model, System.out);

//            System.out.println(r.parseRule("(A|| B) && C-> H"));
//            System.out.println(r.parseRule("D||(�����&& (����� || C))-> ���������"));
//            System.out.println(r.parseRule("(�����&& (����� || C))||D-> ���������"));
//            System.out.println(r.parseRule("�����&& (����� || C)-> ���������"));
//            System.out.println(r.parseRule("(�����|| �����) && C-> ���������"));
//            System.out.println(r.parseRule("����� && ����� -> ������"));
//            System.out.println(r.parseRule("A&&D||f&&H->Z"));

        } catch (InputException | JAXBException e) {
            e.printStackTrace();
//            System.out.println(e.getProblem());
//            fail();
        }

    }


    @Test
    void readFile() {
        File file = new File("src\\testTxt.resources\\testTxt.txt");

        try {
            r.parseFile(file.getAbsolutePath());
        } catch (InputException | IOException e) {
            e.printStackTrace();
        }
    }


}