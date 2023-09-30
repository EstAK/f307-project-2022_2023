package ulb.infof307.g9.controller;

import javafx.stage.Stage;
import ulb.infof307.g9.Main;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.abstracts.ScreenChanger;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.controller.deck.EditDeckController;
import ulb.infof307.g9.controller.store.DownloadDeckController;
import ulb.infof307.g9.controller.store.LeaderBoardController;
import ulb.infof307.g9.controller.store.UploadDeckController;
import ulb.infof307.g9.controller.user.EditPasswordController;
import ulb.infof307.g9.controller.user.LogController;

import java.io.IOException;


/**
 * This is the ControllerFactory
 * (doesn't manage EditCardController and StudyController)
 */
public class ControllerFactory {

    /**
     * Get controller.
     *
     * @param controller    the controller
     * @param stage         the stage
     * @param screenChanger the screen changer
     * @throws IOException IOException
     * @return the controller
     */
    public Controller getController(Screen controller, Stage stage, ScreenChanger screenChanger) throws IOException {
        return switch (controller) {
            case Register -> new LogController(stage, (Main) screenChanger, true);
            case Login -> new LogController(stage, (Main) screenChanger, false);
            case MainMenu -> new MainMenuController(stage, screenChanger);
            case EditPassword -> new EditPasswordController(stage, (Main) screenChanger);
            case NewEditDeck -> new EditDeckController(stage, (Main) screenChanger);
            case DownloadDeckView -> new DownloadDeckController(stage, screenChanger);
            case UploadDeckView -> new UploadDeckController(stage, (Main) screenChanger);
            case LeaderBoardView -> new LeaderBoardController(stage, screenChanger);
            default -> new WelcomeController(stage, screenChanger);
        };
    }
}
