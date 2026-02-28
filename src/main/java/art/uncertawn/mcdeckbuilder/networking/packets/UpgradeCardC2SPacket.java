package art.uncertawn.mcdeckbuilder.networking.packets;

import art.uncertawn.mcdeckbuilder.Mcdeckbuilder;
import art.uncertawn.mcdeckbuilder.card.CardManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class UpgradeCardC2SPacket {
    public static final CustomPayload.Id<UpgradeCardPayload> UPGRADE_CARD_PAYLOAD_ID =
            new CustomPayload.Id<>(Identifier.of(Mcdeckbuilder.MODID, "upgrade_card"));

    public record UpgradeCardPayload(String c1, String c2) implements CustomPayload {
        @Override
        public Id<? extends CustomPayload> getId() {
            return UPGRADE_CARD_PAYLOAD_ID;
        }
    }

    public static void receive(UpgradeCardPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = context.server();

        System.out.println(payload.c1+" "+payload.c2);
        server.execute(() -> CardManager.upgradeCardOnPlayer(player, payload.c1, payload.c2));
    }
}
