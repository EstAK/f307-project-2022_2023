package ulb.infof307.g9.view.mods.study;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * The view for the StandardCardController.
 * Displays a StandardCard.
 * This view has buttons for the appreciations to auto-judge himself.
 * The user cannot enter an answer, he can only choose an appreciation to rate how good he knows the card.
 */
public class StandardCardView extends CardView {
    private Listener listener;

    @FXML
    private HBox appreciationButtons; // The HBox that contains the buttons for the appreciations

    /**
     * Sets the listener for the view
     */
    public void setListener(Listener listener) {
        super.setListener(listener); // Call the super method to set the listener of the CardView
        this.listener = listener;
    }

    /**
     * Sets the buttons for appreciations.
     * Clears all the previous buttons and creates new ones with the new appreciations.
     * When a button is clicked, the listener is called with the appreciation as parameter.
     * @param appreciations The list of appreciations
     */
    public void setAppreciationButtonsLabel(List<String> appreciations) {
        appreciationButtons.getChildren().clear();
        for (String appreciation : appreciations) {
            Button button = new Button(appreciation);
            button.setOnAction(this::onAppreciationClicked);
            appreciationButtons.getChildren().add(button);
        }
    }

    /**
     * Sets the visibility of the buttons for appreciations
     */
    public void setAppreciationButtonsVisibility(boolean visible) {
        appreciationButtons.setVisible(visible);
    }

    /**
     * Called when an appreciation button is clicked
     */
    @FXML
    private void onAppreciationClicked(ActionEvent e) {
        Button button = (Button) e.getSource(); // Get the button that was clicked to get the appreciation
        listener.onAppreciationClicked(button.getText());
    }

    /**
     * The listener interface for the StandardCardView
     * It extends the CardView.Listener, so it can also listen to the CardView events
     * @see CardView.Listener
     */
    public interface Listener extends CardView.Listener {
        void onAppreciationClicked(String appreciation);
    }
}
