package ulb.infof307.g9.controller;


import javafx.stage.Stage;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.abstracts.ScreenChanger;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.view.WelcomeView;

import java.io.IOException;


/**
 * This the controller for the Welcome Menu.
 */
public class WelcomeController extends Controller implements WelcomeView.Listener {

    private final Stage stage;
    private final ScreenChanger listener;

    /**
     * Constructor used to create WelcomeController
     * @param stage the window
     * @param listener the listener (WelcomeView.Listener)
     */
    public WelcomeController(Stage stage, ScreenChanger listener) {
        this.listener = listener;
        this.stage = stage;
    }

    public void show() throws IOException {
        WelcomeView welcomeView = new WelcomeView();
        welcomeView.setListener(this);
        loadFXMLView(welcomeView, "welcome.fxml", stage);
        stage.setTitle("Bienvenue");
    }

    /**
     * Change screen to register view.
     */
    @Override
    public void register() {
        listener.changeScreen(stage, Screen.Register);
    }

    /**
     * Change screen to login view
     */
    @Override
    public void login() {
        listener.changeScreen(stage, Screen.Login);
    }
}
