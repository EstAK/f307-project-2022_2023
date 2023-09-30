package ulb.infof307.g9.controller.deck;

import javafx.stage.Stage;
import ulb.infof307.g9.Main;
import ulb.infof307.g9.abstracts.CardVisitor;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.model.CardType;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.cards.HoleCard;
import ulb.infof307.g9.model.cards.MultiChoiceCard;
import ulb.infof307.g9.model.cards.StandardCard;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.view.deck.EditContentDeckView;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for the menu of the content of a deck
 */

public class EditContentDeckController extends Controller implements EditContentDeckView.Listener, CardVisitor {
    private final String deckName;

    private final Main main;

    private final Stage stage;

    /**
     * Constructor used to edit content deck
     *
     * @param stage    the window
     * @param main     the Main
     * @param deckName the name of the deck
     */
    public EditContentDeckController(Stage stage, Main main, String deckName) {
        this.stage = stage;
        this.main = main;

        this.deckName = deckName;
    }

    /**
     * method associated to the addCardButton, opens the card creation menu and adds it to the database
     */
    @Override
    public void addCard() {
        main.changeScreenToSelectCardType(stage, deckName);
    }

    /**
     * method associated to the modifyCardButton, opens the card modify menu and modify it to the database
     */
    @Override
    public void modifyCard(String title) {

        try {

            //We retrieve the card that has been selected for modification
            Card selectedCard = Database.getInstance().getCard(title);
            //We modify the card and add it the to db/deck*
            selectedCard.accept(this);
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur!")
                    .addContent(String.format("La carte '%s' n'a pas pu être modifiée", title))
                    .show();
        }
    }

    /**
     * method associated to the goBackButton, changes the current menu to the deck menu
     */
    @Override
    public void goBack() {
        main.changeScreen(stage, Screen.NewEditDeck);
    }

    /**
     * Method used to return Card names
     *
     * @return the list of card name and if DatabaseException return null
     */
    @Override
    public List<String> getCardNames() {
        try {
            List<Card> cards = Database.getInstance().getAllCardsFromDeck(deckName);
            // we get the title of each card
            return cards.stream()
                    .map(Card::getTitle)
                    .collect(Collectors.toList());
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .addHeader("Erreur !")
                    .error()
                    .addContent("Les cartes n'ont pas pu être récupérées")
                    .show();
            return null;
        }
    }

    /**
     * method associated to the removeCardButton, removes a card from the deck gui and database
     *
     * @param cardToDelete the name of the card to delete
     */

    @Override
    public void removeCard(String cardToDelete) {
        try {
            Database.getInstance().deleteCardFromDeck(deckName, cardToDelete);
            Database.getInstance().deleteCard(cardToDelete);
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .addHeader("Erreur !")
                    .error()
                    .addContent(String.format("La carte '%s' n'a pas pu être supprimée", cardToDelete))
                    .show();

        }
    }

    @Override
    public void show() throws IOException {
        EditContentDeckView editContentDeckView = new EditContentDeckView();
        editContentDeckView.setListener(this);
        loadFXMLView(editContentDeckView, "edit_content_deck.fxml", stage);
        stage.setTitle(deckName);
    }

    /**
     * Change to modify hole card
     *
     * @param holeCard the hole card
     */
    @Override
    public void visit(HoleCard holeCard) {
        main.changeScreenToModifyCard(stage, holeCard, deckName, CardType.HOLE);
    }

    /**
     * Change to modify multiChoiceCard card
     *
     * @param multiChoiceCard the multiChoiceCard card
     */
    @Override
    public void visit(MultiChoiceCard multiChoiceCard) {
        main.changeScreenToModifyCard(stage, multiChoiceCard, deckName, CardType.MULTI_CHOICE);
    }

    /**
     * Change to modify standardCard card
     *
     * @param standardCard the standardCard card
     */
    @Override
    public void visit(StandardCard standardCard) {
        main.changeScreenToModifyCard(stage, standardCard, deckName, CardType.NORMAL);
    }

}
