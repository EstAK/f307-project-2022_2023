package ulb.infof307.g9.view.card;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import ulb.infof307.g9.abstracts.Observable;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.cards.MultiChoiceCard;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class represents a view for editing multi-choice cards.
 * It implements the Initializable interface and the Observable interface with listeners of type EditMultiChoiceCardView.Listener.
 */
public class EditMultiChoiceCardView implements Initializable, Observable<EditMultiChoiceCardView.Listener> {

    @FXML
    private TextField fieldTitle;
    @FXML
    private TextField fieldQuestion;
    @FXML
    private TextField fieldAnswer;
    @FXML
    private TextField fieldFake1;
    @FXML
    private TextField fieldFake2;
    @FXML
    private TextField fieldFake3;
    @FXML
    private CheckBox isModular;
    private Listener listener;

    /**
     * Method used to register the card in the database.
     *
     * @param event The MouseEvent triggered.
     */
    @FXML
    void registerCard(ActionEvent event) {
        this.listener.registerCard(fieldTitle.getText(),
                fieldQuestion.getText(),
                fieldAnswer.getText(),
                fieldFake1.getText(),
                fieldFake2.getText(),
                fieldFake3.getText(),
                isModular());
    }
    /**
     * Sets the card to be displayed in the edit view.
     *
     * @param card The card to be displayed.
     */
    public void setCard(Card card) {
        fieldTitle.setText(card.getTitle());
        fieldQuestion.setText(card.getQuestion());
        fieldAnswer.setText(card.getAnswer());
        String[] fakes = card.getFakes();
        fieldFake1.setText(fakes[0]);
        fieldFake2.setText(fakes[1]);
        fieldFake3.setText(fakes[2]);
    }
    /**
     * Checks if the isModular checkbox is selected.
     *
     * @return true if the checkbox is selected, false otherwise.
     */
    public boolean isModular() {
        return this.isModular.isSelected();
    }

    /**
     * Disables or enables the fake answer fields based on the given flag.
     *
     * @param disable true to disable the fake answer fields, false to enable them.
     */
    public void disableFakes(boolean disable) {
        this.fieldFake1.setEditable(disable);
        this.fieldFake2.setEditable(disable);
        this.fieldFake3.setEditable(disable);
    }
    /**
     * Initializes the view.
     *
     * @param url            The URL location of the FXML file.
     * @param resourceBundle The ResourceBundle associated with the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.isModular.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                    boolean state = isModular.isSelected();
            fieldFake1.setDisable(state);
                    fieldFake2.setDisable(state);
                    fieldFake3.setDisable(state);
                }
        );
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
     * The listener interface for handling multi-choice card registration.
     */
    public interface Listener {
        void registerCard(String title, String question, String answer, String fake1, String fake2, String fake3, boolean isModular);
    }


}
