package ulb.infof307.g9.controller.mods.free;

import javafx.stage.Stage;
import ulb.infof307.g9.abstracts.ScreenChanger;
import ulb.infof307.g9.controller.mods.study.StudyController;
import ulb.infof307.g9.model.CardType;
import ulb.infof307.g9.model.FreeCardPicker;
import ulb.infof307.g9.model.Modular;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.cards.HoleCard;
import ulb.infof307.g9.model.cards.MultiChoiceCard;
import ulb.infof307.g9.model.cards.StandardCard;
import ulb.infof307.g9.view.AlertBuilder;

import java.io.IOException;
import java.util.Random;

/**
 * Controller for the free study mode.
 * It extends the study controller and overrides the visit methods for the cards.
 * The free study mode only supports multi choice cards, so the other cards are ignored.
 */
public class FreeStudyController extends StudyController implements FreeMultiChoiceCardController.Listener {

    private final Stage stage;
    private final FreeCardPicker cardPicker;
    private MultiChoiceCard card;

    /**
     * Constructor used to create the controller of FreeStudy
     * @param stage the window
     * @param screenChanger the screenChanger
     * @param cardPicker CardPicker
     */
    public FreeStudyController(Stage stage, ScreenChanger screenChanger, FreeCardPicker cardPicker) {
        super(stage, screenChanger, cardPicker);
        this.stage = stage;
        this.cardPicker = cardPicker;
    }

    /**
     * The free study mode does not support hole cards, so we pick the next card
     * @param holeCard the hole card
     */
    @Override
    public void visit(HoleCard holeCard) {
        pickNextCard(); // we only look for MultiChoiceCard, so pick the next one
    }

    /**
     * Starts the new controller, for the multi choice card. Then shows the controller's view.
     * @param multiChoiceCard the multiChoiceCard
     */
    @Override
    public void visit(MultiChoiceCard multiChoiceCard) {
        try {
            card = multiChoiceCard;
            if (card.getType().equals(CardType.MODULAR_MULTI))
                card = (MultiChoiceCard) new Modular(card).randomize(new Random());
            stage.setTitle("Vous avez correctement répondu à " + cardPicker.getNumberOfGoodAnswers());
            FreeMultiChoiceCardController multiChoiceCardController = new FreeMultiChoiceCardController(stage, card);
            multiChoiceCardController.setListener(this);
            multiChoiceCardController.show();
        } catch (IOException e) {
            new AlertBuilder()
                    .error()
                    .addException(e)
                    .show();
        }
    }

    /**
     * The free study mode does not support standard cards, so we pick the next card
     * @param standardCard the standardCard
     */
    @Override
    public void visit(StandardCard standardCard) {
        pickNextCard(); // we only look for MultiChoiceCard, so pick the next one
    }

    /**
     * Picks the next card, and if there is no more card, the game is over.
     * When the game is over, the screen is changed to the score screen.
     */
    @Override
    protected void pickNextCard() {
        Card card = cardPicker.pick();
        if (card != null) { // there are still cards to study
            card.accept(this);
        } else { // the game is over
            screenChanger.changeScreenToScoreBoard(stage, cardPicker.getScore(), cardPicker.getTime(), cardPicker.getNameOfDeck());
        }
    }

    /**
     * When the user validates his answer, we check if it is correct.
     * If it is, we increase the number of good answers, and we update the title of the stage.
     * @param answer the answer
     */
    @Override
    public void onValidateChoice(String answer) {
        if (card.getAnswer().equals(answer)) {
            cardPicker.increaseNumberOfGoodAnswers();
            stage.setTitle("Vous avez correctement répondu à " + cardPicker.getNumberOfGoodAnswers());
        }
    }
}
