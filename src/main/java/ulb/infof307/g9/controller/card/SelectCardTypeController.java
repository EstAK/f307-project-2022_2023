package ulb.infof307.g9.controller.card;

import javafx.stage.Stage;
import ulb.infof307.g9.abstracts.ScreenChanger;
import ulb.infof307.g9.controller.abstracts.Controller;
import ulb.infof307.g9.model.CardType;
import ulb.infof307.g9.view.card.SelectCardTypeView;

import java.io.IOException;

/**
 * Controller for the Card choice creation menu
 */
public class SelectCardTypeController extends Controller implements SelectCardTypeView.Listener {

    private final Stage stage;
    private final ScreenChanger screenChanger;
    private final String deckName;

    /**
     * Constructor used to select type of card
     * @param stage         the stage
     * @param screenChanger the screen changer
     * @param deckName      the name of the deck to which the card will be inserted in
     */
    public SelectCardTypeController(Stage stage, ScreenChanger screenChanger, String deckName) {
        this.stage = stage;
        this.screenChanger = screenChanger;
        this.deckName = deckName;
    }

    /**
     * Change to the view of creating multichoice card
     */
    @Override
    public void createMultiChoiceCard() {
        this.screenChanger.changeScreenToCorrespondingCardType(stage, deckName, CardType.MULTI_CHOICE);
    }
    /**
     * Change to the view of creating Standard card
     */
    @Override
    public void createStandardCard() {
        this.screenChanger.changeScreenToCorrespondingCardType(stage, deckName, CardType.NORMAL);
    }
    /**
     * Change to the view of creating hole card
     */
    public void createHoleQuestion(){this.screenChanger.changeScreenToCorrespondingCardType(stage, deckName, CardType.HOLE); }

    public void show() throws IOException {
        SelectCardTypeView selectCardTypeView = new SelectCardTypeView();
        selectCardTypeView.setListener(this);
        loadFXMLView(selectCardTypeView, "select_card_type.fxml", stage);
        stage.setTitle("Choisissez le type de carte");
    }
}
