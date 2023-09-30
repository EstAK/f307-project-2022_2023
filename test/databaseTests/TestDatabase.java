package databaseTests;

import org.junit.jupiter.api.*;
import ulb.infof307.g9.model.*;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.cards.MultiChoiceCard;
import ulb.infof307.g9.model.cards.StandardCard;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.model.database.exceptions.DatabaseExceptionType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestDatabase {

    static Deck deck;

    @Test
    @Order(1)
    void newInsertIntoUserTable() {
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewUser("Vlad", "Vladimir");
            User user = Database.getInstance().getUser("Vlad");
            assertEquals(user, new User("Vlad", "Vladimir") );
        });
    }

    @Test
    @Order(2)
    void nicknameAlreadyTaken() {
        DatabaseException exception = assertThrows(DatabaseException.class, () -> Database.getInstance().insertNewUser("Vlad", "notVladimir"));
        assertEquals(exception.getType(), DatabaseExceptionType.ALREADY_EXISTS);
    }

    @Test
    @Order(3)
    void changePassword() {
        assertDoesNotThrow(() -> {
            Database.getInstance().modifyUserPassword("Vlad", "Vladescu");
            User user = Database.getInstance().getUser("Vlad");
            assertEquals("Vladescu", user.getPassword());
        });
    }

    @Test
    @Order(4)
    void deleteUser() {
        assertDoesNotThrow(() -> Database.getInstance().deleteUser("Vlad"));
    }

    /**
     * Test the insert and get methods of the Card class
     */
    @Test
    @Order(5)
    void newInsertIntoCardTable() {
        Card card = new StandardCard("Titre de la carte", "Question", "Réponse");
        Card[] savedCard = new Card[1];
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewCard(card);
            // cannot access local variable from inside lambda
            savedCard[0] = Database.getInstance().getCard("Titre de la carte");
        });
        assertEquals(card, savedCard[0]);
    }

    /**
     * Test the existCard method of the Card class
     */
    @Test
    @Order(6)
    void cardExistDataBase() {
        // deleting the previously inserted card
        assertDoesNotThrow(() -> {
            Database.getInstance().existCard("Titre de la carte");
            Database.getInstance().deleteCard("Titre de la carte");
        });
        // checking if the deleted card exists
        DatabaseException databaseException = assertThrows(DatabaseException.class, () -> Database.getInstance().existCard("Titre de la carte"));
        assertEquals(databaseException.getType(), DatabaseExceptionType.NO_SUCH_ENTRY);
    }

    /**
     * Test the deleteCard method of the Card class
     */
    @Test
    @Order(7)
    void deleteCard() {
        Card card = new StandardCard("Titre de la carte", "Question", "Réponse");
        assertDoesNotThrow(() ->  {
            Database.getInstance().insertNewCard(card);
            Database.getInstance().deleteCard("Titre de la carte");
        });
    }

    @Test
    @Order(10)
    void insertCardChoices() {
        assertDoesNotThrow(() -> {
            MultiChoiceCard multiChoiceCard = new MultiChoiceCard("multi choice question",
                    "ceci est une question",
                    "réponse",
                    "fausseréponse1",
                    "fausseréponse2",
                    "fausseréponse3");
            Database.getInstance().insertNewCard(multiChoiceCard);
            Database.getInstance().existCard("multi choice question");
        });
    }

    @Test
    @Order(11)
    void getCardChoices() {
        MultiChoiceCard[] multiChoiceCard = new MultiChoiceCard[1];
        assertDoesNotThrow(() -> {
            multiChoiceCard[0] = (MultiChoiceCard) Database.getInstance().getCard("multi choice question");
            Database.getInstance().deleteCard("multi choice question");
        });

        assertEquals(multiChoiceCard[0].getFake1(), "fausseréponse1");
        assertEquals(multiChoiceCard[0].getFake2(), "fausseréponse2");
        assertEquals(multiChoiceCard[0].getFake3(), "fausseréponse3");
    }


    /**
     * Test the Update ranking score.
     */
    @Test
    @Order(12)
    void updateRankingScore() {
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewDeck(new Deck("géo", new ArrayList<>(), "fun"));
            Database.getInstance().modifyScore("géo", "RG", 100);
            assertEquals(Database.getInstance().getScoreFromRanking("géo", "RG"), 100);
            Database.getInstance().deleteScoreFromRanking("géo", "RG");
        });
    }

    /**
     * Test the delete ranking score.
     */
    @Test
    @Order(13)
    void deleteRankingScore() {
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewDeck(new Deck("éthique", new ArrayList<>(), "fun"));
            Database.getInstance().modifyScore("éthique", "RG", 100);
            Database.getInstance().deleteScoreFromRanking("éthique", "RG");
        });
        DatabaseException databaseException = assertThrows(DatabaseException.class, () -> Database.getInstance().getScoreFromRanking("éthique", "RG"));
        assertEquals(databaseException.getType(), DatabaseExceptionType.NO_SUCH_ENTRY);
    }

    /**
     * Test getting the full ranking score.
     */
    @Test
    @Order(14)
    void getFullRanking() {
        assertDoesNotThrow(() -> {
            Database.getInstance().modifyScore("éthique", "RG", 100);
            Database.getInstance().modifyScore("éthique", "Wallenborn the worst", 1);
            Database.getInstance().modifyScore("éthique", "Alexis", 100);

            List<RankRow> ranking = Database.getInstance().getRankingFromDeck("éthique");
            assertEquals(ranking.get(0).username(), "Alexis");
            assertEquals(ranking.get(0).score(),  100);
        });
    }

    @Test
    @Order(15)
    public void deleteFullRanking() {
        assertDoesNotThrow(() -> {
            Database.getInstance().deleteScoreFromRanking("éthique", "RG");
            Database.getInstance().deleteScoreFromRanking("éthique", "Wallenborn the worst");
            Database.getInstance().deleteScoreFromRanking("éthique", "Alexis");
        });
    }

    @Test
    @Order(16)
    public void deleteTestingDecks() {
        assertDoesNotThrow(() -> {
            Database.getInstance().deleteDeck("géo");
            Database.getInstance().deleteDeck("éthique");
        });
    }

    @Test
    @Order(16)
    void newInsertIntoDeckTable() {
        Deck deck = new Deck("Histoire", new ArrayList<>(), "fun");
        Deck[] savedDeck = new Deck[1];
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewDeck(deck);
            savedDeck[0] = Database.getInstance().getDeck("Histoire");
        });

        // Compare deck attributes instead of deck objects
        assertEquals(deck.getName(), savedDeck[0].getName());
        assertEquals(deck.getCards(), savedDeck[0].getCards());
        assertEquals(deck.getTags(), savedDeck[0].getTags());
    }

    @Test
    @Order(17)
    void getAllDecks() {
        List<Deck> decks = new ArrayList<>();
        assertDoesNotThrow(() -> {
            decks.addAll(Database.getInstance().getAllDecks());
        });
        assertTrue(decks.size() >= 1); // Assuming at least one deck has been inserted during tests
    }

    @Test
    @Order(18)
    void removeDeck() {
        assertDoesNotThrow(() -> Database.getInstance().deleteDeck("Histoire"));
        DatabaseException databaseException = assertThrows(DatabaseException.class, () -> Database.getInstance().getDeck("Histoire"));
        assertEquals(databaseException.getType(), DatabaseExceptionType.NO_SUCH_ENTRY);
    }

    @Test
    @Order(9)
    void modifyUserPassword() {
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewUser("Vlad", "Vladimir");
            Database.getInstance().modifyUserPassword("Vlad", "NewPassword");
            User user = Database.getInstance().getUser("Vlad");
            assertEquals("NewPassword", user.getPassword());
        });
        deleteUser();
    }

    @Test
    @Order(19)
    void isLoginValid() throws DatabaseException {
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewUser("Vlad2", "Vladimir");
            User user = new User("Vlad2", "Vladimir");
            assertTrue(Database.getInstance().isLoginValid(user));
        });

        Database.getInstance().deleteUser("Vlad2");
    }

    @Test
    @Order(23)
    void getDescriptionFromDeck() {
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewDeck(new Deck("DeckTest", new ArrayList<>(), "Description du deck"));
            Database.getInstance().modifyDeckDescription("DeckTest", "Description du deck");
            String description = Database.getInstance().getDescriptionFromDeck("DeckTest");
            assertEquals("Description du deck", description);
            Database.getInstance().deleteDeck("DeckTest");
        });

    }

    @Test
    @Order(24)
    void getAuthorFromDeck(){
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewDeck(new Deck("DeckTest", new ArrayList<>(), "Description du deck"));
            Database.getInstance().modifyAuthor("DeckTest", "Auteur du deck");
            String author = Database.getInstance().getAuthorFromDeck("DeckTest");
            assertEquals("Auteur du deck", author);
            Database.getInstance().deleteDeck("DeckTest");
        });
    }

    @Test
    @Order(25)
    void updateDeckDescription() {
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewDeck(new Deck("DeckTest", new ArrayList<>(), "Description du deck"));
            Database.getInstance().modifyDeckDescription("DeckTest", "Nouvelle description du deck");
            String updatedDescription = Database.getInstance().getDescriptionFromDeck("DeckTest");
            assertEquals("Nouvelle description du deck", updatedDescription);
            Database.getInstance().deleteDeck("DeckTest");
        });
    }

    @Test
    @Order(26)
    void updateAuthor() {
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewDeck(new Deck("DeckTest", new ArrayList<>(), "Description du deck"));
            Database.getInstance().modifyAuthor("DeckTest", "Nouvel auteur du deck");
            String updatedAuthor = Database.getInstance().getAuthorFromDeck("DeckTest");
            assertEquals("Nouvel auteur du deck", updatedAuthor);
            Database.getInstance().deleteDeck("DeckTest");
        });
    }

    @Test
    @Order(27)
    void getAllCardsFromDeck() {
        assertDoesNotThrow(() -> {
            Deck deck = new Deck("DeckTest", new ArrayList<>(), "Description du deck");
            deck.addCard(new StandardCard("Card1", "Question 1", "Answer 1"));
            deck.addCard(new StandardCard("Card2", "Question 2", "Answer 2"));
            Database.getInstance().insertNewDeck(deck);
            List<Card> cards = Database.getInstance().getAllCardsFromDeck("DeckTest");
            assertEquals(2, cards.size());
            Database.getInstance().deleteDeck("DeckTest");
        });
    }

    @Test
    @Order(28)
    void insertCardIntoDeck() {
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewDeck(new Deck("DeckTest", new ArrayList<>(), "Description du deck"));
            Database.getInstance().insertNewCard(new StandardCard("Card1", "Question 1", "Answer 1"));
            Database.getInstance().insertCardIntoDeck("Card1", "DeckTest");
            List<Card> cards = Database.getInstance().getAllCardsFromDeck("DeckTest");
            assertEquals(1, cards.size());
            Database.getInstance().deleteDeck("DeckTest");
        });
    }

    @Test
    @Order(29)
    void removeCardFromDeck() {
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewDeck(new Deck("DeckTest", new ArrayList<>(), "Description du deck"));
            Database.getInstance().insertNewCard(new StandardCard("Card1", "Question 1", "Answer 1"));
            Database.getInstance().insertCardIntoDeck("Card1", "DeckTest");
            Database.getInstance().deleteCardFromDeck("DeckTest","Card1");
            List<Card> cards = Database.getInstance().getAllCardsFromDeck("DeckTest");
            assertEquals(0, cards.size());
            Database.getInstance().deleteDeck("DeckTest");
            Database.getInstance().deleteCard("Card1");
        });
    }

    @Test
    @Order(23)
    void changeTagDeckTest() throws DatabaseException {
        StandardCard standardCard = new StandardCard("Title", "Question", "Answer");
        StandardCard secondStandardCard = new StandardCard("Title2", "Question2", "Answer2");
        ArrayList<Card> listCards = new ArrayList<>();
        listCards.add(standardCard);
        listCards.add(secondStandardCard);

        Deck deck = new Deck("name",listCards,"firstTag" );
        Database.getInstance().insertNewDeck(deck);

        Database.getInstance().modifyDeckTags("name", "newTag");
        deck = Database.getInstance().getDeck("name");
        assertEquals(deck.getTags().get(0), "newTag");


        Database.getInstance().deleteDeck("name");

    }

    @Test
    @Order(31)
    void updateCardScore() {
        assertDoesNotThrow(() -> {
            Database.getInstance().insertNewCard(new StandardCard("CardTest", "Question", "Answer"));
            Database.getInstance().modifyCardScore("CardTest", 10);
            Card card = Database.getInstance().getCard("CardTest");
            assertEquals(10, card.getScore());
            Database.getInstance().deleteCard("CardTest");
        });
    }
}
