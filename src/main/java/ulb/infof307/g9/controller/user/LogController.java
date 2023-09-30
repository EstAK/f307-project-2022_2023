package ulb.infof307.g9.controller.user;

import javafx.stage.Stage;
import ulb.infof307.g9.Main;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.model.User;
import ulb.infof307.g9.server.Client;
import ulb.infof307.g9.view.user.LogMenuView;

import java.io.IOException;

/**
 * This is the controller for the Login Menu.
 */
public class LogController extends Controller implements LogMenuView.Listener {

    private final Stage stage;
    private final Main main;
    private final Boolean register;
    private final Client client;

    private LogMenuView logMenuView;

    /**
     * Instantiates a new Log controller
     * @param stage the window
     * @param main the Main
     * @param register boolean for if it is a register screen or Login screen
     * @throws IOException IOException
     */
    public LogController(Stage stage, Main main, Boolean register) throws IOException {
        client = new Client(new Client.OnEventListener() {
            /**
             * create a label with text when error occurs
             */
            @Override
            public void onFailEvent() {
                runAsMainThread(() -> {
                    if (register) logMenuView.changeErrorLabel("nom déjà pris");
                    else logMenuView.changeErrorLabel("mauvaise combinaison");
                });
            }
            /**
             * if no error occurs, return MainMenu View
             */
            @Override
            public void onOkEvent() {
                runAsMainThread(() -> main.changeScreen(stage, Screen.MainMenu));
            }
        });
        this.main = main;
        this.stage = stage;
        this.register = register;

    }

    public void show() throws IOException {
        logMenuView = new LogMenuView();
        logMenuView.setListener(this);
        loadFXMLView(logMenuView, "log_menu.fxml", stage);
        if (register) {
            stage.setTitle("S'enregistrer");
        } else {
            stage.setTitle("Connexion");
        }
    }

    /**
     * We register a new User into the database
     *
     * @param username the entered username
     * @param password the entered password
     */
    private void register(String username, String password) {
        User user = new User(username, password);
        main.setUser(user);
        client.register(user);
    }

    /**
     * Login method used by an existing user
     *
     * @param username the entered username
     * @param password the entered password
     */
    private void login(String username, String password) {
        User user = new User(username, password);
        main.setUser(user);
        client.login(user);
    }

    /**
     * return to Welcome View
     */
    @Override
    public void returnFirstMenu() {
        main.changeScreen(stage, Screen.Welcome);
    }

    /**
     * Verifies if username and password can be registered in database or if they already exists
     *
     * @param username the entered username
     * @param password the entered password
     */
    @Override
    public void confirmLog(String username, String password) {
        if (register) {
            this.register(username, password);
        } else {
            this.login(username, password);

        }
    }
}
