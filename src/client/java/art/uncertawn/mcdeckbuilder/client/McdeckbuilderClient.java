package art.uncertawn.mcdeckbuilder.client;

import art.uncertawn.mcdeckbuilder.client.event.InputHandler;
import art.uncertawn.mcdeckbuilder.networking.ModPackets;
import net.fabricmc.api.ClientModInitializer;

public class McdeckbuilderClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        InputHandler.register();

        ModPackets.registerS2CPackets();
    }
}
