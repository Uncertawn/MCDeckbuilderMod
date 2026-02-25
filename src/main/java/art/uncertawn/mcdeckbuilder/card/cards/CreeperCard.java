package art.uncertawn.mcdeckbuilder.card.cards;

import art.uncertawn.mcdeckbuilder.card.Card;
import art.uncertawn.mcdeckbuilder.card.CardType;

public class CreeperCard extends Card {
    public CreeperCard() {
        super("Creeper", CardType.ATTACK, 1, 1, "test.png");
    }


    public CreeperCard(int level) {
        super("Creeper", CardType.ATTACK, 1, level, "test.png");
    }
}
