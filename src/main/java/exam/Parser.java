package exam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {

    private int deepFlag;

    private enum ParsingState {Rules, Facts, Finish}

    private enum RuleState {BeforeFact, BeforeResultFact, UnderlineResultFact, ResultFact, AfterResultFact}

    private enum ExpressionState {BeforeFact, UnderlineFact, Fact, BeforeOperation, AndOperation, OrOperation, Arrow}

    private enum FactsState {BeforeFact, UnderlineFact, Fact, BeforeComma}

    private int row, position;

    private char[] charArray;

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

    public Rule parseRule(String line) throws InputException {

        charArray = line.toCharArray();
        deepFlag = 0;
        StringBuilder resultFact = null;
        RuleState ruleState = RuleState.BeforeFact;
        Expression expression = null;

        for (position = 0; position < charArray.length; position++) {
            switch (ruleState) {
                case BeforeFact:
                    expression = parseExpression();
                    ruleState = RuleState.BeforeResultFact;
                    break;
                case BeforeResultFact:
                    resultFact = new StringBuilder();
                    if (Character.isLetter(charArray[position])) {
                        resultFact.append(charArray[position]);
                        ruleState = RuleState.ResultFact;
                        break;
                    }
                    if (charArray[position] == '_') {
                        resultFact.append(charArray[position]);
                        ruleState = RuleState.UnderlineResultFact;
                        break;
                    }
                    if (Character.isWhitespace(charArray[position]))
                        break;
                    throw new InputException(row);
                case UnderlineResultFact:
                    if (Character.isLetter(charArray[position]) || charArray[position] == '_') {
                        resultFact.append(charArray[position]);
                        ruleState = RuleState.ResultFact;
                        break;
                    }
                    throw new InputException(row);
                case ResultFact:
                    if (charArray[position] == '_' || Character.isLetterOrDigit(charArray[position])) {
                        resultFact.append(charArray[position]);
                        break;
                    }
                    if (Character.isWhitespace(charArray[position])) {
                        ruleState = RuleState.AfterResultFact;
                        break;
                    }
                    throw new InputException(row);
                case AfterResultFact:
                    if (Character.isWhitespace(charArray[position]))
                        break;
                    throw new InputException(row);
            }
        }
        if (ruleState == RuleState.ResultFact || ruleState == RuleState.AfterResultFact)
            return new Rule(expression, resultFact.toString());
        throw new InputException(row);
    }

    private Expression parseExpression() throws InputException {

        List<Expression> andExpressionList = new ArrayList<>();
        List<Expression> orExpressionList = new ArrayList<>();
        StringBuilder fact = null;
        ExpressionState expressionState = ExpressionState.BeforeFact;
        Expression factExpression = null;

        for (; position < charArray.length; position++) {
            switch (expressionState) {
                case BeforeFact:
                    if (charArray[position] == '(') {
                        position++;
                        deepFlag++;
                        factExpression = parseExpression();
                        expressionState = ExpressionState.BeforeOperation;
                        break;
                    }
                    if (Character.isLetter(charArray[position])) {
                        fact = new StringBuilder();
                        fact.append(charArray[position]);
                        expressionState = ExpressionState.Fact;
                        break;
                    }
                    if (charArray[position] == '_') {
                        fact = new StringBuilder();
                        fact.append(charArray[position]);
                        expressionState = ExpressionState.UnderlineFact;
                        break;
                    }
                    if (Character.isWhitespace(charArray[position]))
                        break;

                    throw new InputException(row);
                case UnderlineFact:
                    if (Character.isLetter(charArray[position]) || charArray[position] == '_') {
                        fact.append(charArray[position]);
                        expressionState = ExpressionState.Fact;
                        break;
                    }
                    throw new InputException(row);
                case Fact:
                    if (charArray[position] == '_' || Character.isLetterOrDigit(charArray[position])) {
                        fact.append(charArray[position]);
                        break;
                    }
                    if (Character.isWhitespace(charArray[position])) {
                        factExpression = new FactExpression(fact.toString());
                        expressionState = ExpressionState.BeforeOperation;
                        break;
                    }
                    if (charArray[position] == '&') {
                        factExpression = new FactExpression(fact.toString());
                        expressionState = ExpressionState.AndOperation;
                        break;
                    }
                    if (charArray[position] == '|') {
                        factExpression = new FactExpression(fact.toString());
                        expressionState = ExpressionState.OrOperation;
                        break;
                    }
                    if (charArray[position] == '-') {
                        if (deepFlag != 0) throw new InputException(row);
                        factExpression = new FactExpression(fact.toString());
                        expressionState = ExpressionState.Arrow;
                        break;
                    }
                    if (charArray[position] == ')') {
                        deepFlag--;
                        factExpression = new FactExpression(fact.toString());
                        return makeExpression(andExpressionList, orExpressionList, factExpression);
                    }
                    throw new InputException(row);
                case BeforeOperation:
                    if (charArray[position] == '&') {
                        expressionState = ExpressionState.AndOperation;
                        break;
                    }
                    if (charArray[position] == '|') {
                        expressionState = ExpressionState.OrOperation;
                        break;
                    }
                    if (charArray[position] == ')') {
                        deepFlag--;
                        return makeExpression(andExpressionList, orExpressionList, factExpression);
                    }
                    if (charArray[position] == '-') {
                        if (deepFlag != 0) throw new InputException(row);
                        expressionState = ExpressionState.Arrow;
                        break;
                    }
                    if (Character.isWhitespace(charArray[position]))
                        break;

                    throw new InputException(row);
                case AndOperation:
                    if (charArray[position] == '&') {
                        andExpressionList.add(factExpression);
                        expressionState = ExpressionState.BeforeFact;
                        break;
                    }
                    throw new InputException(row);
                case OrOperation:
                    if (charArray[position] == '|') {
                        if (andExpressionList.isEmpty())
                            orExpressionList.add(factExpression);
                        else {
                            andExpressionList.add(factExpression);
                            orExpressionList.add(new AndExpression(andExpressionList));
                            andExpressionList = new ArrayList<>();
                        }
                        expressionState = ExpressionState.BeforeFact;
                        break;
                    }
                    throw new InputException(row);
                case Arrow:
                    if (charArray[position] == '>') {
                        return makeExpression(andExpressionList, orExpressionList, factExpression);
                    }
                    throw new InputException(row);
            }
        }
        throw new InputException(row);
    }

    private Expression makeExpression(List<Expression> andExpressionList, List<Expression> orExpressionList, Expression factExpression) {
        Expression expression;
        expression = factExpression;
        if (!andExpressionList.isEmpty()) {
            andExpressionList.add(expression);
            expression = new AndExpression(andExpressionList);
        }
        if (!orExpressionList.isEmpty()) {
            orExpressionList.add(expression);
            expression = new OrExpression(orExpressionList);
        }
        return expression;
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
