package art.uncertawn.mcdeckbuilder.card.cards;

import art.uncertawn.mcdeckbuilder.card.Card;
import art.uncertawn.mcdeckbuilder.card.CardType;

import java.util.function.Supplier;

public class ZombieCard extends Card {
    public ZombieCard() {
        super("Zombie", CardType.ATTACK, 1, 1, "test.png");
    }


    public ZombieCard(int level) {
        super("Zombie", CardType.ATTACK, 1, level, "test.png");
    }
}
