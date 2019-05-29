package exam;

import org.apache.commons.cli.*;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class Main {

    private static Options makeOptions() {
        Options options = new Options();

        OptionGroup type = new OptionGroup();
        Option i = new Option("i", true, "input");
        i.setArgs(3);
        i.setArgName("property=value");
        i.setOptionalArg(false);
        type.addOption(i);
        type.setRequired(true);
        options.addOptionGroup(type);

        Option o = new Option("o", true, "output");
        o.setArgName("property=value");
        o.setArgs(3);
        options.addOption(o);

        OptionGroup action = new OptionGroup();
        action.addOption(new Option("d", false, "deduce"));
        Option c = new Option("c", false, "convert");
        action.addOption(c);
        action.setRequired(true);
        options.addOptionGroup(action);
        return options;
    }

    public static void main(String[] args) throws PersistenceException {

        try {
            CommandLineParser cmdParser = new DefaultParser();
            CommandLine cmd = cmdParser.parse(makeOptions(), args);
            Properties input = cmd.getOptionProperties("i");
            Engine.Type inputType = null;
            if (input.getProperty("type").equals("txt")) {
                inputType = Engine.Type.Txt;
            }
            if (input.getProperty("type").equals("xml")) {
                inputType = Engine.Type.Xml;
            }
            if (input.getProperty("type").equals("sql")) {
                inputType = Engine.Type.Sql;
            }
            Engine engine = new Engine();
            if (cmd.hasOption("d")) {
                Set<String> fullFacts = engine.deduce(inputType, input.getProperty("file"), input.getProperty("model"));
                System.out.print(makeFullFactsString(fullFacts));
                return;
            }
            if (cmd.hasOption("c")) {
                Properties output = cmd.getOptionProperties("o");
                Engine.Type outputType = null;
                if (output.getProperty("type").equals("xml")) {
                    outputType = Engine.Type.Xml;
                }
                if (output.getProperty("type").equals("sql")) {
                    outputType = Engine.Type.Sql;
                }
                engine.convert(inputType, outputType, input.getProperty("file"), input.getProperty("model"), output.getProperty("file"), output.getProperty("model"));

            }

        } catch (ParseException e) {
            System.out.println("Неверные входные параметры");
        } catch (NullPointerException e) {
            System.out.println("Произошла ошибка");
        }

    }

    private static String makeFullFactsString(Set<String> facts) {
        Iterator<String> fi = facts.iterator();
        String fullFacts = "";
        if (fi.hasNext())
            fullFacts = fullFacts.concat(fi.next());

        while (fi.hasNext()) {
            fullFacts = fullFacts.concat(", ");
            fullFacts = fullFacts.concat(fi.next());
        }
        return fullFacts;
    }
}

