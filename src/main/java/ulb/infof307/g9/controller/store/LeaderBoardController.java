package ulb.infof307.g9.controller.store;

import javafx.stage.Stage;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.abstracts.ScreenChanger;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.RankRow;
import ulb.infof307.g9.server.Client;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.view.store.LeaderBoardView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the LeaderBoard view. this controller manages the leaderBoard
 * */
public class LeaderBoardController extends Controller implements LeaderBoardView.Listener{

    private final Stage stage;
    private final ScreenChanger listener;

    private final Client client;

    private final Map<String, Deck> deckHashMap = new HashMap<>();

    private LeaderBoardView leaderBoardView;

    /**
     * Constructor used to create LeaderBoardController
     * @param stage the window
     * @param listener the listener (LeaderBoardView.Listener)
     * @throws IOException the IOException
     */
    public LeaderBoardController(Stage stage, ScreenChanger listener) throws IOException{
        this.leaderBoardView = new LeaderBoardView();

        this.stage = stage;
        this.listener = listener;

        client = new Client(new Client.OnEventListener() {
            /**
             * create a pop-up when error occurs (AlertBuilder)
             */
            @Override
            public void onFailEvent() {
                runAsMainThread(() -> new AlertBuilder()
                        .error()
                        .addHeader("Erreur")
                        .addContent("Le classement n'a pas pu être téléchargé")
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
                leaderBoardView.fillListDeck(new ArrayList<>(deckHashMap.keySet()));
            }

            /**
             * Get the rank
             * @param leaderboard All ranking
             */
            @Override
            public void onGetRankingEvent(List<RankRow> leaderboard) {
                runAsMainThread(() -> leaderBoardView.setLeaderBoard(leaderboard));
            }
        });

    }

    @Override
    public void show() throws IOException {
        leaderBoardView = new LeaderBoardView();
        leaderBoardView.setListener(this);
        loadFXMLView(leaderBoardView, "leaderboard_menu.fxml", stage);
        client.getAllDecks();
    }
    /**
     * Return to the MainMenu View
     */
    @Override
    public void onGoBack() {
        listener.changeScreen(stage, Screen.MainMenu);
    }

    /**
     * Get the rank of the deck
     * @param deckName the name of the deck
     */
    @Override
    public void getRankDeck(String deckName) {
        client.getLeaderBoard(deckName);
    }
}
