package art.uncertawn.mcdeckbuilder.battle;

import net.minecraft.entity.Entity;

public class Fighter {
    Entity entity;
    boolean submittedTurn = false;

    public Fighter() {

    }

    public void submitTurn() {

    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
