package art.uncertawn.mcdeckbuilder.networking.packets;

import art.uncertawn.mcdeckbuilder.Mcdeckbuilder;
import art.uncertawn.mcdeckbuilder.card.Card;
import art.uncertawn.mcdeckbuilder.card.CardManager;
import art.uncertawn.mcdeckbuilder.data.ModDataManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DiscardC2SPacket {
    public static final CustomPayload.Id<DiscardPayload> DISCARD_PAYLOAD_ID =
            new CustomPayload.Id<>(Identifier.of(Mcdeckbuilder.MODID, "discard"));

    public record DiscardPayload(String data) implements CustomPayload {
        @Override
        public Id<? extends CustomPayload> getId() {
            return DISCARD_PAYLOAD_ID;
        }
    }

    public static void receive(DiscardPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = context.server();

        server.execute(() -> {
            ModDataManager.removeCardFromDeck(player, payload.data());
        });
    }
}
