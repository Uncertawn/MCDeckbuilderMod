package art.uncertawn.mcdeckbuilder.client.graphics.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class ModScreen extends Screen {
    protected ModScreen(Text title) {
        super(title);
    }

    protected ModScreen(MinecraftClient minecraftClient, TextRenderer textRenderer, Text text) {
        super(minecraftClient, textRenderer, text);
    }

    public abstract void initScreen();
}
