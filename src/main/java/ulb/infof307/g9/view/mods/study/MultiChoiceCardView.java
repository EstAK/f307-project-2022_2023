package ulb.infof307.g9.view.mods.study;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;

import java.util.List;

/**
 * The view for the MultiChoiceCardController. Displays a MultiChoiceCard.
 * This view has 3 buttons to set the difficulty (2 choices, 4 choices, direct answer)
 * The choice buttons are contained in a tile pane.
 * The user can also enter the answer directly in a text field if he chooses the direct answer difficulty.
 */
public class MultiChoiceCardView extends CardView {
    private Listener listener;

    @FXML
    private TilePane choiceButtons; // The tile pane that contains the buttons for the choices
    @FXML
    private TextField directAnswerField; // The text field where the user enters the answer directly
    @FXML
    public Button validateDirectAnswerButton; // The button that validates the answer (can also hit ENTER in the text field)
    @FXML
    private Button twoChoicesButton; // The button that sets the difficulty to 2 choices
    @FXML
    private Button fourChoicesButton; // The button that sets the difficulty to 4 choices
    @FXML
    private Button directAnswerButton; // The button that sets the difficulty to direct answer
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
     * Sets the buttons for multiple choices difficulty (2 or 4).
     * Clears all the previous buttons and creates new ones with the new choices.
     * @param choices The list of choices
     */
    public void setChoicesButtonsLabel(List<String> choices) {
        choiceButtons.getChildren().clear();
        for (String choice : choices) {
            Button button = new Button(choice);
            button.setOnAction(e -> onValidateChoice(choice));
            choiceButtons.getChildren().add(button);
        }
    }

    /**
     * Sets the visibility of the buttons for selecting the difficulty (2 choices, 4 choices or direct answer)
     */
    public void setDifficultyButtonsVisibility(boolean visible) {
        twoChoicesButton.setVisible(visible);
        fourChoicesButton.setVisible(visible);
        directAnswerButton.setVisible(visible);
    }

    /**
     * Sets the visibility of the buttons for the choices
     */
    public void setChoiceButtonsVisibility(boolean visible) {
        choiceButtons.setVisible(visible);
    }

    /**
     * Sets the visibility of the direct answer field and the validate button
     */
    public void setDirectAnswerFieldVisibility(boolean visible) {
        directAnswerField.setVisible(visible);
        validateDirectAnswerButton.setVisible(visible);
    }

    /**
     * Sets the text in the messageLabel
     * (used by the controller to display "Correct!" or "Incorrect!")
     */
    public void showMessage(String message) {
        messageLabel.setText(message);
    }

    /**
     * Sets the visibility of the go next button
     */
    public void setGoNextButtonVisibility(boolean visible) {
        goNextButton.setDisable(!visible);
    }

    /**
     * Called when the user selects the 2-choices difficulty
     */
    @FXML
    private void onTwoChoicesClicked(ActionEvent e) {
        listener.onTwoChoicesClicked();
    }

    /**
     * Called when the user selects the 4-choices difficulty
     */
    @FXML
    private void onFourChoicesClicked(ActionEvent e) {
        listener.onFourChoicesClicked();
    }

    /**
     * Called when the user selects the direct answer difficulty
     */
    @FXML
    private void onDirectAnswerClicked(ActionEvent e) {
        listener.onDirectAnswerClicked();
    }

    /**
     * Called when the user clicks on the validate button or hits ENTER in the text field
     */
    @FXML
    private void onValidateDirectAnswerClicked(ActionEvent e) {
        onValidateChoice(directAnswerField.getText());
    }

    /**
     * Called when the user clicks on a choice button or validates the direct answer.
     * Calls the listener to notify that the user has validated a choice
     */
    private void onValidateChoice(String choice) {
        listener.onValidateChoiceClicked(choice);
    }

    /**
     * Called when the user clicks on the go next button
     */
    @FXML
    private void onGoNextClicked(ActionEvent e) {
        listener.onGoNextClicked();
    }

    /**
     * The listener of the MultiChoiceCardView.
     * It extends the CardView.Listener, so it can also listen to the CardView events
     * @see CardView.Listener
     */
    public interface Listener extends CardView.Listener {
        void onValidateChoiceClicked(String choice);
        void onTwoChoicesClicked();
        void onFourChoicesClicked();
        void onDirectAnswerClicked();
        void onGoNextClicked();
    }
}
