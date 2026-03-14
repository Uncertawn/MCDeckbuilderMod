package art.uncertawn.mcdeckbuilder.battle;

import art.uncertawn.mcdeckbuilder.card.Card;
import art.uncertawn.mcdeckbuilder.data.Variables;
import net.minecraft.entity.Entity;

public class BattleManager {
    // create an identifier to store entity moves

    Fighter f1;
    Fighter f2;
    Card[] cardsToPlay = new Card[Variables.MAX_CARD_NUMBER_TO_PLAY];

    public BattleManager() {
    }

    public void submitTurn() {

    }

    public void advanceTurn() {

    }

    public void setF1(Fighter f1) {
        this.f1 = f1;
    }

    public Fighter getF1() {
        return f1;
    }

    public void setF2(Fighter f2) {
        this.f2 = f2;
    }

    public Fighter getF2() {
        return f2;
    }
}
