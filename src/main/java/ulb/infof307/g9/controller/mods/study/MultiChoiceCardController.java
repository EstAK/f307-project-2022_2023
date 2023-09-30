package ulb.infof307.g9.controller.mods.study;

import javafx.stage.Stage;
import ulb.infof307.g9.model.cards.MultiChoiceCard;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.view.mods.study.MultiChoiceCardView;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The controller for the MultiChoiceCardView and manages its behavior.
 */
public class MultiChoiceCardController extends CardController implements MultiChoiceCardView.Listener {

    private final MultiChoiceCard card;
    private final Stage stage;
    private MultiChoiceCardView view;

    // the more the difficulty is high, the more the reward is high and the more the penalty is low
    private int difficultyRewardForSuccess; // the reward for a correct answer
    private int difficultyPenaltyForFailure; // the penalty for a wrong answer

    private final static int REWARD_LOW = 1;
    private final static int REWARD_MIDDLE = 2;
    private final static int REWARD_HIGH = 3;

    /**
     * Constructor used to create a MultiChoice card
     * @param stage the window
     * @param card the current card
     */
    public MultiChoiceCardController(Stage stage, MultiChoiceCard card) {
        this.card = card;
        this.stage = stage;
    }

    /**
     * Starts the new controller and it's view
     *
     * @throws IOException the io exception
     */
    @Override
    public void show() throws IOException {
        view = new MultiChoiceCardView();
        view.setListener(this);
        loadFXMLView(view, "study-multi-choice-card.fxml", stage);
        setCardView(view); // sets the card view to be used by the parent controller
        setCardSide(true); // sets the card side to be displayed -> question side
        view.setGoNextButtonVisibility(false); // the user can't go to the next card before validating an answer
        view.showMessage(""); // clears the message
        view.setChoiceButtonsVisibility(false); // hides the choice buttons
        view.setDirectAnswerFieldVisibility(false); // hides the direct answer field
        view.setDifficultyButtonsVisibility(true); // shows the difficulty buttons for the user to choose
    }

    /**
     * Flips the card. After being flipped once, the user can't answer to the question anymore.
     * It shows the "Go to next card" button and hides all the buttons to answer the question.
     */
    @Override
    public void onFlipCardClicked() {
        super.onFlipCardClicked();
        // the user can't answer to the question anymore
        view.setChoiceButtonsVisibility(false);
        view.setDirectAnswerFieldVisibility(false);
        view.setDifficultyButtonsVisibility(false);
        // the user can go to the next card
        view.setGoNextButtonVisibility(true);
    }

    /**
     * Gets the question side text. Here, it's the question
     */
    @Override
    protected String getQuestionSideText() {
        return card.getQuestion();
    }

    /**
     * Gets the answer side text. Here, it's the answer
     */
    @Override
    protected String getAnswerSideText() {
        return card.getAnswer();
    }

    /**
     * Validates the answer given by the user and updates the score of the card.
     * After validating the answer, the user can go to the next card.
     * @param choice the answer selected by the user
     */
    @Override
    public void onValidateChoiceClicked(String choice) {
        try {
            if (card.getAnswer().equals(choice)) {
                card.setScore(card.getScore() + difficultyRewardForSuccess);
                view.showMessage("Bonne réponse !");
            } else {
                card.setScore(card.getScore() + difficultyPenaltyForFailure);
                view.showMessage("Mauvaise réponse !");
            }
            Database.getInstance().modifyCardScore(card.getTitle(), card.getScore());
        } catch (DatabaseException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur")
                    .addContent("Impossible de mettre à jour la carte")
                    .addStackTrace(e)
                    .show();
        }

        setCardSide(false); // sets the card side to be displayed -> answer side
        // the user can't answer to the question anymore
        view.setChoiceButtonsVisibility(false);
        view.setDifficultyButtonsVisibility(false);
        view.setDirectAnswerFieldVisibility(false);
        // the user can go to the next card
        view.setGoNextButtonVisibility(true);
    }

    /**
     * Shows 2 choice buttons and hides the difficulty buttons.
     * Sets the reward for a correct answer to 1 because it is a low difficulty,
     * and the reward for a wrong answer is -3.
     */
    @Override
    public void onTwoChoicesClicked() {
        // we shuffle the fake answers to have a random one with the correct answer
        List<String> shuffledFakes = Arrays.asList(card.getFake1(), card.getFake2(), card.getFake3());
        Collections.shuffle(shuffledFakes);
        // we take the first fake answer from the shuffled list and the correct answer, then we shuffle them
        List<String> twoChoices = Arrays.asList(card.getAnswer(), shuffledFakes.get(0));
        Collections.shuffle(twoChoices);

        // sets the choices buttons label and shows them
        view.setChoicesButtonsLabel(twoChoices);
        view.setChoiceButtonsVisibility(true);
        // hides all the difficulty buttons and the direct answer field
        view.setDifficultyButtonsVisibility(false);
        view.setDirectAnswerFieldVisibility(false);
        view.setGoNextButtonVisibility(false); // hides the "Go to next card" button

        difficultyRewardForSuccess = REWARD_LOW; // low difficulty -> low reward
        difficultyPenaltyForFailure = -REWARD_HIGH; // low difficulty -> high penalty


    }

    /**
     * Shows 4 choice buttons and hides the difficulty buttons.
     * Sets the reward for a correct answer to 2 because it is a medium difficulty,
     * and the reward for a wrong answer is -2.
     */
    @Override
    public void onFourChoicesClicked() {
        // we shuffle all the fake answers with the correct one to have a random order
        List<String> fourChoices = Arrays.asList(card.getAnswer(), card.getFake1(), card.getFake2(), card.getFake3());
        Collections.shuffle(fourChoices);

        // sets the choices buttons label and shows them
        view.setChoicesButtonsLabel(fourChoices);
        view.setChoiceButtonsVisibility(true);
        // hides all the difficulty buttons and the direct answer field
        view.setDifficultyButtonsVisibility(false);
        view.setDirectAnswerFieldVisibility(false);
        view.setGoNextButtonVisibility(false); // hides the "Go to next card" button

        difficultyRewardForSuccess = REWARD_MIDDLE; // medium difficulty -> medium reward
        difficultyPenaltyForFailure = -REWARD_MIDDLE; // medium difficulty -> medium penalty
    }

    /**
     * Shows the direct answer field and hides the difficulty buttons.
     * Sets the reward for a correct answer to 3 because it is a high difficulty,
     * and the reward for a wrong answer is -1.
     */
    @Override
    public void onDirectAnswerClicked() {
        // hides all the difficulty buttons and the choice buttons
        view.setChoiceButtonsVisibility(false);
        view.setDifficultyButtonsVisibility(false);
        view.setDirectAnswerFieldVisibility(true);
        view.setGoNextButtonVisibility(false); // hides the "Go to next card" button

        difficultyRewardForSuccess = REWARD_HIGH; // high difficulty -> high reward
        difficultyPenaltyForFailure = -REWARD_LOW; // high difficulty -> low penalty
    }

    /**
     * Passes the event to the controller's listener.
     */
    @Override
    public void onGoNextClicked() {
        listener.onGoNext();
    }
}
