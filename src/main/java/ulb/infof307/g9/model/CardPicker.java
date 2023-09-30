package ulb.infof307.g9.model;

import ulb.infof307.g9.model.cards.Card;

import java.util.List;

/**
 * The CardPicker class is used to pick a card from a list of cards.
 * The CardPicker class is abstract and is extended by the SCoreBasedCardPicker and FreeCardPicker class.
 */
public abstract class CardPicker {

    protected List<Card> allCards;

    /**
     * Method used to pick a card from a list of cards.
     * @return the picked card
     */
    public abstract Card pick();
}
