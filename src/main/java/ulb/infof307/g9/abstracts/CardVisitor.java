package ulb.infof307.g9.abstracts;

import ulb.infof307.g9.model.cards.HoleCard;
import ulb.infof307.g9.model.cards.MultiChoiceCard;
import ulb.infof307.g9.model.cards.StandardCard;

/**
 * each card implement a method accept() who calls visit(this) to know the card type
 */
public interface CardVisitor {
    void visit(HoleCard holeCard);
    void visit(MultiChoiceCard multiChoiceCard);
    void visit(StandardCard standardCard);
}
