package ulb.infof307.g9.server;

import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.RankRow;
import ulb.infof307.g9.model.User;

import java.util.List;


/**
 * This class represents a packet sent over the socket.
 * It contains the type of the packet and the data to send.
 */
public class Packet {

    private User user;
    private String deckName;
    private Type type;
    private List<Deck> decks;
    private Deck sentDeck;
    private int score;
    private List<RankRow> leaderboard;

    /**
     * Constructor of packet
     *
     * @param type type of packet to send
     */
    public Packet(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }

    public Deck getSentDeck() {
        return sentDeck;
    }

    public void setSentDeck(Deck sentDeck) {
        this.sentDeck = sentDeck;
    }
    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setScore(int newScore){this.score = newScore;}

    public int getScore(){return this.score;}

    public List<RankRow> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(List<RankRow> leaderboard) {
        this.leaderboard = leaderboard;
    }
}


