package ulb.infof307.g9.model;

import ulb.infof307.g9.abstracts.CardVisitor;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.cards.HoleCard;
import ulb.infof307.g9.model.cards.MultiChoiceCard;
import ulb.infof307.g9.model.cards.StandardCard;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Modular class is used to randomize the variables in a question and answer of a card with modular option enabled.
 */
public class Modular implements CardVisitor {

    //questionPatter is used to verify the modularity of the question parsing the string and searching for :text(*): :variable: :text(*): format
    private static final Pattern questionPattern = Pattern.compile("(.*:[\\w]+:.*)+");
    //answerPattern is used to verify the modularity of the answer parsing the string and searching for :variable: :operator: :variable: pattern
//    private static final Pattern answerPattern = Pattern.compile("(:\\w+:(\\s[/+\\-*]\\s)?)+");
    // the pattern in comment doesn't match free number clarify if needed
    private static final Pattern answerPattern = Pattern.compile("(((:\\w+:)|(\\d+))(\\s[/+\\-*]\\s)?)+");

    //variablePattern is used to detect all the variables in fullyModular
    private static final Pattern variablePattern = Pattern.compile("(:[\\w]+:)");
    //List containing all the variables
    private static final List<String> variables = new ArrayList<>();
    //used to stock the value of a variable after randomising it like for example <a,5>
    private Map<String, Integer> variablesValues;
    private final Card card;
    private Random random;

    private final static int RANDOM_VARIABLE_RANGE = 100;
    private final static int MULTIPLY_PRIORITY = 2;
    private final static int ADDITION_PRIORITY = 1;
    private final static int PARENTHESIS_PRIORITY = 0;


    /**
     * Modular card Constructor
     *
     * @param card to randomize the value of
     */
    public Modular(Card card) {
        this.card = card;
        //this.card.setType((this.card instanceof MultiChoiceCard) ? CardType.MODULAR_MULTI : CardType.MODULAR_NORMAL);
        this.card.setType((this.card.getType()==CardType.MULTI_CHOICE || this.card.getType()==CardType.MODULAR_MULTI)?CardType.MODULAR_MULTI : CardType.MODULAR_NORMAL);
    }

    /**
     * Randomizes the variables from the given card
     *
     * @param random the random generator
     * @return the randomized card
     */
    public Card randomize(Random random) {
        this.random=random;
        // the card cannot be randomized in case of bad input in database
        if (!isFullyModular(this.card.getQuestion(), this.card.getAnswer())) return null;
        randomizeValues(random);
        for (String var : variables) {
            // replaces each variable in the answer with their randomized value
            card.setAnswer(getStringWithNumbers(card.getAnswer(), var));

            // replaces each variable in question with their randomized value
            card.setQuestion(getStringWithNumbers(card.getQuestion(), var));
        }
        card.setAnswer(calculate(card.getAnswer().split(" ")));
        card.accept(this);
        return card;
    }

    /**
     * Method used to replace all occurrences of a certain variable with it's computed value
     *
     * @param stringToEdit      where we have to replace a certain variable with it's corresponding value
     * @param variableToReplace in the string
     * @return a string with it's variables edited
     */
    private String getStringWithNumbers(String stringToEdit, String variableToReplace) {
        return stringToEdit.replaceAll(variableToReplace, String.valueOf(variablesValues.get(variableToReplace)));
    }

    /**
     * Creates a Map of variable and their values ex : <a,5>
     *
     * @param random the random generator
     */
    private void randomizeValues(Random random) {
        this.variablesValues = new HashMap<>();
        for (String var : variables) {
            this.variablesValues.put(var, random.nextInt(RANDOM_VARIABLE_RANGE));
        }
    }

