package ulb.infof307.g9.view.card;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import ulb.infof307.g9.abstracts.Observable;
import ulb.infof307.g9.model.cards.Card;
/**
 * This class represents an edit card view.
 * It implements the Observable interface with listeners of type EditCardView.Listener.
 */
public class EditCardView implements Observable<EditCardView.Listener> {

    @FXML
    private TextField titleField;
    @FXML
    private TextField questionField;
    @FXML
    private TextField answerField;
    @FXML
    private CheckBox isModular;

    private Card card;
    private Listener listener;

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
     * Method used to register the card in the database.
     *
     * @param event The MouseEvent triggered.
     */
    @FXML
    private void registerCard(ActionEvent event) {
        listener.registerCard(titleField.getText(), questionField.getText(), answerField.getText());
    }
    /**
     * Checks if the isModular checkbox is selected.
     *
     * @return true if the checkbox is selected, false otherwise.
     */
    public boolean isModularChecked() {
        return isModular.isSelected();
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
     * The listener interface for handling card registration.
     */
    public interface Listener {
        void registerCard(String title, String question, String answer);
    }
}
