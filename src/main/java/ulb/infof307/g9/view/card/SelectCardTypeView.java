package ulb.infof307.g9.view.card;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import ulb.infof307.g9.abstracts.Observable;

/**
 * This class represents a view for selecting the type of card to create.
 * It implements the Observable interface with listeners of type SelectCardTypeView.Listener.
 */
public class SelectCardTypeView implements Observable<SelectCardTypeView.Listener> {


    private Listener listener;

    /**
     * run EditCardController menu.
     *
     * @param event the event
     */
    @FXML
    void CreateStandardCard(MouseEvent event) {
        this.listener.createStandardCard();
    }

    /**
     * run EditMultiChoiceCardController menu.
     *
     * @param event the event
     */
    @FXML
    void CreateChoiceCard(MouseEvent event) {
        this.listener.createMultiChoiceCard();
    }
    /**
     * Runs the menu for creating a hole question card.
     *
     * @param event The MouseEvent that triggers the creation of a hole question card.
     */
    @FXML
    void createHoleQuestion(MouseEvent event) {
        this.listener.createHoleQuestion();
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
     * The listener interface for handling card type selection.
     */
    public interface Listener {
        /**
         * Creates a multi-choice card.
         */
        void createMultiChoiceCard();
        /**
         * Creates a createStandardcard.
         */
        void createStandardCard();
        /**
         * Creates a createHoleQuestion card.
         */
        void createHoleQuestion();
    }
}