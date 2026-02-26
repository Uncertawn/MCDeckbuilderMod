package art.uncertawn.mcdeckbuilder.client.event;

import art.uncertawn.mcdeckbuilder.Mcdeckbuilder;
import art.uncertawn.mcdeckbuilder.client.graphics.screens.DeckDisplay;
import art.uncertawn.mcdeckbuilder.data.ModDataManager;
import art.uncertawn.mcdeckbuilder.networking.packets.InitializePlayerDeckC2SPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class InputHandler {
    public static final String KEY_CATEGORY = "deckbuilder";
    public static final String KEY_OPEN_DECK_INFO = "key.mcdeckbuilder.open_deck_info";

    public static KeyBinding openDeckInfoKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openDeckInfoKey.wasPressed()) {
                ClientPlayerEntity player = client.player;
                player.sendMessage(Text.of(String.valueOf(player.hasAttached(ModDataManager.DECK))), false);
                if (!player.hasAttached(ModDataManager.DECK)) {
                    ClientPlayNetworking.send(new InitializePlayerDeckC2SPacket.InitPlayerDeckPayload(""));
                }
                player.sendMessage(Text.of(String.valueOf(player.getAttached(ModDataManager.DECK))), false);
                MinecraftClient.getInstance().setScreen(new DeckDisplay());
            }
        });
    }

    public static void register() {
        openDeckInfoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_OPEN_DECK_INFO,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                KeyBinding.Category.create(Identifier.of(Mcdeckbuilder.MODID, KEY_CATEGORY))
        ));

        registerKeyInputs();
    }
}
