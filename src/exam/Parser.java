package exam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {

    private enum ParsingState {Rules, Facts, Finish}

    private enum RuleState {BeforeFact, UnderlineFact, Fact, BeforeOperation, AndOperation, OrOperation, Arrow, BeforeResultFact, UnderlineResultFact, ResultFact, AfterResultFact, Bracket}

    private enum FactsState {BeforeFact, UnderlineFact, Fact, BeforeComma}

    private enum FactState {BeforeFact, UnderlineFact, Fact}

    private int row;

    public Model parseFile(String filePath) throws InputException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        List<Rule> rules = new ArrayList<>();
        Set<String> facts = null;
        row = 0;
        ParsingState parsingState = ParsingState.Rules;
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
                    facts = parseFacts(currentLine);
                    parsingState = ParsingState.Finish;
                    break;
                case Finish:
                    throw new InputException(row);
            }
        }
        return new Model(facts, rules);
    }

    private BracketExpression parseBracket(char[] c, int j) throws InputException {

        List<Expression> andExpressionList = new ArrayList<>();
        List<Expression> orExpressionList = new ArrayList<>();
        StringBuilder fact = null;
        RuleState ruleState = RuleState.BeforeFact;
        BracketExpression bracket = null;
        FactExpression factExpression = null;

        for (int i = j + 1; i < c.length; i++) {
            switch (ruleState) {
                case BeforeFact:
                    if (c[i] == '(') {
                        bracket = parseBracket(c, i);
                        i = bracket.getPosition();
                        ruleState = RuleState.BeforeOperation;
                        break;
                    }
                    if (Character.isLetter(c[i])) {
                        fact = new StringBuilder();
                        fact.append(c[i]);
                        ruleState = RuleState.Fact;
                        break;
                    }
                    if (c[i] == '_') {
                        fact = new StringBuilder();
                        fact.append(c[i]);
                        ruleState = RuleState.UnderlineFact;
                        break;
                    }
                    if (Character.isWhitespace(c[i]))
                        break;

                    throw new InputException(row);
                case UnderlineFact:
                    if (Character.isLetter(c[i]) || c[i] == '_') {
                        fact.append(c[i]);
                        ruleState = RuleState.Fact;
                        break;
                    }
                    throw new InputException(row);
                case Fact:
                    if (c[i] == '_' || Character.isLetterOrDigit(c[i])) {
                        fact.append(c[i]);
                        break;
                    }
                    if (Character.isWhitespace(c[i])) {
                        factExpression = new FactExpression(fact.toString());
                        ruleState = RuleState.BeforeOperation;
                        break;
                    }
                    if (c[i] == '&') {
                        factExpression = new FactExpression(fact.toString());
                        ruleState = RuleState.AndOperation;
                        break;
                    }
                    if (c[i] == '|') {
                        factExpression = new FactExpression(fact.toString());
                        ruleState = RuleState.OrOperation;
                        break;
                    }
                    if (c[i] == ')') {
                        factExpression = new FactExpression(fact.toString());
                        return getBracketExpression(andExpressionList, orExpressionList, bracket, factExpression, i);
                    }
                    throw new InputException(row);
                case BeforeOperation:
                    if (c[i] == '&') {
                        ruleState = RuleState.AndOperation;
                        break;
                    }
                    if (c[i] == '|') {
                        ruleState = RuleState.OrOperation;
                        break;
                    }
                    if (c[i] == ')') {
                        return getBracketExpression(andExpressionList, orExpressionList, bracket, factExpression, i);
                    }
                    if (Character.isWhitespace(c[i]))
                        break;

                    throw new InputException(row);
                case AndOperation:
                    if (c[i] == '&') {
                        if (bracket != null)
                            andExpressionList.add(bracket);
                        else
                            andExpressionList.add(factExpression);
                        ruleState = RuleState.BeforeFact;
                        break;
                    }
                    throw new InputException(row);
                case OrOperation:
                    if (c[i] == '|') {
                        if (andExpressionList.isEmpty())
                            if (bracket != null)
                                orExpressionList.add(bracket);
                            else
                                orExpressionList.add(factExpression);
                        else {
                            if (bracket != null)
                                andExpressionList.add(bracket);
                            else
                                andExpressionList.add(factExpression);
                            orExpressionList.add(new AndExpression(andExpressionList));
                            andExpressionList = new ArrayList<>();
                        }
                        ruleState = RuleState.BeforeFact;
                        break;
                    }
                    throw new InputException(row);

            }
        }

        return null;
    }

    private BracketExpression getBracketExpression(List<Expression> andExpressionList, List<Expression> orExpressionList, BracketExpression bracket, FactExpression factExpression, int i) {
        Expression expression;
        expression = factExpression;
        if (!andExpressionList.isEmpty()) {
            if (bracket != null)
                andExpressionList.add(bracket);
            else
                andExpressionList.add(expression);
            expression = new AndExpression(andExpressionList);
        }
        if (!orExpressionList.isEmpty()) {
            orExpressionList.add(expression);
            expression = new OrExpression(orExpressionList);
        }
        return new BracketExpression(expression, i);
    }

    public Rule parseRule(String line) throws InputException {
        char[] c = line.toCharArray();
        List<Expression> andExpressionList = new ArrayList<>();
        List<Expression> orExpressionList = new ArrayList<>();
        StringBuilder fact = null;
        StringBuilder resultFact = null;
        RuleState ruleState = RuleState.BeforeFact;
        Expression expression = null;
        BracketExpression bracket = null;
        FactExpression factExpression = null;

        for (int i = 0; i < line.length(); i++) {
            switch (ruleState) {
                case BeforeFact:
                    if (c[i] == '(') {
                        bracket = parseBracket(c, i);
                        i = bracket.getPosition();
                        ruleState = RuleState.BeforeOperation;
                        break;
                    }
                    if (Character.isLetter(c[i])) {
                        fact = new StringBuilder();
                        fact.append(c[i]);
                        ruleState = RuleState.Fact;
                        break;
                    }
                    if (c[i] == '_') {
                        fact = new StringBuilder();
                        fact.append(c[i]);
                        ruleState = RuleState.UnderlineFact;
                        break;
                    }
                    if (Character.isWhitespace(c[i]))
                        break;

                    throw new InputException(row);

                case UnderlineFact:
                    if (Character.isLetter(c[i]) || c[i] == '_') {
                        fact.append(c[i]);
                        ruleState = RuleState.Fact;
                        break;
                    }
                    throw new InputException(row);
                case Fact:
                    if (c[i] == '_' || Character.isLetterOrDigit(c[i])) {
                        fact.append(c[i]);
                        break;
                    }
                    if (Character.isWhitespace(c[i])) {
                        factExpression = new FactExpression(fact.toString());
                        ruleState = RuleState.BeforeOperation;
                        break;
                    }
                    if (c[i] == '&') {
                        factExpression = new FactExpression(fact.toString());
                        ruleState = RuleState.AndOperation;
                        break;
                    }
                    if (c[i] == '|') {
                        factExpression = new FactExpression(fact.toString());
                        ruleState = RuleState.OrOperation;
                        break;
                    }
                    if (c[i] == '-') {
                        factExpression = new FactExpression(fact.toString());
                        ruleState = RuleState.Arrow;
                        break;
                    }
                    throw new InputException(row);
                case BeforeOperation:
                    if (c[i] == '&') {
                        ruleState = RuleState.AndOperation;
                        break;
                    }
                    if (c[i] == '|') {
                        ruleState = RuleState.OrOperation;
                        break;
                    }
                    if (c[i] == '-') {
                        ruleState = RuleState.Arrow;
                        break;
                    }
                    if (Character.isWhitespace(c[i]))
                        break;

                    throw new InputException(row);
                case AndOperation:
                    if (c[i] == '&') {
                        if (bracket == null)
                            andExpressionList.add(factExpression);
                        else {
                            andExpressionList.add(bracket);
                            bracket = null;
                        }
                        ruleState = RuleState.BeforeFact;
                        break;
                    }
                    throw new InputException(row);
                case OrOperation:
                    if (c[i] == '|') {
                        if (andExpressionList.isEmpty())
                            if (bracket == null)
                                orExpressionList.add(factExpression);
                            else {
                                orExpressionList.add(bracket);
                                bracket = null;
                            }
                        else {
                            if (bracket == null)
                                andExpressionList.add(factExpression);
                            else
                                andExpressionList.add(bracket);
                            orExpressionList.add(new AndExpression(andExpressionList));
                            andExpressionList = new ArrayList<>();
                        }
                        ruleState = RuleState.BeforeFact;
                        break;
                    }
                    throw new InputException(row);
                case Arrow:
                    if (c[i] == '>') {
                        expression = factExpression;
                        if (!andExpressionList.isEmpty()) {
                            if (bracket == null)
                                andExpressionList.add(expression);
                            else {
                                andExpressionList.add(bracket);
                                bracket = null;
                            }
                            expression = new AndExpression(andExpressionList);
                        }
                        if (!orExpressionList.isEmpty()) {
                            if (bracket == null)
                                orExpressionList.add(expression);
                            else
                                orExpressionList.add(bracket);
                            expression = new OrExpression(orExpressionList);
                        }
                        ruleState = RuleState.BeforeResultFact;
                        break;
                    }
                    throw new InputException(row);
                case BeforeResultFact:
                    resultFact = new StringBuilder();
                    if (Character.isLetter(c[i])) {
                        resultFact.append(c[i]);
                        ruleState = RuleState.ResultFact;
                        break;
                    }
                    if (c[i] == '_') {
                        resultFact.append(c[i]);
                        ruleState = RuleState.UnderlineResultFact;
                        break;
                    }
                    if (Character.isWhitespace(c[i]))
                        break;
                    throw new InputException(row);
                case UnderlineResultFact:
                    if (Character.isLetter(c[i]) || c[i] == '_') {
                        resultFact.append(c[i]);
                        ruleState = RuleState.ResultFact;
                        break;
                    }
                    throw new InputException(row);
                case ResultFact:
                    if (c[i] == '_' || Character.isLetterOrDigit(c[i])) {
                        resultFact.append(c[i]);
                        break;
                    }
                    if (Character.isWhitespace(c[i])) {
                        ruleState = RuleState.AfterResultFact;
                        break;
                    }
                    throw new InputException(row);
                case AfterResultFact:
                    if (Character.isWhitespace(c[i]))
                        break;
                    throw new InputException(row);
            }
        }
        if (ruleState == RuleState.ResultFact || ruleState == RuleState.AfterResultFact)
            return new Rule(expression, resultFact.toString());
        throw new InputException(row);
    }

    public Set<String> parseFacts(String line) throws InputException {
        char[] c = line.toCharArray();
        Set<String> facts = new HashSet<>();
        StringBuilder fact = null;
        FactsState factState = FactsState.BeforeFact;
        for (int i = 0; i < line.length(); i++) {
            switch (factState) {
                case BeforeFact:
                    if (Character.isLetter(c[i])) {
                        fact = new StringBuilder();
                        fact.append(c[i]);
                        factState = FactsState.Fact;
                        break;
                    }
                    if (c[i] == '_') {
                        fact = new StringBuilder();
                        fact.append(c[i]);
                        factState = FactsState.UnderlineFact;
                        break;
                    }
                    if (Character.isWhitespace(c[i])) break;
                    throw new InputException(row);
                case UnderlineFact:
                    if (Character.isLetter(c[i]) || c[i] == '_') {
                        fact.append(c[i]);
                        factState = FactsState.Fact;
                        break;
                    }
                    throw new InputException(row);
                case Fact:
                    if (c[i] == '_' || Character.isLetterOrDigit(c[i])) {
                        fact.append(c[i]);
                        break;
                    }
                    if (Character.isWhitespace(c[i])) {
                        facts.add(fact.toString());
                        factState = FactsState.BeforeComma;
                        break;
                    }
                    if (c[i] == ',') {
                        facts.add(fact.toString());
                        factState = FactsState.BeforeFact;
                        break;
                    }
                    break;
                case BeforeComma:
                    if (c[i] == ',') {
                        factState = FactsState.BeforeFact;
                        break;
                    }
                    if (Character.isWhitespace(c[i])) break;
                    throw new InputException(row);
            }
        }
        if (fact.length() == 1 && Character.isLetter(fact.toString().charAt(0))) facts.add(fact.toString());
        if (factState == FactsState.Fact || factState == FactsState.BeforeComma)
            return facts;
        throw new InputException(row);
    }

}
