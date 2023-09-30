package cardTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g9.model.CardType;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.cards.HoleCard;
import ulb.infof307.g9.model.Modular;
import ulb.infof307.g9.model.cards.MultiChoiceCard;
import ulb.infof307.g9.model.cards.StandardCard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCard {

    private Modular modular;
    private Card card;
    @BeforeEach
    void something() {
        card = new StandardCard("T",":a: plus :b:",":a: + :b:");
    }



        /**
     * Test the equals method of the Card class when the cards are equal
     */
    @Test
    void equalCardTest() {
        Card firstCard = new StandardCard("Titre", "Question", "Réponse");
        Card secondCard = new StandardCard("Titre", "Question", "Réponse");
        assertEquals(firstCard, secondCard);
    }

    /**
     * Test the equals method of the Card class when the cards are not equal
     */
    @Test
    void notEqualCardTest() {
        Card firstCard = new StandardCard("Titre", "Question", "Réponse", 0, 0, 1,CardType.NORMAL);
        Card secondCard = new StandardCard("Titre", "Question", "Réponse", 0, 0, 0,CardType.NORMAL);
        assertNotEquals(firstCard, secondCard);
    }

    /**
     * Test the equals method of the Card class when the cards are equal but have different cooldown
     * The cool down is not taken into account when comparing two cards
     */
    @Test
    void testEqualWithDifferentCoolDown() {
        Card firstCard = new StandardCard("Titre", "Question", "Réponse", 0, 1, 0,CardType.NORMAL);
        Card secondCard = new StandardCard("Titre", "Question", "Réponse", 0, 0, 0,CardType.NORMAL);
        assertEquals(firstCard, secondCard);
    }

    /**
     * Test if the score stays is in the range [-10,10] whit setScore
     */
    @Test
    void testScoreInRange() { // Range must be between -10 and 10
        Card card = new StandardCard("Titre", "Question", "Réponse", 12, 1, 0, CardType.NORMAL);
        assertEquals(10, card.getScore());
        card.setScore(-12);
        assertEquals(-10, card.getScore());
    }

    @Test
    void testModularity(){
        Card multiChoiceCard = new MultiChoiceCard("titre", "combien font :a: plus :b:", ":a: + :b:", " ", " ", " ");
        Card falseCard1 = new StandardCard("T",":a:","Hello :a");
        Card falseCard2 = new StandardCard("T",":a: + :b:","Hello :a:");
        assertTrue(Modular.isFullyModular(multiChoiceCard.getQuestion(),multiChoiceCard.getAnswer()));
        assertFalse(Modular.isFullyModular(falseCard1.getQuestion(),falseCard1.getAnswer()));
        assertFalse(Modular.isFullyModular(falseCard2.getQuestion(),falseCard2.getAnswer()));
    }

    @Test
    void testCalculator(){
        String result = "2";
        modular = new Modular(card);
        assertEquals(result,modular.calculate(new String[]{"1", "+", "1"}));
    }

    @Test
    void testPostFix(){
        ArrayList<String> myResult = new ArrayList<>(List.of("1","1","+"));
        modular = new Modular(card);
        ArrayList<String> result = modular.infixToPostfix(new String[]{"1", "+", "1"});
        assertEquals(myResult,result);
    }

    @Test
    void testHoleCard(){
        HoleCard holeCard = new HoleCard("Hole","Leopold a ete le roi de Belgique","Belgique",0,0,1);
        assertEquals("Leopold a ete le roi de ...", holeCard.getQuestionWithHole());
        assertEquals("Leopold a ete le roi de Belgique", holeCard.getQuestion());
    }
    @Test
    void testHoleCardBasicConstructor(){
        HoleCard holeCard = new HoleCard("Hole","Leopold a ete le roi de Belgique","Belgique");
        assertEquals("Leopold a ete le roi de ...", holeCard.getQuestionWithHole());
        assertEquals("Leopold a ete le roi de Belgique", holeCard.getQuestion());
    }


    @Test
    void testMultipleHolesCard(){
        HoleCard holeCard = new HoleCard("Holes","Leopold nee en Belgique a ete le roi de Belgique","Belgique",0,0,1);
        assertEquals("Leopold nee en ... a ete le roi de ...",holeCard.getQuestionWithHole());
    }

    @Test
    void testEqualsHoleCards(){
        HoleCard firstHoleCard = new HoleCard("Hole","Leopold a ete le roi de Belgique","Belgique");
        HoleCard secondHoleCard = new HoleCard("Hole","Leopold a ete le roi de Belgique","Belgique");
        assertNotSame(firstHoleCard, secondHoleCard);
    }

    @Test
    void testFakeSetter(){
        MultiChoiceCard multiChoiceCard = new MultiChoiceCard("Title", "Who is batman ?", "Bruce Wayne", "Wonder Woman", "Superman", ".");
        multiChoiceCard.setFake1("A");
        multiChoiceCard.setFake2("B");
        multiChoiceCard.setFake3("C");

        assertEquals("A", multiChoiceCard.getFake1());
        assertEquals("B", multiChoiceCard.getFake2());
        assertEquals("C", multiChoiceCard.getFake3());
    }


    @Test
    void testEqualsMultiCards(){
        MultiChoiceCard firstMultiChoiceCard = new MultiChoiceCard("Title", "Who is batman ?", "Bruce Wayne", "Wonder Woman", "Superman", ".");
        MultiChoiceCard secondMultiChoiceCard = new MultiChoiceCard("Title", "Who is batman ?", "Bruce Wayne", "Wonder Woman", "Superman", ".");
        assertNotSame(firstMultiChoiceCard, secondMultiChoiceCard);
    }

    @Test
    void testEqualsStandardCards(){
        StandardCard firstStandardCard = new StandardCard("firstCard","firstQuestion", "firstAnswer");
        StandardCard secondStandardCard = new StandardCard("secondCard","secondQuestion", "secondAnswer");
        assertNotSame(firstStandardCard, secondStandardCard);
    }






}
