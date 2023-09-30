package ulb.infof307.g9.view.card;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ulb.infof307.g9.abstracts.Observable;
import ulb.infof307.g9.model.cards.Card;

/**
 * This class represents a view for editing hole cards.
 * It implements the Observable interface with listeners of type EditHoleCardView.Listener.
 */
public class EditHoleCardView implements Observable<EditHoleCardView.Listener> {

    @FXML
    private TextField answerField;

    @FXML
    private TextField questionField;

    @FXML
    private TextField titleField;

    private Card card;
    private Listener listener;

    /**
     * Method used to register the card in the database.
     *
     * @param event The MouseEvent triggered.
     */
    @FXML
    void registerCard(ActionEvent event) {
        listener.registerCard(titleField.getText(), questionField.getText(), answerField.getText());
    }
    /**
     * Sets the card to be displayed in the edit view.
     *
     * @param card The card to be displayed.
     */
    public void setCard(Card card) {
        titleField.setText(card.getTitle());
        questionField.setText(card.getQuestion());
        answerField.setText(card.getAnswer());
    }
    /**
     * Sets the listener for this view.
     *
     * @param listener The listener to be set.
     */
    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * The listener interface for handling HoleCard registration.
     */
    public interface Listener {
        void registerCard(String title, String question, String answer);
    }

}

