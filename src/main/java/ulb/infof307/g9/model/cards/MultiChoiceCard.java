package ulb.infof307.g9.model.cards;


import ulb.infof307.g9.abstracts.CardVisitor;
import ulb.infof307.g9.model.CardType;

/**
 * The MultiChoiceCard class represents a flashcard with a question on one side and multiple answers on the other.
 */
public class MultiChoiceCard extends Card {

    private String fake1;
    private String fake2;
    private String fake3;

    /**
     * Instantiates a MultiChoiceCard.
     *
     * @param title    the title
     * @param question the question
     * @param answer   the answer
     * @param fake1    the fake answer 1
     * @param fake2    the fake answer 2
     * @param fake3    the fake answer 3
     * @param score    the score of the card
     * @param coolDown the cooldown of the card
     * @param nbrPicked the number of times the card was picked
     * @param type     the type of the card
     */
    public MultiChoiceCard(String title, String question, String answer, String fake1, String fake2, String fake3, int score, int coolDown, int nbrPicked, CardType type) {
        super(title, question, answer, score, coolDown, nbrPicked, type);
        this.fake1 = fake1;
        this.fake2 = fake2;
        this.fake3 = fake3;
    }

    /**
     * Instantiates a MultiChoiceCard.
     * @param title    the title
     * @param question the question
     * @param answer   the answer
     * @param fake1    the fake answer 1
     * @param fake2    the fake answer 2
     * @param fake3    the fake answer 3
     */
    public MultiChoiceCard(String title, String question, String answer, String fake1, String fake2, String fake3) {
        this(title, question, answer, fake1, fake2, fake3, 0, 0, 0, CardType.MULTI_CHOICE);
    }

    /**
     * Copy constructor
     *
     * @param otherCard card to copy
     */
    public MultiChoiceCard(MultiChoiceCard otherCard) {
        super(otherCard.getTitle(),
                otherCard.getQuestion(),
                otherCard.getAnswer(),
                otherCard.getScore(),
                otherCard.getCoolDown(),
                otherCard.getNbrPicked(),
                otherCard.getType());
        this.fake1 = otherCard.getFake1();
        this.fake2 = otherCard.getFake2();
        this.fake3 = otherCard.getFake3();
    }


    @Override
    public String[] getFakes() {
        return new String[]{this.fake1, this.fake2, this.fake3};
    }

    public String getFake1() {
        return fake1;
    }

    public void setFake1(String fake1) {
        this.fake1 = fake1;
    }

    public String getFake2() {
        return fake2;
    }

    public void setFake2(String fake2) {
        this.fake2 = fake2;
    }

    public String getFake3() {
        return fake3;
    }

    public void setFake3(String fake3) {
        this.fake3 = fake3;
    }
    /**
     * method used to verify if 2 MultiChoicecard are equals
     * @param object a Object
     * @return true if the object is equal to the current MultiChoiceCard
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MultiChoiceCard card)) {
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
