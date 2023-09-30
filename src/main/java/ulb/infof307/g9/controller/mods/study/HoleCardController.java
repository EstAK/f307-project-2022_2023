package ulb.infof307.g9.controller.mods.study;

import javafx.stage.Stage;
import ulb.infof307.g9.model.cards.HoleCard;
import ulb.infof307.g9.model.database.Database;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.view.mods.study.HoleCardView;

import java.io.IOException;

/**
 * The controller for the HoleCardView. Manages the behavior of the view.
 */
public class HoleCardController extends CardController implements HoleCardView.Listener {

    private final Stage stage;
    private final HoleCard card;
    private HoleCardView view;

    /**
     * Constructor used to create a hole card
     * @param stage the window
     * @param card the current card
     */
    public HoleCardController(Stage stage, HoleCard card) {
        this.stage = stage;
        this.card = card;
    }

    /**
     * Starts the new controller and it's view
     *
     * @throws IOException the io exception
     */
    @Override
    public void show() throws IOException {
        view = new HoleCardView();
        view.setListener(this);
        loadFXMLView(view, "study-hole-card.fxml", stage);
        setCardView(view); // sets the card view to be used by the parent controller
        setCardSide(true); // sets the card side to be displayed -> question side
        view.setGoNextButtonVisibility(false); // the user can't go to the next card before validating the answer
        view.showMessage(""); // clears the message
    }

    /**
     * Gets the question side text. Here, it's the question with holes
     */
    @Override
    protected String getQuestionSideText() {
        return card.getQuestionWithHole();
    }

    /**
     * Gets the answer side text. Here, it's the answer
     */
    @Override
    protected String getAnswerSideText() {
        return card.getAnswer();
    }

    /**
     * Validates the answer and updates the card score to the Database.
     * If the answer is correct, the score is incremented by 1, else it's decremented by 1.
     * It also sets the card side to be displayed to the answer side.
     * Finally, it shows the "Go to next card" button and hides the "Validate answer" button.
     * @param answer the answer
     */
    @Override
    public void onValidateAnswerClicked(String answer) {
        try {
            if (card.getAnswer().equals(answer)) {
                card.setScore(card.getScore() + 1);
                view.showMessage("Bonne réponse !");
            } else {
                card.setScore(card.getScore() - 1);
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

        setCardSide(false); // show the answer side
        view.setGoNextButtonVisibility(true); // the user can now go to the next card
        view.setAnswerFieldVisibility(false); // the user can't validate the answer anymore
    }

    /**
     * Passes the event to the controller's listener
     */
    @Override
    public void onGoNextClicked() {
        listener.onGoNext();
    }
}
