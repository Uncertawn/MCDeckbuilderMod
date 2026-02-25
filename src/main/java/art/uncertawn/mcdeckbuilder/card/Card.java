package art.uncertawn.mcdeckbuilder.card;

import art.uncertawn.mcdeckbuilder.Mcdeckbuilder;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

public abstract class Card {
    private final String name;
    private final CardType type;
    private double value;
    private final int level;

    private final Identifier texture;

    public Card(String name, CardType type, double value, int level, String texture) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.level = level;
        this.texture = Identifier.of(Mcdeckbuilder.MODID, "textures/cards/"+texture);
    }

    public String getName() {
        return name;
    }

    public CardType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public int getLevel() {
        return level;
    }

    public Identifier getTexture() {
        return texture;
    }

}
