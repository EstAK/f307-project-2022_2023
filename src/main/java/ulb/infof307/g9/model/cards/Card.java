package ulb.infof307.g9.model.cards;

import ulb.infof307.g9.abstracts.CardVisitor;
import ulb.infof307.g9.model.CardType;

/**
 * The Card class represents a flashcard with a question on one side and an answer on the other.
 */
public abstract class Card {
    private String answer;
    private String question;
    private String title;
    private int score = 0;
    private CardType type;
    private int coolDown;
    private int nbrPicked;

    private final static int SCORE_MAX = 10;
    private final static int SCORE_MIN = -10;

    /**
     * Constructor used to create a card
     * @param title the title of card
     * @param question the question
     * @param answer the answer
     * @param score the score for this card
     * @param coolDown the cooldown
     * @param nbrPicked the number of times the card was picked
     * @param type the type of the card
     */
    public Card(String title, String question, String answer, int score, int coolDown, int nbrPicked, CardType type) {
        setType(type);
        setTitle(title);
        setQuestion(question);
        setAnswer(answer);
        setScore(score);
        setCoolDown(coolDown);
        setNbrPicked(nbrPicked);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = Math.max(SCORE_MIN, Math.min(SCORE_MAX, score));
    }

    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public int getNbrPicked() {
        return nbrPicked;
    }

    public void setNbrPicked(int nbrPicked) {
        this.nbrPicked = nbrPicked;
    }

    @Override
    public abstract boolean equals(Object object);

    @Override
    public String toString() {
        return this.getTitle();
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public String[] getFakes() {
        return null;
    }

    public abstract void accept(CardVisitor cardVisitor);

}
