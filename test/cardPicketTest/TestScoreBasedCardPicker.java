package cardPicketTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.ScoreBasedCardPicker;
import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.cards.StandardCard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestScoreBasedCardPicker {

    private Deck deck;
    private ScoreBasedCardPicker cardpicker;

    @BeforeEach
    void setUp() {
        this.deck = new Deck("Deck", new ArrayList<>(), "");
        for (int i = 0; i < 10; i++) {
            this.deck.addCard(new StandardCard(Integer.toString(i), "", ""));
        }
        this.deck.getCard("0").setScore(-2);
        this.cardpicker = new ScoreBasedCardPicker(deck);
    }

    Card pickCardLeastScore() {
        for (int i = 0; i < 10; i++) {
            this.deck.getCard(Integer.toString(i)).setNbrPicked(1);
        }
        return this.cardpicker.pick();
    }

    @Test
    void pickLesserCard() {
        assertEquals(pickCardLeastScore().getTitle(), "0");
    }

    @Test
    void waitForCoolDown() {
        Card picked = pickCardLeastScore();
        assertFalse(this.cardpicker.getPossibleCards().contains(picked));
        assertFalse(this.cardpicker.getPossibleCards().contains(picked));
        assertFalse(this.cardpicker.getPossibleCards().contains(picked));
        assertTrue(this.cardpicker.getPossibleCards().contains(picked));
    }

    @Test
    void cardsNeverPickedCanBePicked() {
        this.deck.getCard("1").setNbrPicked(1);

        ArrayList<Card> cardsNeverPicked = new ArrayList<>();
        for (int i = 0; i < this.deck.getCards().size(); i++) {
            if (this.deck.getCard(Integer.toString(i)).getNbrPicked() == 0) {
                cardsNeverPicked.add(this.deck.getCard(Integer.toString(i)));
            }
        }
        List<Card> chosenCards = this.cardpicker.getPossibleCards();
        for (Card card : cardsNeverPicked) {
            assertTrue(chosenCards.contains(card));
        }
    }

    @Test
    void PickLessPicked() {
        for (int i = 0; i < this.deck.getCards().size(); i++) {
            this.deck.getCard(Integer.toString(i)).setNbrPicked(30);
        }
        this.cardpicker.setMaxPicked(30);

        Card lessPicked = this.deck.getCard("1");
        lessPicked.setNbrPicked(1);
        lessPicked.setScore(10);
        assertTrue(this.cardpicker.getPossibleCards().contains(lessPicked));
    }

    @Test
    void updateMaxPicked() {
        this.cardpicker.pick();
        assertEquals(1, this.cardpicker.getMaxPicked());
    }
}