package ulb.infof307.g9.controller.user;

import javafx.stage.Stage;
import ulb.infof307.g9.Main;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.RankRow;
import ulb.infof307.g9.model.User;
import ulb.infof307.g9.server.Client;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.view.user.EditPasswordView;

import java.io.IOException;
import java.util.List;

/**
 * This is the controller for the Change password menu.
 */
public class EditPasswordController extends Controller implements EditPasswordView.Listener {

    private final User user;
    private final Stage stage;
    private final Main main;
    private final Client client;

    /**
     * Instantiates a new Change password controller.
     *
     * @param stage a Stage
     * @param main  a Main
     */
    public EditPasswordController(Stage stage, Main main) throws IOException {
        this.user = main.getUser();
        client = new Client(new Client.OnEventListener() {
            @Override
            public void onOkEvent() {
                runAsMainThread(() -> {
                    new AlertBuilder()
                            .addHeader("Succès")
                            .addContent("Modification du mot de passe terminée avec succès")
                            .show();
                });
            }
        });
        this.main = main;
        this.stage = stage;
    }

    /**
     * Change the password.
     *
     * @param newPassword     the new password
     */
    @Override
    public void changePassword(String newPassword) {
        user.setPassword(newPassword);
        client.changePassword(user);
        returnMainMenu();
    }

    /**
     * Return to the Main menu screen.
     */
    @Override
    public void returnMainMenu() {
        main.changeScreen(stage, Screen.MainMenu);
    }

    @Override
    public void show() throws IOException {
        EditPasswordView editPasswordView = new EditPasswordView();
        editPasswordView.setListener(this);
        loadFXMLView(editPasswordView, "edit_password.fxml", stage);
        stage.setTitle("Changer le mot de passe");
    }
}
