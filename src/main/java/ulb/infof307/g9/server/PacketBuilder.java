package ulb.infof307.g9.server;

import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.RankRow;
import ulb.infof307.g9.model.User;

import java.util.List;

/**
 * Builder in the builder designer pattern meant for more flexible and easier Packet manipulation
 */
public class PacketBuilder {

    private final Packet packet;

    public PacketBuilder(Type type) {
        this.packet = new Packet(type);
    }

    public Packet build() {
        return packet;
    }

    public PacketBuilder setDeckToSend(Deck deck) {
        this.packet.setSentDeck(deck);
        return this;
    }

    public PacketBuilder setDecks(List<Deck> decks) {
        this.packet.setDecks(decks);
        return this;
    }

    public PacketBuilder setUser(User user) {
        this.packet.setUser(user);
        return this;
    }

    public PacketBuilder setDeckName(String deckName) {
        this.packet.setDeckName(deckName);
        return this;
    }

    public PacketBuilder setScore(int score){
        this.packet.setScore(score);
        return this;
    }

    public PacketBuilder setLeaderBoard(List<RankRow> scores){
        this.packet.setLeaderboard(scores);
        return this;
    }
}
