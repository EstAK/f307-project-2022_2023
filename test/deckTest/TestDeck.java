package deckTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.cards.StandardCard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestDeck {
    private Deck deck;
    private Card firstCard;
    private Card secondCard;
    private Card nonExistingCard;
    private String secondTag;
    private String nonExistingTag;

    @BeforeEach
    void setUp() {
        firstCard = new StandardCard("fun with tree balancing", "", "");
        secondCard = new StandardCard("complexity of bozo sort", "", "");
        nonExistingCard = new StandardCard("the joy of programming", "", "");
        secondTag = "computer science";
        nonExistingTag = "easy";

        List<String> tags = new ArrayList<>();
        String firstTag = "pain";
        tags.add(firstTag);
        tags.add(secondTag);

        List<Card> cards = new ArrayList<>();
        cards.add(firstCard);
        cards.add(secondCard);

        deck = new Deck("algorithms", cards, tags);
    }

    @Test
    void removeExistingCard() {
        assertTrue(deck.removeCard(secondCard));
        assertFalse(deck.hasCard(secondCard));
    }

    @Test
    void removeNonExistingCard() {
        assertFalse(deck.removeCard(nonExistingCard));
    }

    @Test
    void addCard() {
        deck.addCard(nonExistingCard);
        assertTrue(deck.hasCard(nonExistingCard));
    }

    @Test
    void addTag() {
        deck.addTag(nonExistingTag);
        assertTrue(deck.hasTag(nonExistingTag));
    }

    @Test
    void removeExistingTag() {
        assertTrue(deck.removeTag(secondTag));
        assertFalse(deck.hasTag(secondTag));
    }

    @Test
    void removeNonExistingTag() {
        assertFalse(deck.removeTag(nonExistingTag));
    }

    @Test
    void testGetCards() {
        List<Card> cards = deck.getCards();
        assertEquals(2, cards.size());
        assertTrue(cards.contains(firstCard));
        assertTrue(cards.contains(secondCard));
    }

    @Test
    void testGetName() {
        assertEquals("algorithms", deck.getName());
    }

    @Test
    void testSetName() {
        deck.setName("new name");
        assertEquals("new name", deck.getName());
    }

    @Test
    void testGetTags() {
        List<String> tags = deck.getTags();
        assertEquals(2, tags.size());
        assertTrue(tags.contains("pain"));
        assertTrue(tags.contains(secondTag));
    }

    @Test
    void testSetTags() {
        List<String> newTags = new ArrayList<>();
        newTags.add("new tag");
        deck.setTags(newTags);
        List<String> tags = deck.getTags();
        assertEquals(1, tags.size());
        assertTrue(tags.contains("new tag"));
    }

    @Test
    void testGetDescription() {
        assertNull(deck.getDescription());
    }

    @Test
    void testSetDescription() {
        deck.setDescription("new description");
        assertEquals("new description", deck.getDescription());
    }

    @Test
    void testGetFormattedTags() {
        assertEquals("pain;computer science", deck.getFormattedTags());
    }
}

