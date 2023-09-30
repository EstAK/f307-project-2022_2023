package ulb.infof307.g9;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import ulb.infof307.g9.abstracts.Screen;
import ulb.infof307.g9.abstracts.ScreenChanger;
import ulb.infof307.g9.controller.ControllerFactory;
import ulb.infof307.g9.controller.WelcomeController;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.controller.card.EditCardController;
import ulb.infof307.g9.controller.card.EditHoleCardController;
import ulb.infof307.g9.controller.card.EditMultiChoiceCardController;
import ulb.infof307.g9.controller.card.SelectCardTypeController;
import ulb.infof307.g9.controller.deck.EditContentDeckController;
import ulb.infof307.g9.controller.mods.free.FreeStudyController;
import ulb.infof307.g9.controller.mods.free.ScoreController;
import ulb.infof307.g9.controller.mods.study.StudyController;
import ulb.infof307.g9.model.*;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.view.AlertBuilder;
import ulb.infof307.g9.model.ScoreBasedCardPicker;

import java.io.IOException;

/**
 * This is the Class starting the program.
 * It's a java application
 */
public class Main extends Application implements ScreenChanger {

    private User user;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * The start method is called when the application is starting.
     * It sets up the welcome screen and handles the application close request.
     *
     * @param stage The primary stage of the application.
     * @throws IOException If an error occurs during screen initialization.
     */
    @Override
    public void start(Stage stage) throws IOException {
        WelcomeController welcomeController = new WelcomeController(stage, this);
        welcomeController.show();

        // force the application to stop when the main window is closed
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Show screen.
     *
     * @param controller the controller
     */
    public void showScreen(Controller controller) {
        try {
            controller.show();
        } catch (IOException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("L'écran n'a pas pu s'afficher")
                    .addContent("Une erreur est survenue lors du chargement de l'écran")
                    .addStackTrace(e)
                    .show();
        }
    }
    /**
     * Changes the screen to the specified screen.
     *
     * @param oldStage The current stage.
     * @param screen   The screen to change to.
     */
    @Override
    public void changeScreen(Stage oldStage, Screen screen) {
        try {
            ControllerFactory controllerFactory = new ControllerFactory();
            Controller controller;
            controller = controllerFactory.getController(screen, oldStage, this);
            showScreen(controller);
        } catch (IOException e) {
            new AlertBuilder()
                    .error()
                    .addException(e)
                    .show();
        }
    }


    /**
     * Changes the screen to the ModifyCard screen.
     *
     * @param oldStage  The current stage.
     * @param card      The card to modify.
     * @param deckName  The name of the deck.
     * @param type      The type of the card.
     */
    @Override
    public void changeScreenToModifyCard(Stage oldStage,Card card,String deckName,CardType type){
        if (type == CardType.MULTI_CHOICE || type == CardType.MODULAR_MULTI) {
            EditMultiChoiceCardController editMultiChoiceCardController = new EditMultiChoiceCardController(oldStage, this, deckName,card);
            showScreen(editMultiChoiceCardController);
        } else if (type == CardType.NORMAL || type == CardType.MODULAR_NORMAL) {
            EditCardController editCardController = new EditCardController(oldStage, this, deckName,card);
            showScreen(editCardController);
        } else if (type == CardType.HOLE){
            EditHoleCardController editHoleCardController = new EditHoleCardController(oldStage, this, deckName,card);
            showScreen(editHoleCardController);
        }
    }
    /**
     * Changes the screen to the SelectCardType screen.
     *
     * @param oldStage  The current stage.
     * @param deckName  The name of the deck.
     */
    @Override
    public void changeScreenToSelectCardType(Stage oldStage, String deckName) {
        SelectCardTypeController selectCardTypeController = new SelectCardTypeController(oldStage, this, deckName);
        showScreen(selectCardTypeController);
    }


    /**
     * Changes the screen to the corresponding card type screen.
     *
     * @param oldStage  The current stage.
     * @param deckName  The name of the deck.
     * @param type      The type of the card.
     */
    @Override
    public void changeScreenToCorrespondingCardType(Stage oldStage, String deckName, CardType type) {
        if (type == CardType.MULTI_CHOICE || type == CardType.MODULAR_MULTI) {
            EditMultiChoiceCardController editMultiChoiceCardController = new EditMultiChoiceCardController(oldStage, this, deckName);
            showScreen(editMultiChoiceCardController);
        } else if (type == CardType.NORMAL || type == CardType.MODULAR_NORMAL) {
            EditCardController editCardController = new EditCardController(oldStage, this, deckName);
            showScreen(editCardController);
        } else if (type == CardType.HOLE){
            EditHoleCardController editHoleCardController = new EditHoleCardController(oldStage, this, deckName);
            showScreen(editHoleCardController);
        }
    }
    /**
     * Changes the screen to the study screen.
     *
     * @param oldStage              The current stage.
     * @param scoreBasedCardPicker  The card picker for score-based study mode.
     */
    @Override
    public void changeScreenToStudy(Stage oldStage, ScoreBasedCardPicker scoreBasedCardPicker) {
        StudyController studyController = new StudyController(oldStage, this, scoreBasedCardPicker);
        showScreen(studyController);
    }
    /**
     * Changes the screen to the free mode screen.
     *
     * @param oldStage          The current stage.
     * @param freeCardPicker    The card picker for free study mode.
     */
    @Override
    public void changeScreenToFreeMod(Stage oldStage, FreeCardPicker freeCardPicker) {
        FreeStudyController freeModeController = new FreeStudyController(oldStage, this, freeCardPicker);
        showScreen(freeModeController);
    }
    /**
     * Changes the screen to the score board screen.
     *
     * @param oldStage      The current stage.
     * @param score         The score.
     * @param time          The time.
     * @param deckName      The name of the deck.
     */
    @Override
    public void changeScreenToScoreBoard(Stage oldStage, int score, long time, String deckName) {
        ScoreController scoreController = new ScoreController(oldStage, this, time, score, deckName, this);
        showScreen(scoreController);
    }

    /**
     * Changes the screen to the edit deck screen.
     *
     * @param oldStage      The current stage.
     * @param screen        The screen to change to.
     * @param deckName      The name of the deck.
     */
    @Override
    public void changeScreenToEditDeck(Stage oldStage, Screen screen, String deckName){
        EditContentDeckController editContent = new EditContentDeckController(oldStage, this ,deckName);
        showScreen(editContent);
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
