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

public class AddToDeckC2SPacket {
    public static final CustomPayload.Id<AddToDeckPayload> ADD_TO_DECK_PAYLOAD_ID =
            new CustomPayload.Id<>(Identifier.of(Mcdeckbuilder.MODID, "add_card_to_deck"));

    public record AddToDeckPayload(String data) implements CustomPayload {
        @Override
        public Id<? extends CustomPayload> getId() {
            return ADD_TO_DECK_PAYLOAD_ID;
        }
    }

    public static void receive(AddToDeckPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = context.server();

        server.execute(() -> {
            if (player.hasAttached(ModDataManager.DECK)) {
                List<String> cards = new ArrayList<>();
                cards.add(payload.data());
//                ModDataManager.initializePlayer(player, cards);
                ModDataManager.addCardsToDeck(player, cards);
            }
        });
    }
}
