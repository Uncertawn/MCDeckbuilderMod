package art.uncertawn.mcdeckbuilder.networking.packets;

import art.uncertawn.mcdeckbuilder.Mcdeckbuilder;
import art.uncertawn.mcdeckbuilder.data.ModDataManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class InitializePlayerDeckC2SPacket {
    public static final CustomPayload.Id<InitPlayerDeckPayload> INITIALIZE_DECK_PAYLOAD_ID =
            new CustomPayload.Id<>(Identifier.of(Mcdeckbuilder.MODID, "init_player_deck"));

    public record InitPlayerDeckPayload(String data) implements CustomPayload {
        @Override
        public Id<? extends CustomPayload> getId() {
            return INITIALIZE_DECK_PAYLOAD_ID;
        }
    }

    public static void receive(InitPlayerDeckPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = context.server();

        server.execute(() -> {
            if (!player.hasAttached(ModDataManager.DECK)) {
                ModDataManager.initializePlayer(player);
            }
        });
    }
}
