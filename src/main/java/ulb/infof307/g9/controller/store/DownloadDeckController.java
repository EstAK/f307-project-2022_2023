package ulb.infof307.g9.controller.store;

import javafx.stage.Stage;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.abstracts.ScreenChanger;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.server.Client;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.view.store.DownloadDeckView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the download deck view. It is responsible for the communication between the view and the server
 */
public class DownloadDeckController extends Controller implements DownloadDeckView.Listener {

    private final Stage stage;
    private final ScreenChanger listener;
    private final Client client;
    private final Map<String, Deck> deckHashMap = new HashMap<>();
    private DownloadDeckView downloadView;

    /**
     * Constructor used to create DownloadDeckController
     * @param stage the window
     * @param listener the listener
     * @throws IOException the io exception
     */
    public DownloadDeckController(Stage stage, ScreenChanger listener) throws IOException {
        client = new Client(new Client.OnEventListener() {
            /**
             * create a pop-up when error occurs (AlertBuilder)
             */
            @Override
            public void onFailEvent() {
                runAsMainThread(() -> new AlertBuilder()
                        .error()
                        .addHeader("Erreur")
                        .addContent("Les paquets n'ont pas pu être téléchargés")
                        .show());
            }

            /**
             * Receive decks sent by the server
             * @param decks list of decks sent by the server
             */
            @Override
            public void onSendAllDecksEvent(List<Deck> decks) {
                for (Deck deck : decks)  // if the user wants multiple decks from the store it will be better
                    deckHashMap.put(deck.getName(), deck);
                downloadView.updateDeckList(new ArrayList<>(deckHashMap.keySet()));
            }

        });
        this.stage = stage;
        this.listener = listener;
    }

    @Override
    public void show() throws IOException {
        downloadView = new DownloadDeckView();
        downloadView.setListener(this);
        loadFXMLView(downloadView, "download_deck.fxml", stage);
        client.getAllDecks();
    }

    /**
     * Return to the MainMenu View
     */
    @Override
    public void goBack() {
        listener.changeScreen(stage, Screen.MainMenu);
    }

    /**
     * Method use to download a deck
     * @param deckName the name of the deck to download
     */
    @Override
    public void downloadDeck(String deckName) {
        try {
            Database.getInstance().insertNewDeck(deckHashMap.get(deckName));
            new AlertBuilder()
                    .addHeader("Succès")
                    .addContent("Le paquet a bien été téléchargé")
                    .show();
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .addHeader("Erreur")
                    .addContent("Vous possédez déjà ce paquet")
                    .show();
        }
    }

    /**
     * Method use to get the Card titles
     * @param deckName the name of the deck
     * @return the titles of the cards
     */
    @Override
    public List<String> getCardTitles(String deckName) {
        List<Card> cards = deckHashMap.get(deckName).getCards();

        // mapping the cards to their titles
        return cards.stream()
                .map(Card::getTitle)
                .toList();
    }

    /**
     * Get the author of the deck
     * @param deckName the name of the deck
     * @return the author of the deck
     */
    @Override
    public String getAuthorFromDeck(String deckName) {
        try {
            return Database.getInstance().getAuthorFromDeck(deckName);
        } catch (DatabaseException e) {
            return "no author";
        }
    }

    /**
     * Get the description of the deck
     * @param deckName the name of the deck
     * @return the description of the deck
     */
    @Override
    public String getDeckDescription(String deckName){
        // getting the description from the decks sent by the server
        return deckHashMap.get(deckName).getDescription();
    }

}
