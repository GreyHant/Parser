package exam;

import org.apache.commons.cli.*;

public class Main {

    private static Options makeOptions() {
        Options options = new Options();
        Option iFile = new Option("ifile", true, "name of input file");
        iFile.setArgName("filename");
        iFile.setOptionalArg(false);
        iFile.setRequired(true);
        options.addOption(iFile);

        OptionGroup inputType = new OptionGroup();
        inputType.addOption(new Option("itxt", false, "txt input format"));
        inputType.addOption(new Option("ixml", false, "xml input format"));
        Option iSql = new Option("isql", true, "sql input format");
        iSql.setArgName("modelName");
        iSql.setOptionalArg(false);
        inputType.addOption(iSql);
        inputType.setRequired(true);
        options.addOptionGroup(inputType);

        Option oFile = new Option("ofile", true, "name of output file");
        oFile.setRequired(false);
        oFile.setArgName("filename");
        oFile.setOptionalArg(false);
        options.addOption(oFile);

        OptionGroup outputType = new OptionGroup();
        outputType.addOption(new Option("oxml", false, "xml output format"));
        Option oSql = new Option("osql", true, "sql output format");
        oSql.setArgName("modelName");
        oSql.setOptionalArg(false);
        outputType.addOption(oSql);
        options.addOptionGroup(outputType);

        OptionGroup action = new OptionGroup();
        action.addOption(new Option("d", false, "deduce and print all facts"));
        action.addOption(new Option("c", false, "convert format to other format"));
        options.addOptionGroup(action);

        options.addOption(new Option("help", false, "help"));
        return options;
    }

    public static void main(String[] args) {
        Options options = makeOptions();
        CommandLine cmd;
        try {
            CommandLineParser cmdParser = new DefaultParser();
            cmd = cmdParser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Неверные входные параметры");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("rules", options);
            return;
        }
        if (cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("rules", options);
            return;
        }
        Engine.Type inputType = null;
        if (cmd.hasOption("itxt")) {
            inputType = Engine.Type.Txt;
        } else if (cmd.hasOption("ixml")) {
            inputType = Engine.Type.Xml;
        } else if (cmd.hasOption("isql")) {
            inputType = Engine.Type.Sql;
        }
        Presenter presenter = new CMDPresenter();
        Engine engine = new Engine(presenter);
        if (cmd.hasOption("d")) {
            engine.deduce(inputType, cmd.getOptionValue("ifile"), cmd.getOptionValue("isql"));
            return;
        }
        if (cmd.hasOption("c")) {
            Engine.Type outputType;
            if (cmd.hasOption("oxml")) {
                outputType = Engine.Type.Xml;
            } else if (cmd.hasOption("osql")) {
                outputType = Engine.Type.Sql;
            } else {
                System.err.println("Missing output file format!");
                return;
            }
            engine.convert(inputType, outputType, cmd.getOptionValue("ifile"), cmd.getOptionValue("osql"), cmd.getOptionValue("ofile"), cmd.getOptionValue("osql"));
        }


    }
}

