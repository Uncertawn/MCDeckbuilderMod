package art.uncertawn.mcdeckbuilder.card.cards;

import art.uncertawn.mcdeckbuilder.card.Card;
import art.uncertawn.mcdeckbuilder.card.CardType;

public class CowCard extends Card {
    public CowCard() {
        super("Cow", CardType.SKILL, 1, 1, "test.png");
    }


    public CowCard(int level) {
        super("Cow", CardType.SKILL, 1, level, "test.png");
    }
}
