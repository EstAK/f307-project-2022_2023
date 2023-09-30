package ulb.infof307.g9.controller.mods.study;

import javafx.stage.Stage;
import ulb.infof307.g9.model.cards.StandardCard;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.view.mods.study.StandardCardView;

import java.io.IOException;
import java.util.List;

/**
 * The controller for the standard card. It's the controller that will be used to study a standard card.
 */
public class StandardCardController extends CardController implements StandardCardView.Listener {
    // the list of the appreciations, ordered from the worst to the best
    private final static List<String> APPRECIATIONS = List.of("Très mauvaise", "Mauvaise", "Moyenne", "Bonne", "Très bonne");
    private final Stage stage;
    private final StandardCard standardCard;
    private StandardCardView view;

    /**
     * Constructor used to create a standard card
     * @param stage the window
     * @param card the current card
     */
    public StandardCardController(Stage stage, StandardCard card) {
        this.stage = stage;
        this.standardCard = card;
    }

    /**
     * Starts the new controller and it's view
     *
     * @throws IOException the io exception
     */
    @Override
    public void show() throws IOException {
        view = new StandardCardView();
        view.setListener(this);
        loadFXMLView(view, "study-standard-card.fxml", stage);
        setCardView(view); // sets the card view to be used by the parent controller
        setCardSide(true); // sets the card side to be displayed -> question side
        // Sets the appreciation buttons label but hides them for now. The user will be able to see them after flipping the card
        view.setAppreciationButtonsLabel(APPRECIATIONS);
        view.setAppreciationButtonsVisibility(false);
    }

    /**
     * Flips the card. After being flipped once, the user can see the appreciation buttons and auto judge himself.
     */
    @Override
    public void onFlipCardClicked() {
        super.onFlipCardClicked();
        view.setAppreciationButtonsVisibility(true); // shows the appreciation buttons
    }

    /**
     * Gets the question side text.
     */
    @Override
    protected String getQuestionSideText() {
        return standardCard.getQuestion();
    }

    /**
     * Gets the answer side text.
     */
    @Override
    protected String getAnswerSideText() {
        return standardCard.getAnswer();
    }

    /**
     * Updates the card score to the Database, depending on the appreciation chosen by the user.
     * Then it calls the controller's listener to go to the next card.
     * @param appreciation the knowledge of the user for this card
     */
    @Override
    public void onAppreciationClicked(String appreciation) {
        try {
            // The reward is calculated by the appreciation index minus the middle index of the list.
            // Ex: if the appreciation is "Bonne", the reward is -> 3 - 2 = 1.
            //     If the appreciation is "Mauvaise", the reward is -> 1 - 2 = -1
            int reward = APPRECIATIONS.indexOf(appreciation) - APPRECIATIONS.size() / 2; // reward in range [-2, 2]
            standardCard.setScore(standardCard.getScore() + reward);
            Database.getInstance().modifyCardScore(standardCard.getTitle(), standardCard.getScore());
            listener.onGoNext(); // calls the controller's listener to go to the next card
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur")
                    .addContent("Impossible de mettre à jour la carte")
                    .addStackTrace(e)
                    .show();
        }
    }
}
