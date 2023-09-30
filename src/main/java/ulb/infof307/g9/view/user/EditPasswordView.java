package ulb.infof307.g9.view.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ulb.infof307.g9.abstracts.Observable;
/**
 * The EditPasswordView class represents the view for editing the password.
 * It implements the Observable interface with listeners of type EditPasswordView.Listener.
 */
public class EditPasswordView implements Observable<EditPasswordView.Listener> {

    @FXML
    private TextField newPassword;
    private Listener listener;
    /**
     * Handles the change password event.
     *
     * @param event The mouse event object.
     */
    @FXML
    void changePassword(ActionEvent event) {
        if (!newPassword.getText().equals("")) {
            listener.changePassword(newPassword.getText());
        }
    }
    /**
     * Handles the return to main menu event.
     *
     * @param event The mouse event object.
     */
    @FXML
    void returnMainMenu(ActionEvent event) {
        listener.returnMainMenu();
    }
    /**
     * Sets the listener for the EditPasswordView.
     *
     * @param listener The listener to be set.
     */
    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void changePassword(String newPassword);

        void returnMainMenu();
    }
}
