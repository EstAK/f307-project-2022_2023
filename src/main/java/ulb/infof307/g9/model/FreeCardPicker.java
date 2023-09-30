package ulb.infof307.g9.model;
import ulb.infof307.g9.model.cards.Card;

import java.util.ArrayList;

/**
 * Free Card picker is the class used to pick the next card shown when studying in free mode.
 * The main goal of the algorithm is to show all cards only once.
 */
public class FreeCardPicker extends  CardPicker {

    private int indexCurrentCard;
    private final long timerBegin;
    private long timeTaken;
    private int numberOfGoodAnswers;
    private final String deckName;
    private final static int TIME_FACTOR = 1000;


    /**
     * The constructor requires a full deck but only keeps the multi choice cards
     * to construct his deck.
     */
    public FreeCardPicker(Deck deck) {
        allCards = new ArrayList<>();
        for (Card card : deck.getCards()) {
            if (card.getType().toString().contains("MULTI")){
                allCards.add(card);
            }
        }
        indexCurrentCard = 0;
        timeTaken = 1;
        timerBegin = System.currentTimeMillis();
        deckName = deck.getName();
    }

    /**
     * A method that send the next card requested in the list using the indexCurrentCard.
     */
    public Card pick(){
        if (indexCurrentCard >= allCards.size()){
            long timerEnd = System.currentTimeMillis();
            timeTaken =  timerEnd - timerBegin;
            return null;
        }
       Card cardPicked = allCards.get(indexCurrentCard);
       indexCurrentCard += 1;
       return cardPicked;
    }

    public String getNameOfDeck(){
        return deckName;
    }

    /**
     * Calculate the player's score (for the free mod) depending on the number of good answers and the time taken.
     */
    public int getScore(){
        if (timeTaken == 0){
            return 0;
        }
        return (int)(numberOfGoodAnswers * TIME_FACTOR/(timeTaken / TIME_FACTOR)) ;
    }

    public void increaseNumberOfGoodAnswers(){
        numberOfGoodAnswers += 1;
    }

    public int getNumberOfGoodAnswers(){
        return numberOfGoodAnswers;
    }

    public long getTime(){
        return timeTaken;
    }

    public boolean haveCards(){
        return allCards.size() > 0;
    }
}
