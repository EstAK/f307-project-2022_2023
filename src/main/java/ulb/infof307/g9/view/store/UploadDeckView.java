package ulb.infof307.g9.view.store;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import ulb.infof307.g9.abstracts.Observable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The UploadDeckView class represents the view for uploading a deck.
 * It implements the Initializable and Observable interfaces.
 */
public class UploadDeckView implements Initializable, Observable<UploadDeckView.Listener> {

    @FXML
    private TextField description;
    @FXML
    private ListView<String> listCard;
    @FXML
    private ListView<String> listDeck;
    @FXML
    private Label scoreLabel;
    private Listener listener;

    /**
     * Method used to init the list views of the decks and the cards.
     *
     * @param url            The location of the FXML file.
     * @param resourceBundle The resource bundle used to localize the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listDeck.getItems().clear();
        listDeck.getItems().addAll(listener.getDeckNames());
        listDeck.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            String currentDeckName = listDeck.getSelectionModel().getSelectedItem();
            if (currentDeckName != null) {
                List<String> currentCards = listener.getCards(currentDeckName);
                listCard.getItems().clear();
                listCard.getItems().addAll(currentCards);
                scoreLabel.setText(listener.getScore(currentDeckName) + " points");
            }
        });
    }
    /**
     * Handles the go back button click event.
     *
     * @param event The action event object.
     */
    @FXML
    private void goBack(ActionEvent event) {
        listener.movingBack();
    }
    /**
     * Handles the send deck button click event.
     *
     * @param event The action event object.
     */
    @FXML
    private void sendDeck(ActionEvent event) {
        String currentDeckName = listDeck.getSelectionModel().getSelectedItem();
        listener.sendSelectedDeck(currentDeckName, description.getText());
    }
    /**
     * Handles the send score button click event.
     *
     * @param e The action event object.
     */
    @FXML
    private void sendScoreClicked(ActionEvent e) {
        String currentDeckName = listDeck.getSelectionModel().getSelectedItem();
        if (currentDeckName != null) {
            listener.onSendScore(currentDeckName);
        }
    }
    /**
     * Sets the listener for the UploadDeckView.
     *
     * @param listener The listener to be set.
     */
    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {

        /**
         * Method used to upload a specific deck based on its name.
         *
         * @param deckName the name of the deck
         */
        void sendSelectedDeck(String deckName, String description);

        void movingBack();

        /**
         * Returns a list of cards from the local database
         * @param deckName from whom to get the cards from
         * @return a list of cards or null if there was an error
         */
        List<String> getCards(String deckName);

        /**
         * Returns a list of deck names from the local database
         * @return a list of deck names or null there was an error
         */
        List<String> getDeckNames();

        void onSendScore(String deckName);

        int getScore(String currentDeckName);
    }
}
