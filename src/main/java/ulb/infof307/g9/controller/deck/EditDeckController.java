package ulb.infof307.g9.controller.deck;

import javafx.stage.Stage;
import ulb.infof307.g9.Main;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.model.*;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.ScoreBasedCardPicker;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.view.deck.EditDeckView;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

/**
 * Controller for the Deck editor menu
 */
public class EditDeckController extends Controller implements EditDeckView.Listener {

    private final Stage stage;
    private final Main main;
    private final Map<String, Deck> deckMap = new HashMap<>();
    private EditDeckView editDeckView;
    private Deck currentDeck;

    /**
     * Constructor used to edit deck
     * @param stage the window
     * @param main the Main
     */
    public EditDeckController(Stage stage, Main main) {
        this.main = main;
        this.stage = stage;

        try {
            List<Deck> decks = Database.getInstance().getAllDecks();
            for (Deck deck : decks) deckMap.put(deck.getName(), deck);
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur !")
                    .addContent("Aucun paquet n'a pu être chargé")
                    .addStackTrace(e)
                    .show();
        }
    }

    @Override
    public void show() throws IOException {
        editDeckView = new EditDeckView();
        editDeckView.setListener(this);
        loadFXMLView(editDeckView, "new_deck_view.fxml", stage);
        stage.setTitle("Menu Deck");
    }

    /**
     * method associated to addDeckButton, adds a new deck from the data in the TextView deckName to the database
     *
     * @param newDeckName the name of the new deck
     * @return boolean for success or failure of operation
     */
    @Override
    public boolean addDeck(String newDeckName) {
        try {
            Deck deck = new Deck(newDeckName, new ArrayList<>(), new ArrayList<>());
            Database.getInstance().insertNewDeck(deck);
            deckMap.put(newDeckName, deck);
            editDeckView.addToListDeck(newDeckName);
            Database.getInstance().modifyAuthor(newDeckName, main.getUser().getUsername()); //We add the author by default.
            return true;
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .warning()
                    .addHeader("Attention !")
                    .addContent("Un paquet de cartes avec ce nom existe déjà")
                    .show();
            return false;
        }
    }

    /**
     * method associated to the addTagButton, adds the new tag to the gui deck and database
     *
     * @param newTag the name of the new tag
     */
    @Override
    public void addNewTag(String newTag) {
        try {
            Database.getInstance().modifyDeckTags(currentDeck.getName(), currentDeck.getFormattedTags());
            currentDeck.addTag(newTag);
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Attention !")
                    .addContent("Le tag n'a pas pu être ajouté")
                    .show();
        }
    }

    /**
     * method associated to the goBackButton, changes the current menu to the mainMenu
     */
    @Override
    public void goBack() {
        main.changeScreen(stage, Screen.MainMenu);
    }

    /**
     * method associated to the gotStudyButton, changes the current menu to the study menu
     * @param deckName the name of the deck
     * @param difficulty the difficulty (Int)
     */
    @Override
    public void goStudy(String deckName, int difficulty) {
        Deck deck = deckMap.get(deckName);
        if (deck.getCards().isEmpty()) {
            return;
        }
        ScoreBasedCardPicker scoreBasedCardPicker = new ScoreBasedCardPicker(deck);
        main.changeScreenToStudy(stage, scoreBasedCardPicker);
    }

    /**
     * Change screen to the view of freeMod
     * @param deckName the name of the deck
     * @param difficulty the difficulty (Int)
     */
    @Override
    public void goFreeMod(String deckName, int difficulty) {
        Deck deck = deckMap.get(deckName);
        FreeCardPicker freeCardPicker = new FreeCardPicker(deck);
        if (!freeCardPicker.haveCards()) {
            return;
        }
        main.changeScreenToFreeMod(stage, freeCardPicker);
    }

