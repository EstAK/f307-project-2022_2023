package ulb.infof307.g9.controller.mods.study;

import javafx.stage.Stage;
import ulb.infof307.g9.abstracts.CardVisitor;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.abstracts.ScreenChanger;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.model.CardPicker;
import ulb.infof307.g9.model.CardType;
import ulb.infof307.g9.model.Modular;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.cards.HoleCard;
import ulb.infof307.g9.model.cards.MultiChoiceCard;
import ulb.infof307.g9.model.cards.StandardCard;
import ulb.infof307.g9.view.AlertBuilder;

import java.io.IOException;
import java.util.Random;

/**
 * Controller for the study mode. It implements the card visitor to know witch controller to start for each card.
 * This mode uses a CardPick interface to pick the next card to display, so the algorithm can be changed.
 * For now, we only use the ScoreBasedCardPicker.
 */
public class StudyController extends Controller implements CardVisitor, CardController.Listener {

    private final Stage stage;
    private final CardPicker randomCardPicker;
    protected final ScreenChanger screenChanger;

    /**
     * Constructor used to create a standard card
     * @param stage the window
     * @param screenChanger Screenchanger
     * @param randomCardPicker CardPicker
     */
    public StudyController(Stage stage, ScreenChanger screenChanger, CardPicker randomCardPicker) {
        this.stage = stage;
        this.randomCardPicker = randomCardPicker;
        this.screenChanger = screenChanger;
    }

    /**
     * Starts the new controller, for the hole card. Then shows the controller's view.
     * @param holeCard the holeCard
     */
    @Override
    public void visit(HoleCard holeCard) {
        try {
            HoleCardController holeCardController = new HoleCardController(stage, holeCard);
            holeCardController.setListener(this);
            holeCardController.show();
        } catch (IOException e) {
            new AlertBuilder()
                    .error()
                    .addException(e)
                    .show();
        }
    }

    /**
     * Starts the new controller, for the multi choice card. Then shows the controller's view.
     * @param multiChoiceCard the multiChoiceCard
     */
    @Override
    public void visit(MultiChoiceCard multiChoiceCard) {
        try {
            MultiChoiceCard cardToStudy = new MultiChoiceCard(multiChoiceCard);

            if (multiChoiceCard.getType().equals(CardType.MODULAR_MULTI))
                cardToStudy = (MultiChoiceCard) new Modular(cardToStudy).randomize(new Random());
            MultiChoiceCardController multiChoiceCardController = new MultiChoiceCardController(stage, cardToStudy);
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
     * Starts the new controller, for the standard card. Then shows the controller's view.
     * @param standardCard the standardCard
     */
    @Override
    public void visit(StandardCard standardCard) {
        try {
            StandardCard cardToStudy = new StandardCard(standardCard);
            if (standardCard.getType().equals(CardType.MODULAR_NORMAL))
                cardToStudy = (StandardCard) new Modular(cardToStudy).randomize(new Random());

            StandardCardController standardCardController = new StandardCardController(stage, cardToStudy);
            standardCardController.setListener(this);
            standardCardController.show();
        } catch (IOException e) {
            new AlertBuilder()
                    .error()
                    .addException(e)
                    .show();
        }
    }

    /**
     * Starts the new controller and it's view
     *
     * @throws IOException the io exception
     */
    @Override
    public void show() throws IOException {
        pickNextCard(); // Picks the first card. The next cards will be displayed to the user
    }

    /**
     * Picks the next card and shows it to the user
     */
    protected void pickNextCard() {
        Card card = randomCardPicker.pick();
        card.accept(this);
    }

    /**
     * Called when the user wants to exit the study mode. Changes the screen to the edit deck screen
     */
    @Override
    public void onExit() {
        screenChanger.changeScreen(stage, Screen.NewEditDeck);
    }

    /**
     * Called when the user wants to go to the next card. Picks the next card and shows it to the user
     */
    @Override
    public void onGoNext() {
        pickNextCard();
    }
}
