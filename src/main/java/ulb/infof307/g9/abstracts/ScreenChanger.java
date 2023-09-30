package ulb.infof307.g9.abstracts;

import javafx.stage.Stage;
import ulb.infof307.g9.model.CardType;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.FreeCardPicker;
import ulb.infof307.g9.model.ScoreBasedCardPicker;

/**
 * The interface Screen changer.
 */
public interface ScreenChanger {

    /**
     * Change screen.
     *
     * @param oldStage the old stage
     * @param screen   the screen
     */
    void changeScreen(Stage oldStage, Screen screen);

    /**
     * change screen to the view of modify card
     * @param oldStage the old stage
     * @param card the current card
     * @param deckName the name of the deck
     * @param type type of card
     */
    void changeScreenToModifyCard(Stage oldStage, Card card, String deckName, CardType type);


    /**
     * Change screen to the view of select card type
     *
     * @param oldStage the old stage
     * @param deckName the name of the deck
     */
    void changeScreenToSelectCardType(Stage oldStage, String deckName);

    /**
     * Change screen to the view of the corresponding study cardView
     * @param oldStage the old stage
     * @param deckName the name of the deck
     * @param type type of card
     */
    void changeScreenToCorrespondingCardType(Stage oldStage, String deckName, CardType type);


    /**
     * Change screen to the view of editing deck
     * @param oldStage  the old stage
     * @param screen the screen
     * @param deckName the name of the deck
     */
    void changeScreenToEditDeck(Stage oldStage, Screen screen, String deckName);

    /**
     * Change screen to the view of study
     * @param oldStage the old stage
     * @param scoreBasedCardPicker CardPicker
     */
    void changeScreenToStudy(Stage oldStage, ScoreBasedCardPicker scoreBasedCardPicker);

    /**
     * Change screen to the view of freemode study
     * @param oldStage the old stage
     * @param freeCardPicker CardPicker for free mode
     */
    void changeScreenToFreeMod(Stage oldStage, FreeCardPicker freeCardPicker);

    /**
     * Change screen to the view of scoreboard
     * @param oldStage the old stage
     * @param score the score made by the user
     * @param time the time take by the user
     * @param deckName the name of the deck
     */
    void changeScreenToScoreBoard(Stage oldStage, int score, long time, String deckName);
}
