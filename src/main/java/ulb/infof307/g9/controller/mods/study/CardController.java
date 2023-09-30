package ulb.infof307.g9.controller.mods.study;

import t2s.son.LecteurTexte;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.view.mods.study.CardView;

/**
 * Controller for the CardView.
 * This controller is used to display the card sides and manages the text to speech feature.
 */
public abstract class CardController extends Controller implements CardView.Listener {
    private static final LecteurTexte lecteurTexte = new LecteurTexte(); // used for text to speech

    protected Listener listener;
    private boolean isQuestionSide = true; // keeps track of which side of the card is currently displayed
    private CardView cardView;

    /**
     * Sets the card view to be used by the controller
     * @param cardView the card view
     */
    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    /**
     * Sets the listener to be used by the controller
     * @param listener the listener
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Passes the event to the controller's listener
     */
    @Override
    public void onExitClicked() {
        listener.onExit();
    }

    /**
     * Sets the card side to be displayed
     * @param isQuestionSide the side of the card (true if question and false if answer)
     */
    protected void setCardSide(boolean isQuestionSide) {
        this.isQuestionSide = isQuestionSide;
        if (isQuestionSide) {
            cardView.setCardSideLabel(getQuestionSideText());
            cardView.setFlipCardButtonLabel("Voir la réponse");
        } else {
            cardView.setCardSideLabel(getAnswerSideText());
            cardView.setFlipCardButtonLabel("Voir la question");
        }
    }

    /**
     * Flips the card side
     */
    @Override
    public void onFlipCardClicked() {
        setCardSide(!isQuestionSide);
    }

    /**
     * Plays the text to speech engin with the given text.
     * @param text text displayed on the view for the card side
     */
    @Override
    public void onTextToSpeechClicked(String text) {
        String textFormatted = text.strip(); // remove leading and trailing spaces
        lecteurTexte.setTexte(textFormatted);
        lecteurTexte.playAll();
    }

    /**
     * Gets the text to be displayed for the question side of the card.
     * Needs to be implemented by the child class.
     */
    protected abstract String getQuestionSideText();

    /**
     * Gets the text to be displayed for the answer side of the card.
     * Needs to be implemented by the child class.
     */
    protected abstract String getAnswerSideText();

    /**
     * Interface used by the upper controller to listen to events from this controller
     */
    public interface Listener {
        void onExit();
        void onGoNext();
    }
}
