package ulb.infof307.g9.view.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ulb.infof307.g9.abstracts.Observable;

/**
 * The LogMenuView class represents the view for the login menu.
 * It implements the Observable interface.
 */
public class LogMenuView implements Observable<LogMenuView.Listener> {

    @FXML
    private Label errorLabel;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    private Listener listener;

    /**
     * Verifies if username and password can be registered in database or if they already exists
     *
     * @param event the mouse event
     */
    @FXML
    void confirmLog(ActionEvent event) {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        if (!username.getText().equals("") && !password.getText().equals("")) {

            listener.confirmLog(username.getText(), password.getText());

        } else {
            errorLabel.setText("Veuillez remplir tous les champs de texte");
            errorLabel.setVisible(true);
        }
    }

    /**
     * changes the content of error label and sets it to visible
     *
     * @param text the content of the error label
     */
    public void changeErrorLabel(String text) {
        errorLabel.setText(text);
        errorLabel.setVisible(true);
    }

    /**
     * Returns to first menu.
     *
     * @param event the mouse event
     */
    @FXML
    void returnFirstMenu(ActionEvent event) {
        listener.returnFirstMenu();
    }
    /**
     * Sets the listener for the LogMenuView.
     *
     * @param listener The listener to be set.
     */
    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void returnFirstMenu();

        void confirmLog(String username, String password);
    }
}
