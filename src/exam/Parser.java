package exam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private enum ParsingState {Rules, Facts, Finish}

    private ParsingState parsingState;

    private int row;

    private Pattern pattern;

    public Parser() {
        pattern = Pattern.compile("_*\\p{IsAlphabetic}+[\\p{IsAlphabetic}_\\d]*");
    }

    public void validateFact(String fact) throws InputException {
        Matcher matcher = pattern.matcher(fact);
        if (!matcher.matches())
            throw new InputException(row);
    }


    public void parseFacts(String currentLine, Set<String> facts) throws InputException {
        String[] allFacts = currentLine.split(",", -1);
        for (String fact : allFacts) {
            fact = fact.trim();
            validateFact(fact);
            facts.add(fact);
        }
    }

    private Expression parseOrExpression(String expressionString) throws InputException {
        Expression expression;
        List<Expression> expressionList = new ArrayList<>();
        String[] orSplit = expressionString.split("\\|{2}",-1);
        if (orSplit.length > 1) {
            for (String s : orSplit) {
                expressionList.add(parseAndExpression(s));
            }
            expression = new OrExpression(expressionList);
        } else {
            expression = parseAndExpression(expressionString);
        }
        return expression;
    }

    private Expression parseAndExpression(String s) throws InputException {
        List<Expression> expressionList = new ArrayList<>();
        String[] andSplit = s.split("&{2}",-1);
        if (andSplit.length > 1) {
            for (String ss : andSplit) {
                expressionList.add(parseFactExpression(ss));
            }
        } else {
            return parseFactExpression(s);
        }
        return new AndExpression(expressionList);
    }

    private FactExpression parseFactExpression(String expressionString) throws InputException {
        String s = expressionString.trim();
        validateFact(s);
        return new FactExpression(s);
    }

    public Rule parseRule(String currentLine) throws InputException {
        String[] half = currentLine.split("->",-1);
        if ((half.length == 2)) {
            String result = half[1].trim();
            validateFact(result);
            Expression expression = parseOrExpression(half[0].trim());
            return new Rule(expression, result);
        }
        throw new InputException(row);
    }

    public Model parseFile(String filePath) throws InputException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        List<Rule> rules = new ArrayList<>();
        Set<String> facts = new HashSet<>();
        row = 0;
        parsingState = ParsingState.Rules;
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            row++;
            switch (parsingState) {
                case Rules:
                    if (currentLine.equals("----------------------------------------------------------------")) {
                        parsingState = ParsingState.Facts;
                        break;
                    }
                    rules.add(parseRule(currentLine));
                    break;
                case Facts:
                    parseFacts(currentLine, facts);
                    parsingState = ParsingState.Finish;
                    break;
                case Finish:
                    throw new InputException(row);
            }
        }
        return new Model(facts, rules);
    }


}
