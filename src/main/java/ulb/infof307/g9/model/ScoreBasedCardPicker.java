package ulb.infof307.g9.model;

import ulb.infof307.g9.model.cards.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ScoreBasedCardPicker is the class used to pick the next card shown when studying.
 * The main goal of the algorithm is to show cards that are less known.
 */
public class ScoreBasedCardPicker extends CardPicker{

    private final int COOL_DOWN_PICK;
    private final int FREQ_MIN;
    private int maxPicked = 0;

    private final static int MINIMUM_COOLDOWN = 4;
    private final static int MINIMUM_FREQUENCE = 10;

    private final static int COOLDOWN_DIVISOR = 3;
    private final static int FREQUENCE_DIVISOR = 2;


    /**
     * Instantiates a new Card picker.
     *
     * @param deck Deck of cards from which the cards will be picked.
     */
    public ScoreBasedCardPicker(Deck deck) {
        this.allCards = deck.getCards();
        this.COOL_DOWN_PICK = Math.min(MINIMUM_COOLDOWN, this.allCards.size() / COOLDOWN_DIVISOR);
        this.FREQ_MIN = Math.min(MINIMUM_FREQUENCE, this.allCards.size() / FREQUENCE_DIVISOR);
    }

    /**
     * Pick a fully random card from a set of cards.
     *
     * @param allCards All the cards that are eligible to be picked.
     * @return The picked card.
     */
    private Card pickRandom(List<Card> allCards) {
        int rand = new Random().nextInt(allCards.size());
        return allCards.get(rand);
    }

    /**
     * Main algorithm. Gets all the cards that are eligible to be picked.
     * It handles a cool down during which a card can't be picked,
     * it picks cards with the lowest score and cards which were not picked in a long time.
     *
     * @return The cards that could be picked.
     */
    public List<Card> getPossibleCards() {
        List<Card> possibleCards = new ArrayList<>();
        List<Card> cardsLessPicked = new ArrayList<>();
        int scoreMin = Integer.MAX_VALUE;

        for (Card current_card : this.allCards) {
            int current_score = current_card.getScore();
            if (current_card.getCoolDown() > 0) {
                // don't pick card if cool down still active
                current_card.setCoolDown(current_card.getCoolDown() - 1);
                continue;
            }

            if (current_score < scoreMin) {
                scoreMin = current_score;
                possibleCards.clear();
            }
            if (current_score == scoreMin) {
                // pick cards with the lowest score
                possibleCards.add(current_card);
            }
            if (current_card.getNbrPicked() < this.maxPicked - this.FREQ_MIN || current_card.getNbrPicked() == 0) {
                // pick card if card not picked in a long time or card never picked
                cardsLessPicked.add(current_card);
            }
        }
        possibleCards.addAll(cardsLessPicked);
        return possibleCards;
    }

    /**
     * Main method. Pick a card from the deck.
     * Determines which cards could be picked at this turn and pick a random one from them.
     *
     * @return the picked card.
     */
    public Card pick() {
        Card picked = pickRandom(this.getPossibleCards());

        picked.setCoolDown(this.COOL_DOWN_PICK);
        picked.setNbrPicked(picked.getNbrPicked() + 1);
        if (picked.getNbrPicked() > this.maxPicked) {
            this.maxPicked = picked.getNbrPicked();
        }
        return picked;
    }

    public int getMaxPicked() {
        return maxPicked;
    }

    public void setMaxPicked(int maxPicked) {
        this.maxPicked = maxPicked;
    }
}
