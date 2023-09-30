package ulb.infof307.g9.view.mods.study;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * The view for the CardController.
 * Displays a base Card.
 * This view is used to display the card's sides and gives the ability to the user to use the text to speech feature.
 */
public abstract class CardView {
    private Listener listener;

    @FXML
    private Label cardSideLabel; // The label that displays the card side (question or answer)
    @FXML
    private Button flipCardButton; // The button that flips the card

    /**
     * Sets the listener for the view
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Sets the label of the card side
     */
    public void setCardSideLabel(String label) {
        cardSideLabel.setText(label);
    }

    /**
     * Sets the label of the flip card button
     */
    public void setFlipCardButtonLabel(String label) {
        flipCardButton.setText(label);
    }

    /**
     * Called when the exit button is clicked
     */
    @FXML
    protected void onExitClicked(ActionEvent e) {
        listener.onExitClicked();
    }

    /**
     * Called when the flip card button is clicked
     */
    @FXML
    protected void onFlipCardClicked(ActionEvent e) {
        listener.onFlipCardClicked();
    }

    /**
     * Called when the text to speech button is clicked.
     * Calls the listener with the text in the cardSideLabel
     */
    @FXML
    protected void onTextToSpeechClicked(ActionEvent e) {
        listener.onTextToSpeechClicked(cardSideLabel.getText());
    }

    /**
     * The listener interface for the view
     */
    public interface Listener {
        void onExitClicked();
        void onFlipCardClicked();
        /** @see CardView#onTextToSpeechClicked(ActionEvent) */
        void onTextToSpeechClicked(String text);
    }
}
