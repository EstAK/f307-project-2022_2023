package ulb.infof307.g9.view.deck;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import ulb.infof307.g9.abstracts.Observable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
/**
 * This class represents a view for editing a deck.
 * It implements the Initializable interface and the Observable interface with listeners of type EditDeckView.Listener.
 */
public class EditDeckView implements Initializable, Observable<EditDeckView.Listener> {

    public static final int DIFFICULTY = 2;
    @FXML
    private Button addTagButton;

    @FXML
    private Button backButton;

    @FXML
    private ListView<String> listDeck;

    @FXML
    private ListView<String> listTags;

    @FXML
    private Button newDeckButton;

    @FXML
    private TextField newTagTextField;

    @FXML
    private Button removeDeckButton;

    @FXML
    private Button removeTagButton;

    @FXML
    private Button searchTagButton;

    @FXML
    private Button studyButton;

    @FXML
    private TextField tagSearch;

    @FXML
    private Button unselectDeckButton;
    private Listener listener;

    /**
     * Adds a new deck when the corresponding event occurs.
     *
     * @param event The MouseEvent that triggers the addition of a new deck.
     */
    @FXML
    void addDeck(ActionEvent event) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Ajout d'un nouveau deck");
        inputDialog.setHeaderText("Entrez le titre du deck à ajouter:");
        inputDialog.setContentText("Titre:");

        inputDialog.showAndWait().ifPresent(response -> listener.addDeck(response));
    }
    /**
     * Adds a new tag when the corresponding event occurs.
     *
     * @param event The MouseEvent that triggers the addition of a new tag.
     */
    @FXML
    void addNewTag(ActionEvent event) {
        String newTag = newTagTextField.getText();
        if (!newTag.isBlank()) {
            listener.addNewTag(newTag);
            listTags.getItems().add(newTag);
        }
    }
    /**
     * Placeholder method for adding a new tag from the list.
     *
     * @param event The ActionEvent that triggers the addition of a new tag.
     */
    @FXML
    void addNewTagFromList(ActionEvent event) {
        // implement later for ease of use
    }
    /**
     * Handles the click event for exporting a deck.
     *
     * @param event The ActionEvent that triggers the export of a deck.
     */
    @FXML
    void exportClick(ActionEvent event) {
        listener.exportDeck(listDeck.getSelectionModel().getSelectedItem());
    }
    /**
     * Change screen to the previous menu.
     *
     * @param event The MouseEvent that triggers the navigation back.
     */
    @FXML
    void goBack(ActionEvent event) {
        listener.goBack();
    }
    /**
     * Handles the event for going to the free mode for a deck.
     *
     * @param event The MouseEvent that triggers the transition to free mode.
     */
    @FXML
    void goFreeMod(ActionEvent event) {
        String deckName = listDeck.getSelectionModel().getSelectedItem();
        if (deckName == null) {
            return;
        }
        listener.goFreeMod(deckName, DIFFICULTY);
    }
    /**
     * Adds a deck name to the list of decks.
     *
     * @param deckName The name of the deck to add.
     */
    public void addToListDeck(String deckName) {
        listDeck.getItems().add(deckName);
    }
    /**
     * Adds multiple deck names to the list of decks.
     *
     * @param filteredDeckNames The list of deck names to add.
     */
    public void addToListDeck(List<String> filteredDeckNames) {
        listDeck.getItems().clear();
        listDeck.getItems().addAll(filteredDeckNames);
    }

    /**
     * Handles the event for starting the study mode.
     *
     * @param event The MouseEvent that triggers the study mode action.
     */
    @FXML
    void goStudy(ActionEvent event) {
        String deckName = listDeck.getSelectionModel().getSelectedItem();
        if (deckName == null) {
            return;
        }
        listener.goStudy(deckName, DIFFICULTY);
    }
    /**
     * Handles the event for importing a deck.
     *
     * @param event The ActionEvent that triggers the import deck action.
     */
    @FXML
    void importClick(ActionEvent event) {
        listener.importDeck();
    }
    /**
     * Handles the event for unselecting a deck.
     *
     * @param event The MouseEvent that triggers the unselect deck action.
     */
    @FXML
    void onUnselectClick(ActionEvent event) {
        clearSelectionFromALl();
        unselectDeckButton.setDisable(true);
        listener.unselectDeck();
    }
    /**
     * Handles the event for removing a deck.
     *
     * @param event The MouseEvent that triggers the remove deck action.
     */
    @FXML
    void removeDeck(ActionEvent event) {
        String deckToRemove = listener.removeDeck();
        if (deckToRemove != null) {
            clearSelectionFromALl();
            listDeck.getItems().remove(deckToRemove);
        }
    }
    /**
     * Handles the event for removing a tag.
     *
     * @param event The MouseEvent that triggers the remove tag action.
     */
    @FXML
    void removeTag(ActionEvent event) {
        String tagToRemove = listTags.getSelectionModel().getSelectedItem();
        if (tagToRemove != null) {

            // setting the tags on the view
            listTags.getItems().remove(tagToRemove);

            listener.removeTag(tagToRemove);
        }
    }
    /**
     * Handles the event for searching a tag.
     *
     * @param event The MouseEvent that triggers the search tag action.
     */
    @FXML
    void searchTag(ActionEvent event) {
        String tagToSearch = tagSearch.getText();
        clearSelectionFromALl();
        unselectDeckButton.setDisable(true);
        listener.searchTag(tagToSearch);
    }

    /**
     * unselect the currentDeck, card and tag then clears cards and tags
     */
    private void clearSelectionFromALl() {
        listDeck.getSelectionModel().clearSelection();
        listTags.getSelectionModel().clearSelection();

        listTags.getItems().clear();

        newDeckButton.setDisable(false);
        removeDeckButton.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listDeck.getItems().clear();
        listDeck.getItems().addAll(listener.getDeckNames());

        // setting a listener for when a cell in the decks list view is clicked
        listDeck.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            String currentDeckName = listDeck.getSelectionModel().getSelectedItem();
            listener.updateCurrentDeckController(currentDeckName);
            if (currentDeckName != null) {
                newDeckButton.setDisable(true);
                unselectDeckButton.setDisable(false);
                removeDeckButton.setDisable(false);

                listTags.getItems().clear();

                // filling the tags visualisation listView
                listTags.getItems().addAll(listener.getTags());
            }
        });
        listDeck.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getClickCount() == 2) { // Checking for double click
                String currentDeckName = listDeck.getSelectionModel().getSelectedItem();
                if (currentDeckName != null) {
                    listener.goDeckContent(currentDeckName);
                }
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
     * The listener interface for the EditDeckView class.
     */
    public interface Listener {
        boolean addDeck(String newDeckName);
        void addNewTag(String newTag);

        void goBack();

        void goDeckContent(String deckName);

        void goStudy(String deckName, int difficulty);

        void goFreeMod(String deckName, int difficulty);

        String removeDeck();

        void removeTag(String tagToRemove);

        void searchTag(String tagToSearch);

        void updateCurrentDeckController(String currentDeckName);


        List<String> getDeckNames();

        List<String> getTags();


        void importDeck();

        void exportDeck(String deckName);

        void unselectDeck();
    }

}
