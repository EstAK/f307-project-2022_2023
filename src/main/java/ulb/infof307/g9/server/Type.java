package ulb.infof307.g9.server;

/**
 * This enum represents the type of packet sent over the socket.
 */
public enum Type {
    GET_DECKS,   // get all decks stored in the server database
    GET_DECKS_BY_TAG,   // get all decks stored in the server database that matches the given tag
    EXPORT_DECK,    // publish a local deck to the store
    SEND_ALL_DECKS, // asks the server to send all the decks from the store
    REGISTER,   // register on the server
    LOGIN,  // log in to the server
    CHANGE_PASSWORD,    // changes password with existing account
    SEND_SCORE,     //send the store
    GET_RANKING,
    OK,
    FAIL
}
