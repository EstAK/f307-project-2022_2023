package ulb.infof307.g9.controller.card;

import javafx.stage.Stage;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.abstracts.ScreenChanger;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.CardType;
import ulb.infof307.g9.model.cards.StandardCard;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.view.card.EditCardView;
import ulb.infof307.g9.model.Modular;

import java.io.IOException;

/**
 * Controller Class that creates a new card and modifies an existing card.
 *
 */
public class EditCardController extends Controller implements EditCardView.Listener {

    private final String deckName;
    private final Stage stage;
    private final ScreenChanger listener;
    private final Card card;
    private EditCardView editCardView;

    /**
     * Constructor used to modify a card
     * @param stage the window
     * @param listener the listener(EditCardView.Listener)
     * @param deckName the selected deck where a new card will be added
     * @param card the card to modify
     */
    public EditCardController(Stage stage, ScreenChanger listener, String deckName, Card card) {
        this.stage = stage;
        this.listener = listener;
        this.deckName = deckName;
        this.card = card;
    }

    /**
     * Constructor used with the purpose to create a card and add it to the deck
     *
     * @param stage the window
     * @param listener the listener(a interface)
     * @param deckName the selected deck where a new card will be added
     */
    public EditCardController(Stage stage, ScreenChanger listener, String deckName) {
        this(stage, listener, deckName, null);
    }


    @Override
    public void show() throws IOException {
        this.editCardView = new EditCardView();
        editCardView.setListener(this);
        loadFXMLView(editCardView, "edit_card.fxml", stage);
        if (card != null)
            editCardView.setCard(card);
        stage.setTitle("Modification de carte");
    }

    /**
     * Method to register a new card to the database and put in the deck if fromCardDeckEditor is true
     * @param title the name of the card
     * @param question the question
     * @param answer the answer of the question
     */
    @Override
    public void registerCard(String title, String question, String answer) {
        if (title.isEmpty() || question.isEmpty() || answer.isEmpty()) {
            new AlertBuilder()
                    .warning()
                    .addHeader("Attention !")
                    .addContent("Veuillez remplir tous les champs")
                    .show();
            return;
        }

        Card newCard = new StandardCard(title, question, answer, CardType.NORMAL);
        if (editCardView.isModularChecked()) {
            if (!Modular.isFullyModular(question, answer)) {
                new AlertBuilder()
                        .warning()
                        .addHeader("Attention !")
                        .addContent("La carte modulaire n'a pas été bien formatée");
                return;
            }
            newCard.setType(CardType.MODULAR_NORMAL);
        }


        try {
            if (card != null) { // if we are modifying an existing card -> delete it (overwriting)
                Database.getInstance().deleteCardFromDeck(deckName,card.getTitle());
                Database.getInstance().deleteCard(card.getTitle());
            }
            Database.getInstance().insertNewCard(newCard);
            Database.getInstance().insertCardIntoDeck(title, deckName);
            listener.changeScreenToEditDeck(stage, Screen.EditContentDeck, deckName);
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .warning()
                    .addHeader("Attention !")
                    .addContent(e.getMessage())
                    .addStackTrace(e)
                    .show();
        }
    }
}
