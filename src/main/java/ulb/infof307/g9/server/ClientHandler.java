package ulb.infof307.g9.server;


import com.google.gson.Gson;
import ulb.infof307.g9.model.RankRow;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class handling a single Client, called on each new Client connection
 */
public class ClientHandler implements Runnable {

    public static final List<ClientHandler> clients = new ArrayList<>(); // all the handler know each others
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;
    public final Socket socketClient;

    public ClientHandler(Socket socketClient) {
        this.socketClient = socketClient;
        try {
            // setup
            bufferedReader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));  // msg from client
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));    // msg to client
            clients.add(this);
        } catch (IOException e) {
            System.out.println("connection lost with the host socket");
        }
    }

    /**
     * Sends a Message to a certain Client matching the
     *
     * @param message message to send
     */
    private void sendMessage(String message) {
        try {
            bufferedWriter.write(message + "\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("the buffer writing failed");
        } catch (Exception e) { // when there is no client matching the given id
            e.printStackTrace();
        }
    }

    /**
     * Wrapper method for sendMessage
     *
     * @param packet packet to send
     */
    private void sendPacket(Packet packet) {
        sendMessage(packetToJson(packet));
    }

    /**
     * Converts a packet into a jsonString
     *
     * @param packet Packet to convert into Json
     * @return a JsonString corresponding to the Packet
     */
    public String packetToJson(Packet packet) {
        Gson gson = new Gson();
        return gson.toJson(packet);
    }

    /**
     * Method called when this class is run inside a thread, starting the event handling
     */
    @Override
    public void run() {
        String message;
        Gson gson = new Gson();
        Packet packet;

        while (socketClient.isConnected()) {
            try {
                message = bufferedReader.readLine();
                if (message == null) {
                    terminate();
                    return;
                }
                System.out.println(message);    // for logging purposes


                packet = gson.fromJson(message, Packet.class);

                switch (packet.getType()) {
                    case GET_DECKS -> onGetDecksEvent(packet);
                    case EXPORT_DECK -> onExportDeckEvent(packet);
                    case REGISTER -> onRegisterEvent(packet);
                    case LOGIN -> onLoginEvent(packet);
                    case CHANGE_PASSWORD -> onChangePasswordEvent(packet);
                    case SEND_SCORE -> onScoreEvent(packet);
                    case GET_RANKING -> onGetRankingEvent(packet);
                }

            } catch (IOException e) {
                System.out.println("the connection to the server has been lost");
                return;
            }
        }
    }

    /**
     * Method called when the server receives GET_RANKING
     *
     * @param packet received from the client
     */
    private void onGetRankingEvent(Packet packet) {
        try {
            ArrayList<RankRow> leaderboard = (ArrayList<RankRow>) Database.getInstance().getRankingFromDeck(packet.getDeckName());
            sendPacket(new PacketDirector().buildPacketSendLeaderBoard(leaderboard));
        } catch (DatabaseException e) {
            sendPacket(new PacketDirector().buildPacketFail());
        }
    }

    /**
     * Method called when the server receives CHANGE_PASSWORD
     *
     * @param packet received from the client
     */
    private void onChangePasswordEvent(Packet packet) {
        try {
            Database.getInstance().modifyUserPassword(packet.getUser().getUsername(), packet.getUser().getPassword());
            sendPacket(new PacketDirector().buildPacketOk());
        } catch (DatabaseException e) {
            sendPacket(new PacketDirector().buildPacketFail());
        }
    }

    /**
     * Method called when the server receives EXPORT_DECK message
     *
     * @param packet received from the client
     */
    private void onExportDeckEvent(Packet packet) {
        try {
            Database.getInstance().insertNewDeck(packet.getSentDeck());
            Database.getInstance().modifyDeckDescription(packet.getSentDeck().getName(), packet.getSentDeck().getDescription());
            sendPacket(new PacketDirector().buildPacketOk());
        } catch (DatabaseException e) { // packet insertion failed
            sendPacket(new PacketDirector().buildPacketFail());
        }
    }

    /**
     * Method called when the server receives LOGIN message
     *
     * @param packet received from the client
     */
    private void onLoginEvent(Packet packet) {
        if (Database.getInstance().isLoginValid(packet.getUser())) {
            sendPacket(new PacketDirector().buildPacketOk());
        } else {
            sendPacket(new PacketDirector().buildPacketFail());
        }
    }

    /**
     * Method called when the server sends a SEND_SCORE message
     *
     * @param packet to send to the client
     */
    private void onScoreEvent(Packet packet) {
        try {
            Database.getInstance().modifyScore(packet.getDeckName(), packet.getUser().getUsername(), packet.getScore()); // update the score
            sendPacket(new PacketDirector().buildPacketOk());
        } catch (DatabaseException e) { // if the deck does not exist or the score update fails
            sendPacket(new PacketDirector().buildPacketFail()); // Idea: send a packet with a message explaining the error back to the client using the exception
        }
    }

    /**
     * Method called when the server receives GET_DECKS message
     *
     * @param packet packed received from the client
     */
    private void onGetDecksEvent(Packet packet) {
        try {
            sendPacket(new PacketDirector().buildPacketSendAllDecks(Database.getInstance().getAllDecks()));
        } catch (DatabaseException e) {
            sendPacket(new PacketDirector().buildPacketFail());
        }
    }

    /**
     * Method called when the server receives REGISTER message
     *
     * @param packet packed received from the client
     */
    private void onRegisterEvent(Packet packet) {
        try {
            Database.getInstance().insertNewUser(packet.getUser().getUsername(), packet.getUser().getPassword());
            sendPacket(new PacketDirector().buildPacketOk());
        } catch (DatabaseException e) { // if the registration fails
            sendPacket(new PacketDirector().buildPacketFail());
        }
    }

    private void terminate() throws IOException {
        bufferedReader.close();
        bufferedWriter.close();
        socketClient.close();
        clients.remove(this);
    }
}