    /**
     * Choose the corresponding file
     * @return the selected file
     */
    public String chooseFile() {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(fc);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().toString();
        }
        return null;
    }

    /**
     * Import the corresponding deck
     */
    @Override
    public void importDeck() {
        String file = chooseFile();
        if (file != null) {
            try {
                Deck deckImport = Deck.importDeck(file);
                String deckName = null;
                if (deckImport != null) {
                    deckName = deckImport.getName();
                }
                // Update the view
                editDeckView.addToListDeck(deckName);
                deckMap.put(deckName, deckImport);
            } catch (DatabaseException e) {
                new AlertBuilder()
                        .error()
                        .addHeader("Attention !")
                        .addContent("Le fichier n'est pas valide ou le paquet de cartes existe déjà")
                        .show();
            }
        }
    }

    /**
     * Export the corresponding deck
     * @param deckName the name of the deck
     */
    @Override
    public void exportDeck(String deckName) {
        try {
            String file = chooseFile();
            if (file != null) {
                Deck deck = deckMap.get(deckName);
                deck.exportDeck(file);
            }
        } catch (IOException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur !")
                    .addContent("Erreur d'export")
                    .show();
        }
    }

    /**
     * method associated to the removeDeckButton (only available if there is a selected item)
     *
     * @return the deck to remove or null object if removal failed
     */
    @Override
    public String removeDeck() {
        String deckToRemove = currentDeck.getName();
        try {
            Database.getInstance().deleteDeck(deckToRemove);
            deckMap.remove(deckToRemove);
            return deckToRemove;
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Attention !")
                    .addContent(String.format("Impossible de supprimer le deck %s", deckToRemove))
                    .show();
            return null;
        }
    }

    /**
     * method associated to the removeTagButton, removes the tag from the deck gui and database
     * @param tagToRemove the name of the tag to remove
     */
    @Override
    public void removeTag(String tagToRemove) {
        List<String> listOfCurrentTags = currentDeck.getTags();
        try {
            // tag must exist in the list of tags
            assert listOfCurrentTags.contains(tagToRemove);

            // settings the new tags in the deck model
            listOfCurrentTags.remove(tagToRemove);
            currentDeck.setTags(listOfCurrentTags);

            Database.getInstance().modifyDeckTags(currentDeck.getName(), currentDeck.getFormattedTags());
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur")
                    .addContent("Le tag n'a pas pu être enlevé de la base de données")
                    .show();
            // reversing the operation
            listOfCurrentTags.add(tagToRemove);
            currentDeck.setTags(listOfCurrentTags);
        }
    }


    /**
     * method associated to the search bar in fxml triggered by the searchTagButton, it filters the displayed
     * deck names on the listDeck with the given tag
     * @param tagToSearch the name of the tag to search
     */
    @Override
    public void searchTag(String tagToSearch) {
        unselectDeck();
        if (tagToSearch.isBlank()) { // guard condition resetting tags when blank
            editDeckView.addToListDeck(new ArrayList<>(deckMap.keySet()));
            return;
        }
        // filtering the deck names with the given tags
        List<String> tags = Arrays.asList(tagToSearch.split(";"));
        List<String> filteredDeckNames = deckMap.values().stream()
                .filter(deck -> new HashSet<>(deck.getTags()).containsAll(tags)) // only keep decks with all the tags
                .map(Deck::getName)
                .toList();
        editDeckView.addToListDeck(filteredDeckNames);
    }

    /**
     * Change the current deck name
     * @param deckName the new name of the deck
     */
    public void updateCurrentDeckController(String deckName) {
        currentDeck = deckMap.get(deckName);
    }

    /**
     * Unselect the deck
     */
    @Override
    public void unselectDeck() {
        currentDeck = null;
    }

    /**
     * Get the names of the decks
     * @return names of the decks
     */
    @Override
    public List<String> getDeckNames() {
        return new ArrayList<>(deckMap.keySet());
    }

    /**
     * Get the tags of the decks
     * @return tags of the decks
     */
    @Override
    public List<String> getTags() {
        return currentDeck.getTags();
    }

    /**
     * Change screen to the editContentDeck View
     * @param deckName the name of the deck
     */
    @Override
    public void goDeckContent(String deckName) {
        main.changeScreenToEditDeck(stage, Screen.EditContentDeck, deckName);
    }
}

