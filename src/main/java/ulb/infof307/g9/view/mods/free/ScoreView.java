package ulb.infof307.g9.view.mods.free;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ulb.infof307.g9.abstracts.Observable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A view that displays the current record of the deck and the score of the game in free study mode.
 */
public class ScoreView implements Initializable, Observable<ScoreView.Listener> {

    public static final double TIME_MILLI = 1000.0;
    public static final int TIME_SECONDS = 60;
    @FXML
    private Label oldRecord;
    @FXML
    private Button quitLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label timeMinuteLabel;

    @FXML
    private Label timeSecondLabel;
    private Listener listener;
    /**
     * Handles the event for exiting the score view.
     *
     * @param event The ActionEvent that triggers the exit.
     */
    @FXML
    public void onExit(ActionEvent event) {
        listener.onExit();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String scoreString = String.valueOf(listener.getScore());

        // We have to display the time taken, so we do...
        long timeTakenMilliseconds = listener.getTime();
        double timeSeconds = timeTakenMilliseconds / TIME_MILLI;
        int minutes = (int) (timeSeconds / TIME_SECONDS);
        int secondes = (int) (timeSeconds % TIME_SECONDS);

        scoreLabel.setText(scoreString);
        timeSecondLabel.setText(secondes + " s");
        timeMinuteLabel.setText(minutes + " min");

        oldRecord.setText("Votre record : " + listener.getRecord());

    }
    /**
     * Sets the listener for this view.
     *
     * @param listener The listener to be set.
     */
    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        int getScore();

        int getRecord();

        long getTime();

        void onExit();

    }

}
