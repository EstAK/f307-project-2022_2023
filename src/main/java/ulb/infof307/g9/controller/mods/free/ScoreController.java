package ulb.infof307.g9.controller.mods.free;

import javafx.stage.Stage;
import ulb.infof307.g9.Main;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.abstracts.ScreenChanger;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.view.mods.free.ScoreView;
import java.io.IOException;

/**
 * A controller that will register a new record if the player's score is higher than the record.
 */
public class ScoreController extends Controller implements ScoreView.Listener{

    private final long timeTaken;

    private final int scoreGame;

    private final Stage stage;
    private final ScreenChanger listener;

    private final String deckName;

    private final Main main;

    private final static int SCORE_NUL = 0;

    /**
     * Constructor used to create the controller of the score
     * @param stage the window
     * @param listener the listener(ScoreView.Listener)
     * @param timeTaken the timeTaker by the user
     * @param scoreGame the score made by the user
     * @param deckName the name of the deck
     * @param main the Main
     */
    public ScoreController(Stage stage, ScreenChanger listener, long timeTaken, int scoreGame, String deckName, Main main) {
        this.stage = stage;
        this.listener = listener;
        this.timeTaken = timeTaken;
        this.scoreGame = scoreGame;
        this.deckName = deckName;
        this.main = main;
        try {
            if (scoreGame > getRecord()) {
                Database.getInstance().modifyScore(deckName, main.getUser().getUsername(), scoreGame);
            }
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur !")
                    .addContent("Une erreur est survenue lors de l'ajout du score à la base de données locale")
                    .addStackTrace(e)
                    .show();

        }
    }
    @Override
    public void show() throws IOException {
        ScoreView scoreView = new ScoreView();
        scoreView.setListener(this);
        loadFXMLView(scoreView, "score_game_screen.fxml", stage);
        stage.setTitle("Votre score !");

    }

    /**
     * Get the score
     * @return the score
     */
    @Override
    public int getScore() {
        return scoreGame;
    }

    /**
     * Get the best score
     * @return best score
     */
    @Override
    public int getRecord() {
        try {
            return Database.getInstance().getScoreFromRanking(deckName,main.getUser().getUsername());
        } catch (DatabaseException e) {
            return SCORE_NUL;   // there is no score for a certain user on the selected packet
        }
    }

    /**
     * Get time taken
     * @return time taken
     */
    @Override
    public long getTime() {
        return timeTaken;
    }

    /**
     * exit the free mod
     */
    @Override
    public void onExit(){
        listener.changeScreen(stage, Screen.NewEditDeck);
    }
}