    /**
     * checks if a card is modular
     *
     * @param question to check
     * @param answer   to check
     * @return true if the card is modular else false
     */
    public static boolean isFullyModular(String question, String answer) {
        // checks if the parenthesis match 1 opened and 1 closed or none at all.
        Set<String> answerSet = Stream.of(answer).collect(Collectors.toSet());
        if (!((answerSet.contains("(") && answerSet.contains(")"))
                || (!answerSet.contains("(") && !answerSet.contains(")")))) {
            return false;
        }
        // check if the answer and the question format is good
        if (!(questionPattern.matcher(question).matches() && answerPattern.matcher(answer).matches())) return false;

        // checking if the number of variables match
        List<String> listOfQuestionfVariables = new ArrayList<>();
        List<String> listOfAnswerVariables = new ArrayList<>();
        variablePattern.matcher(answer).results().forEach(s -> listOfAnswerVariables.add(s.group()));
        variablePattern.matcher(question).results().forEach(s -> listOfQuestionfVariables.add(s.group()));

        // checking if the two list are equals
        if (listOfQuestionfVariables.containsAll(listOfAnswerVariables)
                && listOfAnswerVariables.containsAll(listOfQuestionfVariables)) {
            // adding the variables to the list of variables for further use
            variables.addAll(listOfAnswerVariables);
            return true;
        }
        return false;
    }

    /**
     * Computes the answer of an expression in infix notation by making it a postfix expression and calculate its result
     *
     * @param elements array of the infix expression
     * @return the result of the expression as a string
     */
    public String calculate(String[] elements) {
        ArrayList<String> result = infixToPostfix(elements);
        Stack<Integer> operand = new Stack<>();
        for (String element : result) {
            //Treats every element of the postfix operation
            try {
                // verify if the element is a numeric
                int value = Integer.parseInt(element);
                operand.push(value);
            } catch (NumberFormatException e) {
                // we do the math
                int value = doMath(element, operand.pop(), operand.pop());
                operand.push(value);
            }
        }

        return Integer.toString(operand.pop());
    }

    /**
     * Converts an infix expression to postfix
     *
     * @param elements infix expression to convert
     * @return array of the expression in form of postfix
     */
    public ArrayList<String> infixToPostfix(String[] elements) {
        ArrayList<String> result = new ArrayList<>();
        Stack<String> operator = new Stack<>();
        HashMap<String, Integer> priority = new HashMap<>(); // give priority to operators
        priority.put("*", MULTIPLY_PRIORITY);
        priority.put("/", MULTIPLY_PRIORITY);
        priority.put("+", ADDITION_PRIORITY);
        priority.put("-", ADDITION_PRIORITY);
        priority.put("(", PARENTHESIS_PRIORITY);
        for (String element : elements) {
            try {
                // verify if element is a number
                Integer.parseInt(element);
                result.add(element);
            } catch (NumberFormatException e) {
                //treats operator to sort them in PEMDAS order
                if (element.equals("(")) {
                    operator.push("(");
                } else if (element.equals(")")) {
                    String topElement = operator.pop();
                    while (!topElement.equals("(")) {
                        result.add(topElement);
                        topElement = operator.pop();
                    }

                } else {
                    while (!operator.isEmpty() && priority.get(operator.peek()) >= priority.get(element)) {
                        result.add(operator.pop());
                    }
                    operator.push(element);
                }
            }
        }
        while (!operator.isEmpty()) {// add remaining operators to the postfix operation
            result.add(operator.pop());
        }
        return result;
    }

    /**
     * does math from string
     * @param operator the mathematical operation
     * @param operand1 the variable
     * @param operand2 the variable
     * @return mathematical result
     */
    public Integer doMath(String operator, int operand1, int operand2) {
        return switch (operator) {
            case "*" -> operand1 * operand2;
            case "/" -> operand2 / operand1;
            case "-" -> operand2 - operand1;
            case "+" -> operand1 + operand2;
            default -> 0;
        };
    }

    @Override
    public void visit(HoleCard holeCard) {

    }

    /**
     * Visits a multi choice card and adds 3 fake answers
     * @param multiChoiceCard visited
     */
    @Override
    public void visit(MultiChoiceCard multiChoiceCard) {
        int answer = Integer.parseInt(multiChoiceCard.getAnswer());
        // adding 3 random fake answers in the range of [0.5 * answer, 1.5 * answer]
        multiChoiceCard.setFake1(String.valueOf(random.nextInt((int) (answer * 0.5), (int) (answer * 1.5))));
        multiChoiceCard.setFake2(String.valueOf(random.nextInt((int) (answer * 0.5), (int) (answer * 1.5))));
        multiChoiceCard.setFake3(String.valueOf(random.nextInt((int) (answer * 0.5), (int) (answer * 1.5))));
    }

    @Override
    public void visit(StandardCard standardCard) {

    }
}
