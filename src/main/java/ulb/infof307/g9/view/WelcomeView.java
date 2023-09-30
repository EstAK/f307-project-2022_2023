package ulb.infof307.g9.view;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import ulb.infof307.g9.abstracts.Observable;

/**
 * The WelcomeView class represents the view for the welcome screen.
 * It implements the Observable interface.
 */
public class WelcomeView implements Observable<WelcomeView.Listener> {

    private Listener listener;
    /**
     * Handles the event when the login button is clicked.
     *
     * @param event The mouse event object.
     */
    @FXML
    void login(ActionEvent event) {
        this.listener.login();
    }
    /**
     * Handles the event when the register button is clicked.
     *
     * @param event The mouse event object.
     */
    @FXML
    void register(ActionEvent event) {
        this.listener.register();
    }
    /**
     * Sets the listener for the WelcomeView.
     *
     * @param listener The listener to be set.
     */
    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void register();

        void login();
    }
}
