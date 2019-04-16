package exam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private enum ParsingState {Rules, Facts, Finish}

    private enum StringState {BeforeFact, UnderlineFact, Fact, BeforeOperation, AndOperation, OrOperation, Arrow, BeforeResultFact, UnderlineResultFact, ResultFact, AfterResultFact}

    private ParsingState parsingState;
    private StringState stringState;

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
        String[] orSplit = expressionString.split("\\|{2}", -1);
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
        String[] andSplit = s.split("&{2}", -1);
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
        String[] half = currentLine.split("->", -1);
        if ((half.length == 2)) {
            String result = half[1].trim();
            validateFact(result);
            Expression expression = parseOrExpression(half[0].trim());
            return new Rule(expression, result);
        }
        throw new InputException(row);
    }

    public Rule parseRuleByMachine(String line) throws InputException {
        char[] c = line.toCharArray();
        List<Expression> andExpressionList = new ArrayList<>();
        List<Expression> orExpressionList = new ArrayList<>();
        StringBuilder fact = new StringBuilder();
        StringBuilder resultFact = new StringBuilder();
        stringState = StringState.BeforeFact;
        for (int i = 0; i < line.length(); i++) {
            switch (stringState) {
                case BeforeFact:
                    fact.delete(0, fact.length());
                    if (Character.isLetter(c[i])) {
                        stringState = StringState.Fact;
                        fact.append(c[i]);
                        break;
                    }
                    if (c[i] == '_') {
                        stringState = StringState.UnderlineFact;
                        fact.append(c[i]);
                        break;
                    }
                    if (c[i] != ' ') throw new InputException(row);
                    break;
                case UnderlineFact:
                    if (Character.isLetter(c[i])) {
                        stringState = StringState.Fact;
                        fact.append(c[i]);
                        break;
                    }
                    throw new InputException(row);
                case Fact:
                    if (c[i] == '_' || Character.isLetterOrDigit(c[i])) {
                        fact.append(c[i]);
                        break;
                    }
                    if (c[i] == ' ') {
                        stringState = StringState.BeforeOperation;
                        andExpressionList.add(new FactExpression(fact.toString()));
                        break;
                    }
                    if (c[i] == '&') {
                        stringState = StringState.AndOperation;
                        andExpressionList.add(new FactExpression(fact.toString()));
                        break;
                    }
                    if (c[i] == '|') {
                        stringState = StringState.OrOperation;
                        andExpressionList.add(new FactExpression(fact.toString()));
                        break;
                    }
                    if (c[i] == '-') {
                        stringState = StringState.Arrow;
                        andExpressionList.add(new FactExpression(fact.toString()));
                        break;
                    }
                    throw new InputException(row);
                case BeforeOperation:
                    if (c[i] == '&') {
                        stringState = StringState.AndOperation;
                        break;
                    } else if (c[i] == '|') {
                        stringState = StringState.OrOperation;
                        break;
                    }
                    if (c[i] == '-') {
                        stringState = StringState.Arrow;
                        break;
                    }
                    if (c[i] != ' ') throw new InputException(row);
                    break;
                case AndOperation:
                    if (c[i] == '&') {
                        stringState = StringState.BeforeFact;
                        break;
                    }
                    throw new InputException(row);
                case OrOperation:
                    if (c[i] == '|') {
                        stringState = StringState.BeforeFact;
                        orExpressionList.add(new AndExpression(andExpressionList));
                        andExpressionList = new ArrayList<>();
                        break;
                    }
                    throw new InputException(row);
                case Arrow:
                    if (c[i] == '>') {
                        stringState = StringState.BeforeResultFact;
                        orExpressionList.add(new AndExpression(andExpressionList));
                        andExpressionList = new ArrayList<>();
                        break;
                    }
                    throw new InputException(row);
                case BeforeResultFact:
                    if (Character.isLetter(c[i])) {
                        stringState = StringState.ResultFact;
                        resultFact.append(c[i]);
                        break;
                    }
                    if (c[i] == '_') {
                        stringState = StringState.UnderlineResultFact;
                        resultFact.append(c[i]);
                        if (i+1==c.length) throw new InputException(row);
                        break;
                    }
                    if (c[i] != ' ') throw new InputException(row);
                    break;
                case UnderlineResultFact:
                    if (Character.isLetter(c[i])) {
                        stringState = StringState.ResultFact;
                        resultFact.append(c[i]);
                        break;
                    }
                    throw new InputException(row);
                case ResultFact:
                    if (c[i] == '_' || Character.isLetterOrDigit(c[i])) {
                        resultFact.append(c[i]);
                        break;
                    }
                    if (c[i] == ' ') {
                        stringState = StringState.AfterResultFact;
                        break;
                    }
                    throw new InputException(row);
                case AfterResultFact:
                    if (c[i] != ' ') throw new InputException(row);
                    break;
            }
        }
        Expression expression = new OrExpression(orExpressionList);
        return new Rule(expression, resultFact.toString());
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
//                    rules.add(parseRule(currentLine));
                    rules.add(parseRuleByMachine(currentLine));
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
