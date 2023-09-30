package ulb.infof307.g9.server;

import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.RankRow;
import ulb.infof307.g9.model.User;

import java.util.List;

/**
 * Director class in the builder design pattern
 */
public class PacketDirector {

    /**
     * builds the Login Packet
     *
     * @param user user that is trying to log in
     * @return the asked Packet
     */
    public Packet buildPacketLogin(User user) {
        return new PacketBuilder(Type.LOGIN).setUser(user).build();
    }

    /**
     * builds the Register Packet
     *
     * @param user user that is trying to register
     * @return the asked Packet
     */
    public Packet buildPacketRegister(User user) {
        return new PacketBuilder(Type.REGISTER).setUser(user).build();
    }

    /**
     * builds the Get_Decks Packet
     *
     * @return the asked Packet
     */
    public Packet buildPacketGetDecks() {
        return new PacketBuilder(Type.GET_DECKS).build();
    }

    /**
     * builds the Ok Packet
     *
     * @return the asked Packet
     */
    public Packet buildPacketOk() {
        return new PacketBuilder(Type.OK).build();
    }

    /**
     * builds the Fail Packet
     *
     * @return the asked Packet
     */
    public Packet buildPacketFail() {
        return new PacketBuilder(Type.FAIL).build();
    }

    /**
     * builds the Export_Deck Packet
     *
     * @return the asked Packet
     */
    public Packet buildPacketExportDeck(Deck deck) {
        return new PacketBuilder(Type.EXPORT_DECK).setDeckToSend(deck).build();
    }

    /**
     * builds the Send_All_Decks Packet
     *
     * @param decks the list of decks to be sent
     * @return the asked Packet
     */
    public Packet buildPacketSendAllDecks(List<Deck> decks) {
        return new PacketBuilder(Type.SEND_ALL_DECKS).setDecks(decks).build();
    }

    /**
     * build the Change_Password Packet
     *
     * @param user new credentials
     * @return the asked Packet
     */
    public Packet buildPacketChangePassword(User user) {
        return new PacketBuilder(Type.CHANGE_PASSWORD).setUser(user).build();
    }

    /**
     * build the Send_Score Packet
     *
     * @param deckName the name of the deck
     * @param score   the user's score on this deck
     * @param user   the user
     * @return score the user's score on this deck
     */
    public Packet buildPacketSendScore(String deckName, int score, User user) {
        return new PacketBuilder(Type.SEND_SCORE).setScore(score).setDeckName(deckName).setUser(user).build();
    }

    /**
     * build the Send Score Packet
     *
     * @param deckName the name of the deck
     * @return leaderboard
     */
    public Packet buildPacketGetLeaderBoard(String deckName) {
        return new PacketBuilder(Type.GET_RANKING).setDeckName(deckName).build();
    }

    /**
     * build the GET_RANKING Packet
     *
     * @param leaderboard the leaderboard
     * @return leaderboard
     */
    public Packet buildPacketSendLeaderBoard(List<RankRow> leaderboard) {
        return new PacketBuilder(Type.GET_RANKING).setLeaderBoard(leaderboard).build();
    }
}
