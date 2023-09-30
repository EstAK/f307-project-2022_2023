package ulb.infof307.g9.controller.store;

import javafx.stage.Stage;
import ulb.infof307.g9.Main;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.RankRow;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.server.Client;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.view.store.UploadDeckView;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the UploadDeck view. this controller manages the uploading of deck
 */
public class UploadDeckController extends Controller implements UploadDeckView.Listener {

    private final Stage stage;
    private final Main main;
    private final Client client;

    private final static int SCORE_NUL = 0;

    /**
     * Constructor used to create UploadDeckController
     * @param stage the window
     * @param main the Main
     * @throws IOException IOException
     */
    public UploadDeckController(Stage stage, Main main) throws IOException {
        this.client = new Client(new Client.OnEventListener() {
            /**
             * create a pop-up when error occurs (AlertBuilder)
             */
            @Override
            public void onFailEvent() {
                runAsMainThread(() -> new AlertBuilder()
                        .error()
                        .addHeader("Erreur")
                        .addContent("La demande serveur a échoué")
                        .show());
            }
            /**
             * create a pop-up when no error occurs (AlertBuilder)
             */
            @Override
            public void onOkEvent() {
                runAsMainThread(() -> new AlertBuilder()
                        .addHeader("Succès")
                        .addContent("La demande serveur a réussi")
                        .show());
            }

        });
        this.stage = stage;
        this.main = main;
    }

    @Override
    public void show() throws IOException {
        UploadDeckView uploadView = new UploadDeckView();
        uploadView.setListener(this);
        loadFXMLView(uploadView, "upload_deck.fxml", stage);
    }

    /**
     * Send the deck selected by the user
     * @param deckName the name of the deck
     * @param description the description of the deck
     */
    @Override
    public void sendSelectedDeck(String deckName, String description) {
        if (description.isEmpty()) {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur")
                    .addContent("Veuillez entrer une description")
                    .show();
            return;
        }
        try {
            Deck deck = Database.getInstance().getDeck(deckName);
            deck.setDescription(description);
            client.exportDeck(deck);
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur !")
                    .addContent("Une erreur est survenue lors de la récupération du paquet depuis la base de données locale")
                    .show();
        }
    }

    /**
     * Return to the MainMenu View
     */
    @Override
    public void movingBack() {
        main.changeScreen(stage, Screen.MainMenu);
    }

    /**
     * Returns a list of cards from the local database
     * @param deckName from whom to get the cards from
     * @return a list of cards or null if there was an error
     */
    @Override
    public List<String> getCards(String deckName) {
        try {
            List<Card> cards = Database.getInstance().getAllCardsFromDeck(deckName);
            // mapping the list of cards to a list of card titles
            return cards.stream()
                    .map(Card::getTitle)
                    .toList();
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur !")
                    .addContent("Une erreur est survenue lors de la récupération du paquet depuis la base de données locale")
                    .show();
            return null;
        }

    }
    /**
     * Returns a list of deck names from the local database
     * @return a list of deck names or null there was an error
     */
    @Override
    public List<String> getDeckNames() {
        try {
            List<Deck> decks = Database.getInstance().getAllDecks();

            // mapping the list of decks to a list of deck names
            return decks.stream()
                    .map(Deck::getName)
                    .toList();
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur !")
                    .addContent("Une erreur est survenue lors des noms de paquet depuis la base de données locale")
                    .show();
            return null;
        }
    }

    /**
     * Send the score of the user to the server and then shows an alert to the user to confirm the score has been sent
     *
     * @param deckName the name of the deck
     */
    @Override
    public void onSendScore(String deckName) {
        int score = getScore(deckName);
        if (score > 0) {
            client.sendScore(deckName, getScore(deckName), main.getUser());
        } else {
            new AlertBuilder()
                    .warning()
                    .addHeader("Attention !")
                    .addContent("Vous essayez d'envoyer un score nul ou non existant")
                    .show();
        }
    }

    /**
     * Get the score
     * @param currentDeckName the name of the current deck
     * @return the score
     */
    @Override
    public int getScore(String currentDeckName) {
        try {
            return Database.getInstance().getScoreFromRanking(currentDeckName, main.getUser().getUsername());
        } catch (DatabaseException e) {
            return SCORE_NUL;
        }
    }
}
