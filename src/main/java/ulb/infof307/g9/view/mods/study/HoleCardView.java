package ulb.infof307.g9.view.mods.study;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

/**
 * The view for the HoleCardController. Displays a HoleCard.
 * This view displays a text field for the user to enter the answer.
 */
public class HoleCardView extends CardView {
    private Listener listener;

    @FXML
    private TextField answerField; // The text field where the user enters the answer
    @FXML
    private Button validateAnswerButton; // The button that validates the answer (can also hit ENTER in the text field)
    @FXML
    private Button goNextButton; // The button that goes to the next card
    @FXML
    private Label messageLabel; // The label that displays a message to the user

    /**
     * Sets the listener for the view
     */
    public void setListener(Listener listener) {
        super.setListener(listener); // Call the super method to set the listener of the CardView
        this.listener = listener;
    }

    /**
     * Sets the text in the messageLabel
     * (used by the controller to display "Correct!" or "Incorrect!")
     */
    public void showMessage(String message) {
        messageLabel.setText(message);
    }

    /**
     * Sets the goNextButton visibility.
     * When the visibility is set to false, the button is disabled,
     * so it's still visible, but the user can't click it
     */
    public void setGoNextButtonVisibility(boolean visible) {
        goNextButton.setDisable(!visible);
    }

    /**
     * Sets the answerField visibility and the validateAnswerButton visibility.
     * When the visibility is set to false, the field is disabled,
     * so it's still visible, but the user can't type in the text field
     */
    public void setAnswerFieldVisibility(boolean visible) {
        answerField.setDisable(!visible);
        validateAnswerButton.setDisable(!visible);
    }

    /**
     * Called when the validate answer button is clicked or the user hit ENTER in the text field.
     * Calls the listener with the answer entered by the user
     * in the answerField
     */
    @FXML
    private void onValidateAnswerClicked(ActionEvent e) {
        listener.onValidateAnswerClicked(answerField.getText());
    }

    /**
     * Called when the go next button is clicked
     */
    @FXML
    private void onGoNextClicked(ActionEvent e) {
        listener.onGoNextClicked();
    }

    /**
     * The listener of the HoleCardView.
     * It extends the CardView.Listener, so it can also listen to the CardView events
     * @see CardView.Listener
     */
    public interface Listener extends CardView.Listener {
        /** @see HoleCardView#onValidateAnswerClicked(ActionEvent) */
        void onValidateAnswerClicked(String answer);
        void onGoNextClicked();
    }
}
