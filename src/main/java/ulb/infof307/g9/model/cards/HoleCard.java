package ulb.infof307.g9.model.cards;

import ulb.infof307.g9.abstracts.CardVisitor;
import ulb.infof307.g9.model.CardType;

/**
 * The HoleCard class represents a flashcard with a question with a hole on one side and an answer on the other.
 */
public class HoleCard extends Card {

    private String holeQuestion;

    private final static String HOLE = "...";

    /**
     * Constructor used to create a HoleCard
     * @param title the title of card
     * @param question the question
     * @param answer the answer
     * @param score the score for this card
     * @param coolDown the cooldown
     * @param nrPicked the number of times the card was picked
     */
    public HoleCard(String title, String question, String answer, int score, int coolDown, int nrPicked) {
        super(title, question, answer, score, coolDown, nrPicked, CardType.HOLE);
        this.holeQuestion = question;
        putHolesIntoQuestion();
    }

    /**
     * Constructor used to create a HoleCard
     * @param title the title of card
     * @param question the question
     * @param answer the answer
     */
    public HoleCard(String title, String question, String answer) {
        this(title, question, answer, 0, 0, 0);
    }

    /**
     * Method used to replace all the words in the question that are equal with the answer with holes.
     * ex.q: Leopold was the king of Belgium et a: Belgium : Leopold was the king of ...
     */
    private void putHolesIntoQuestion() {
        if (!super.getQuestion().contains(this.getAnswer())) {
            return;
        }
        this.holeQuestion = holeQuestion.replaceAll(this.getAnswer(), HOLE);

    }

    public String getQuestionWithHole() {
        return holeQuestion;
    }

    /**
     * method used to verify if 2 hole card are equals
     * @param object a Object
     * @return true if the object is equal to the current HoleCard
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HoleCard card)) {
            return false;
        }
        return this == card || this.getTitle().equals(card.getTitle()) &&
                this.getQuestion().equals(card.getQuestion()) && this.getAnswer().equals(card.getAnswer()) &&
                this.getScore() == card.getScore() && this.getNbrPicked() == card.getNbrPicked();
    }

    /**
     * The accept method is part of the visitor pattern and allows a visitor to interact with an object in a type-safe manner.
     * The method takes a visitor as a parameter and calls the appropriate visit method on the visitor, based on the type of the object.
     * @param cardVisitor the visitor that will interact with the object.
     */
    @Override
    public void accept(CardVisitor cardVisitor) {
        cardVisitor.visit(this);
    }

}
