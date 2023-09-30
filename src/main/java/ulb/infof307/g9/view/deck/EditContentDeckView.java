package ulb.infof307.g9.view.deck;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import ulb.infof307.g9.abstracts.Observable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
/**
 * This class represents a view for editing the content of a deck.
 * It implements the Initializable interface and the Observable interface with listeners of type EditContentDeckView.Listener.
 */
public class EditContentDeckView implements Initializable, Observable<EditContentDeckView.Listener> {
    @FXML
    private Button addCardButton;

    @FXML
    private Button backButton;

    @FXML
    private Button modifyCardButton;

    @FXML
    private ListView<String> listCards;

    @FXML
    private Button removeCardButton;
    private Listener listener;
    /**
     * Adds a card when the corresponding event occurs.
     *
     * @param event The MouseEvent that triggers the addition of a card.
     */
    @FXML
    void addCard(ActionEvent event) {
        listener.addCard();
    }
    /**
     * Change screen when the corresponding event occurs.
     *
     * @param event The MouseEvent that triggers the navigation back.
     */
    @FXML
    void goBack(ActionEvent event) {
        listener.goBack();
    }

    /**
     * Modifies a card when the corresponding event occurs.
     */
    @FXML
    void modifyCard(ActionEvent event) {
        if (!listCards.getSelectionModel().isEmpty()) {
            String title = listCards.getSelectionModel().getSelectedItem();
            int index = listCards.getSelectionModel().getSelectedIndex();
            listCards.getItems().remove(index);
            listener.modifyCard(title);
        }
    }
    /**
     * Removes a card from the deck when the corresponding event occurs.
     *
     * @param event The MouseEvent that triggers the removal of a card.
     */
    @FXML
    void removeCardFromDeck(ActionEvent event) {
        String cardToDelete = listCards.getSelectionModel().getSelectedItem();
        if (cardToDelete != null) {
            // removing the card from the view
            listCards.getItems().remove(listCards.getSelectionModel().getSelectedIndex());
            listener.removeCard(cardToDelete);
        }
    }
    /**
     * Initializes the view.
     *
     * @param url            The URL location of the FXML file.
     * @param resourceBundle The ResourceBundle associated with the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> listOfCardNames = listener.getCardNames();


        listCards.getItems().clear();
        listCards.getItems().addAll(listOfCardNames);

        // setting a listener for when a cell in the decks list view is clicked
        listCards.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            String currentCardName = listCards.getSelectionModel().getSelectedItem();
            //listener.updateCurrentDeckController(currentDeckName);
            if (currentCardName != null) {

                removeCardButton.setDisable(false);
                modifyCardButton.setDisable(false);
                // filling the cards visualisation listView

            }
        });

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

    /**
     * The listener interface for handling deck content editing.
     */
    public interface Listener {
        void addCard();
        void modifyCard(String title);

        void goBack();

        List<String> getCardNames();
        void removeCard(String cardToDelete);
    }

}
