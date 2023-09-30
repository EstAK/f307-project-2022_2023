package ulb.infof307.g9.controller.mods.free;

import javafx.stage.Stage;
import ulb.infof307.g9.controller.mods.study.CardController;
import ulb.infof307.g9.controller.mods.study.MultiChoiceCardController;
import ulb.infof307.g9.model.cards.MultiChoiceCard;

/**
 * Controller for the multi choice card in free study mode.
 * Used to catch the event when the user validates his answer.
 */
public class FreeMultiChoiceCardController extends MultiChoiceCardController {
    private Listener listener;

    /**
     * Constructor used to create the controller of FreeMultiChoiceCard
     * @param stage the window
     * @param card the current card
     */
    public FreeMultiChoiceCardController(Stage stage, MultiChoiceCard card) {
        super(stage, card);
    }

    /**
     * Sets the listener to be used by the controller and the parent controller
     * @param listener the listener
     */
    public void setListener(Listener listener) {
        super.setListener(listener);
        this.listener = listener;
    }

    /**
     * Catch the event and passes it to the controller's listener
     * @see FreeStudyController#onValidateChoice(String)
     * @param choice the choice of the user
     */
    @Override
    public void onValidateChoiceClicked(String choice) {
        super.onValidateChoiceClicked(choice);
        listener.onValidateChoice(choice);
    }

    /**
     * The listener interface for receiving events from the controller.
     * Add the onValidateChoice event (used to increment the number of correct answers in the listener controller)
     * @see CardController.Listener
     * @see FreeStudyController#onValidateChoice(String)
     */
    public interface Listener extends CardController.Listener {
        void onValidateChoice(String answer);
    }
}
