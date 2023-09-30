package ulb.infof307.g9.model;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.view.AlertBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Model class for the deck of cards.
 */
public class Deck implements Serializable {

    @JsonAdapter(JsonCardAdapter.class) // @see JsonCardAdapter
    private final List<Card> cards;
    private String name;
    private List<String> tags;
    private String description;

    /**
     * Instantiates a new Deck model with several tags.
     *
     * @param name  the name of the deck
     * @param cards the cards in the deck
     * @param tags  the tags of the deck
     */
    public Deck(String name, List<Card> cards, List<String> tags) {
        this.name = name;
        this.tags = tags;
        this.cards = cards;
    }

    /**
     * Instantiates a new Deck model with a single tag.
     *
     * @param name  the name of the deck
     * @param cards the cards in the deck
     * @param tag   the tag of the deck
     */
    public Deck(String name, List<Card> cards, String tag) {
        this(name, cards, new ArrayList<>(List.of(tag)));
    }

    /**
     * Imports a deck from a JSON file.
     *
     * @param file the path to the file
     * @return the deck or null if the file does not exist or the deck already exists
     */
    public static Deck importDeck(String file) throws DatabaseException {
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(file));
            Deck deckImport = gson.fromJson(reader, Deck.class);
            Database.getInstance().insertNewDeck(deckImport);
            return deckImport;
        } catch (IOException e) {
            new AlertBuilder().addContent("Attention!")
                    .warning()
                    .addContent("le fichier sélectionné n'existe pas")
                    .show();
            return null;
        }
    }

    /**
     * Gets the card with the correct title.
     *
     * @param title the title of the card
     * @return the card
     */
    public Card getCard(String title) {
        for (Card card : this.cards) {
            if (Objects.equals(card.getTitle(), title)) {
                return card;
            }
        }
        return null;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Boolean hasTag(String tag) {
        return getTags().contains(tag);
    }

    public Boolean hasCard(Card card) {
        return getCards().contains(card);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * Gets the tags in an easy-to-read format.
     *
     * @return the formatted list of tags
     */
    public String getFormattedTags() {
        List<String> listOfTags = getTags();
        return String.join(";", listOfTags);
    }

    public boolean removeCard(Card card) {
        return getCards().remove(card);
    }

    /**
     * Remove a card from the deck by its title
     * @param cardTitle the title of the card to remove
     */
    public void removeCard(String cardTitle) {
        for (Card card : getCards())
            if (card.getTitle().equals(cardTitle)) {
                removeCard(card);
                return;
            }
    }

    public boolean addCard(Card card) {
        return getCards().add(card);
    }

    public void addTag(String tag) {
        getTags().add(tag);
    }

    /**
     * export a deck in json file
     *
     * @param file the file
     */
    public void exportDeck(String file) throws IOException {
        Gson gson = new Gson();
        if (file != null) {
            FileWriter filewriter = new FileWriter(file);
            gson.toJson(this, filewriter);
            filewriter.close();
        }
    }

    public boolean removeTag(String tagName) {
        return getTags().remove(tagName);
    }
}
