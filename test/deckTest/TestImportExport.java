package deckTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.cards.MultiChoiceCard;
import ulb.infof307.g9.model.cards.StandardCard;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestImportExport {

    private static final String JSON_PATH = "test.json";
    private static final Deck underTest = new Deck("testName", new ArrayList<>(), "tagTest");

    @AfterAll
    static void deleteJsons() throws DatabaseException {
        Database.getInstance().deleteDeck(underTest.getName());

        File f = new File(JSON_PATH);
        f.delete();
    }

    @Test
    @Order(1)
    void testExportDeck() {
        assertDoesNotThrow(() -> {
            underTest.addCard(new StandardCard("cardTitleTest", "questionTestSimple", "answerTestSimple"));
            underTest.addCard(new MultiChoiceCard("cardMul", "questionTestMulti", "answerTestMulti", "fake1", "fake2", "fake3"));
            underTest.exportDeck(JSON_PATH);
        });
        File f = new File(JSON_PATH);
        assertTrue(f.exists());
    }

    @Test
    @Order(2)
    void testImportDeck() {
        assertDoesNotThrow(() -> {
            Deck deck = Deck.importDeck(JSON_PATH);
            assertNotEquals(null, deck);
            assert deck != null;
            assertEquals(underTest.getName(), deck.getName());
            assertEquals(underTest.getTags(), deck.getTags());
            assertEquals(underTest.getCards().size(), deck.getCards().size());

            assertEquals(underTest.getCards().get(0).getTitle(), deck.getCards().get(0).getTitle());
            assertEquals(underTest.getCards().get(0).getQuestion(), deck.getCards().get(0).getQuestion());
            assertEquals(underTest.getCards().get(0).getAnswer(), deck.getCards().get(0).getAnswer());

            assertEquals(underTest.getCards().get(1).getTitle(), deck.getCards().get(1).getTitle());
            assertEquals(underTest.getCards().get(1).getQuestion(), deck.getCards().get(1).getQuestion());
            assertEquals(underTest.getCards().get(1).getAnswer(), deck.getCards().get(1).getAnswer());
            assertTrue(deck.getCards().get(1) instanceof MultiChoiceCard);
            MultiChoiceCard underTestCard = (MultiChoiceCard) underTest.getCards().get(1);
            MultiChoiceCard card = (MultiChoiceCard) underTest.getCards().get(1);
            assertEquals(underTestCard.getFake1(), card.getFake1());
            assertEquals(underTestCard.getFake2(), card.getFake2());
            assertEquals(underTestCard.getFake3(), card.getFake3());
        });
    }
}
