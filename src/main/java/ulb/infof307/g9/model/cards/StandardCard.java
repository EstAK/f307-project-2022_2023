package ulb.infof307.g9.model.cards;

import ulb.infof307.g9.abstracts.CardVisitor;
import ulb.infof307.g9.model.CardType;

/**
 * The StandardCard class represents a flashcard with a question on one side and an answer on the other.
 */
public class StandardCard extends Card {

    /**
     * Instantiates a StandardCard.
     * @param title    the title
     * @param question the question
     * @param answer   the answer
     * @param score    the score of the card
     * @param coolDown the cooldown of the card
     * @param nbrPicked the number of times the card was picked
     * @param type     the type of the card
     */
    public StandardCard(String title, String question, String answer, int score, int coolDown, int nbrPicked, CardType type) {
        super(title, question, answer, score, coolDown, nbrPicked, type);
    }

    /**
     * Instantiates a StandardCard.
     * @param title    the title
     * @param question the question
     * @param answer   the answer
     * @param type the type of the card
     */

    public StandardCard(String title, String question, String answer, CardType type) {
        this(title, question, answer, 0, 0, 0, type);
    }

    public StandardCard(String title,String question,String answer){this(title,question,answer,CardType.NORMAL);}

    /**
     * Constructor who creates a new card with the same attributes
     * @param otherCard the other card
     */
    public StandardCard(StandardCard otherCard) {
        super(otherCard.getTitle(),
                otherCard.getQuestion(),
                otherCard.getAnswer(),
                otherCard.getScore(),
                otherCard.getCoolDown(),
                otherCard.getNbrPicked(),
                otherCard.getType());
    }

    /**
     * method used to verify if 2 StandardCard are equals
     * @param object a Object
     * @return true if the object is equal to the current StandardCard
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StandardCard card)) {
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
