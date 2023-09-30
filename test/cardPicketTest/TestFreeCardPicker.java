package cardPicketTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g9.model.*;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.cards.MultiChoiceCard;
import ulb.infof307.g9.model.cards.StandardCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TestFreeCardPicker {
    private FreeCardPicker freeCardPicker;

    @BeforeEach
    void setUp() {
        Deck deck = new Deck("Deck", new ArrayList<>(), "");
        for (int i = 0; i < 10; i++) {
            deck.addCard(new StandardCard(Integer.toString(i), " ", " ")); // add standard Card
            deck.addCard(new MultiChoiceCard(Integer.toString(i+1), "questionTest", "answerTest","test", "test","test")) ;  // add multiple choice card
        }
        this.freeCardPicker = new FreeCardPicker(deck);
    }

    @Test
    void pickOnlyMultiChoiceCardsFromDeck(){
        Card cardPicked;
        do {
            cardPicked = this.freeCardPicker.pick();
            if (cardPicked != null){
                System.out.println(cardPicked.getType());
                assertNotEquals(cardPicked.getType(), CardType.NORMAL);
            }
        }
        while (cardPicked != null && freeCardPicker.haveCards());
    }

    @Test
    void cardsPickedOnlyOnce(){
        HashMap<Card,Integer> cardApparitions = new HashMap<>();
        Card cardPicked;
        do {
            cardPicked = this.freeCardPicker.pick();
            if (cardPicked != null){
                if (!cardApparitions.containsKey(cardPicked)){
                    cardApparitions.put(cardPicked,1);
                }
                else{
                    cardApparitions.put(cardPicked, cardApparitions.get(cardPicked) + 1);
                }
            }
        }
        while (cardPicked != null && freeCardPicker.haveCards());
        Set<Card> setCards = cardApparitions.keySet();
        for (Card card : setCards){
            assertEquals(cardApparitions.get(card),1);
        }
    }

    @Test
    void increaseCorrectlyScore(){
        for (int i = 0; i<3; i++){
            freeCardPicker.increaseNumberOfGoodAnswers();
        }
        assertEquals(freeCardPicker.getNumberOfGoodAnswers(), 3);
    }

    @Test
    void getNameOfDeck_returnsDeckName() {
        String expectedDeckName = "Deck";
        assertEquals(expectedDeckName, freeCardPicker.getNameOfDeck());
    }

    @Test
    void getTime_returnsTimeTaken() {
        // Time taken is set to 1 in the setup
        long expectedTime = 1;
        assertEquals(expectedTime, freeCardPicker.getTime());
    }
}