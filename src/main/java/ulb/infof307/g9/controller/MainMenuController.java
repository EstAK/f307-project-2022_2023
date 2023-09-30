package ulb.infof307.g9.controller;


import javafx.stage.Stage;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.abstracts.ScreenChanger;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.view.MainMenuView;

import java.io.IOException;

/**
 * This is the controller for the Main menu.
 */
public class MainMenuController extends Controller implements MainMenuView.Listener {

    private final ScreenChanger listener;
    private final Stage stage;

    /**
     * Instantiates a new Main menu controller.
     *
     * @param stage    the stage
     * @param listener the listener
     */
    public MainMenuController(Stage stage, ScreenChanger listener) {
        this.listener = listener;
        this.stage = stage;
    }

    @Override
    public void show() throws IOException {
        MainMenuView mainMenuView = new MainMenuView();
        mainMenuView.setListener(this);
        stage.setTitle("Menu principal");
        loadFXMLView(mainMenuView, "main_menu.fxml", stage);
    }

    /**
     * Change screen to the Change password view.
     */
    @Override
    public void changePassword() {
        listener.changeScreen(stage, Screen.EditPassword);
    }

    /**
     * Change screen to the Welcome view.
     */
    @Override
    public void disconnect() {
        listener.changeScreen(stage, Screen.Welcome);
    }

    /**
     * Change screen to the DeckMenu.
     */
    @Override
    public void goToDeckModification() {
        listener.changeScreen(stage, Screen.NewEditDeck);
    }

    /**
     * Change screen to the UploadDeck view
     */
    @Override
    public void uploadDeck() {
        listener.changeScreen(stage, Screen.UploadDeckView);
    }
    /**
     * Change screen to the DownloadDeck view.
     */
    @Override
    public void downloadDeck() {
        listener.changeScreen(stage, Screen.DownloadDeckView);
    }
    /**
     * Change screen to the LeaderBoard view.
     */
    @Override
    public void leaderBoard() { listener.changeScreen(stage, Screen.LeaderBoardView);}
}
