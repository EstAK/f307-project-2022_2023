package ulb.infof307.g9.controller.card;

import javafx.stage.Stage;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.abstracts.ScreenChanger;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.model.CardType;
import ulb.infof307.g9.model.Modular;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.cards.MultiChoiceCard;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.view.card.EditMultiChoiceCardView;

import java.io.IOException;

/**
 * Controller Class that creates a new MultiChoice card and modifies an existing MultiChoice card.
 */
public class EditMultiChoiceCardController extends Controller implements EditMultiChoiceCardView.Listener {

    private final Stage stage;
    private final ScreenChanger screenChanger;
    private final String deckName;
    private final Card card;
    private EditMultiChoiceCardView editMultiChoiceCardView;


    /**
     * Instantiates a new Card choice modification controller.
     * @param stage the stage
     * @param listener the listener (EditMultiChoiceCardView.Listener)
     * @param deckName the name of the deck
     * @param card the card to modify
     */
    public EditMultiChoiceCardController(Stage stage, ScreenChanger listener, String deckName, Card card) {
        this.stage = stage;
        this.screenChanger = listener;
        this.card = card;
        this.deckName = deckName;
    }

    /**
     * Instantiates a new Card choice modification controller.
     *
     * @param stage    the stage
     * @param listener the listener
     * @param deckName the name of the deck
     */
    public EditMultiChoiceCardController(Stage stage, ScreenChanger listener, String deckName) {
        this(stage, listener, deckName, null);
    }

    /**
     * Method to register a new MultiChoiceCard to the database and put in the deck if fromCardDeckEditor is true
     *
     * @param title    the title of the MultiChoiceCard
     * @param question the question
     * @param answer   the answer
     * @param fake1    a fake answer
     * @param fake2    a fake answer
     * @param fake3    a fake answer
     * @param isModular indicate if this card is modular
     */
    @Override
    public void registerCard(String title, String question, String answer, String fake1, String fake2, String fake3, boolean isModular) {
        MultiChoiceCard newCard = new MultiChoiceCard(title, question, answer, fake1, fake2, fake3);

        if (isModular) {
            if (!Modular.isFullyModular(question, answer)) {
                new AlertBuilder()
                        .warning()
                        .addHeader("Attention !")
                        .addContent("La carte modulaire n'a pas été bien formatée")
                        .show();
                return;
            }
            newCard.setType(CardType.MODULAR_MULTI);
            newCard.setFake1("placeHolder");
            newCard.setFake2("placeHolder");
            newCard.setFake3("placeHolder");
        }

        try {
            if (card != null) { // if we are modifying an existing card -> delete it (overwriting)
                Database.getInstance().deleteCardFromDeck(deckName, card.getTitle());
                Database.getInstance().deleteCard(card.getTitle());
            }
            Database.getInstance().insertNewCard(newCard);
            Database.getInstance().insertCardIntoDeck(title, deckName);
            screenChanger.changeScreenToEditDeck(stage, Screen.EditContentDeck, deckName);
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .warning()
                    .addHeader("Attention !")
                    .addContent(e.getMessage())
                    .addStackTrace(e)
                    .show();
        }
    }

    public void show() throws IOException {
        this.editMultiChoiceCardView = new EditMultiChoiceCardView();
        editMultiChoiceCardView.setListener(this);
        loadFXMLView(editMultiChoiceCardView, "edit_multi_choice_card.fxml", stage);
        if (card != null) {
            editMultiChoiceCardView.setCard(card);
        }
        stage.setTitle("Modification de la carte à choix multiples");
    }
}
