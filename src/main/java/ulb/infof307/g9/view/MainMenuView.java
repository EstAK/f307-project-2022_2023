package ulb.infof307.g9.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ulb.infof307.g9.abstracts.Observable;

/**
 * The MainMenuView class represents the view for the main menu.
 * It implements the Observable interface.
 */
public class MainMenuView implements Observable<MainMenuView.Listener> {

    private Listener listener;
    /**
     * Handles the event when the password change button is clicked.
     *
     * @param event The mouse event object.
     */
    @FXML
    void changePassword(ActionEvent event) {
        listener.changePassword();
    }
    /**
     * Handles the event when the disconnect button is clicked.
     *
     * @param event The mouse event object.
     */
    @FXML
    void disconnect(ActionEvent event) {
        listener.disconnect();
    }
    /**
     * Handles the event when the deck modification button is clicked.
     *
     * @param event The mouse event object.
     */
    @FXML
    void goToDeckModification(ActionEvent event) {
        listener.goToDeckModification();
    }
    /**
     * Handles the event when the upload deck button is clicked.
     *
     * @param event The action event object.
     */
    @FXML
    void uploadPacketButton(ActionEvent event) {
        listener.uploadDeck();
    }
    /**
     * Handles the event when the download deck button is clicked.
     *
     * @param event The action event object.
     */
    @FXML
    void downloadButtonClicked(ActionEvent event) {
        listener.downloadDeck();
    }
    /**
     * Handles the event when the leaderboard button is clicked.
     *
     * @param event The action event object.
     */
    @FXML
    void leaderBoardButtonClicked(ActionEvent event) {
        listener.leaderBoard();
    }
    /**
     * Sets the listener for the MainMenuView.
     *
     * @param listener The listener to be set.
     */
    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void changePassword();

        void disconnect();

        void goToDeckModification();

        void uploadDeck();

        void downloadDeck();

        void leaderBoard();
    }
}
