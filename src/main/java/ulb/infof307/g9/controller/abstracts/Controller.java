package ulb.infof307.g9.controller.abstracts;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g9.Main;

import java.io.IOException;

/**
 * This is the controller class for the application.
 */
public abstract class Controller {

    /**
     * Method used to edit as the main thread because in javafx we cannot change a gui element from another thread other than the main thread.
     */
    public void runAsMainThread(Runnable runnable) {
        Platform.runLater(runnable);
    }

    /**
     * Starts the new controller and it's view
     *
     * @throws IOException the io exception
     */
    public abstract void show() throws IOException;

    /**
     * Method used to show a new view
     *
     * @param viewController the view controller
     * @param fxml           the fxml file path
     * @param stage          the stage to show the view on
     * @param <T>            the type parameter of the view controller
     * @throws IOException if the fxml file failed to load
     */
    protected <T> void loadFXMLView(T viewController, String fxml, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        fxmlLoader.setController(viewController);
        fxmlLoader.load();
        Parent root = fxmlLoader.getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
