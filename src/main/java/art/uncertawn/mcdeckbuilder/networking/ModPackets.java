package art.uncertawn.mcdeckbuilder.networking;



import art.uncertawn.mcdeckbuilder.networking.packets.AddToDeckC2SPacket;
import art.uncertawn.mcdeckbuilder.networking.packets.DiscardC2SPacket;
import art.uncertawn.mcdeckbuilder.networking.packets.InitializePlayerDeckC2SPacket;
import art.uncertawn.mcdeckbuilder.networking.packets.UpgradeCardC2SPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;

import static art.uncertawn.mcdeckbuilder.networking.packets.AddToDeckC2SPacket.ADD_TO_DECK_PAYLOAD_ID;
import static art.uncertawn.mcdeckbuilder.networking.packets.InitializePlayerDeckC2SPacket.INITIALIZE_DECK_PAYLOAD_ID;
import static art.uncertawn.mcdeckbuilder.networking.packets.DiscardC2SPacket.DISCARD_PAYLOAD_ID;
import static art.uncertawn.mcdeckbuilder.networking.packets.UpgradeCardC2SPacket.UPGRADE_CARD_PAYLOAD_ID;

public class ModPackets {

    public static void registerC2SPackets() {
        PayloadTypeRegistry.playC2S().register(INITIALIZE_DECK_PAYLOAD_ID,
                PacketCodec.of((value, buf) -> buf.writeString(value.data()),
                        buf -> new InitializePlayerDeckC2SPacket.InitPlayerDeckPayload(buf.readString())));

        PayloadTypeRegistry.playC2S().register(ADD_TO_DECK_PAYLOAD_ID,
                PacketCodec.of((value, buf) -> buf.writeString(value.data()),
                        buf -> new AddToDeckC2SPacket.AddToDeckPayload(buf.readString())));

        PayloadTypeRegistry.playC2S().register(DISCARD_PAYLOAD_ID,
                PacketCodec.of((value, buf) -> buf.writeString(value.data()),
                        buf -> new DiscardC2SPacket.DiscardPayload(buf.readString())));

        PayloadTypeRegistry.playC2S().register(UPGRADE_CARD_PAYLOAD_ID,
                PacketCodec.of((value, buf) -> {
                    buf.writeString(value.c1());
                    buf.writeString(value.c2());
                        },
                        buf -> new UpgradeCardC2SPacket.UpgradeCardPayload(buf.readString(), buf.readString())));

        ServerPlayNetworking.registerGlobalReceiver(INITIALIZE_DECK_PAYLOAD_ID, InitializePlayerDeckC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(ADD_TO_DECK_PAYLOAD_ID, AddToDeckC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DISCARD_PAYLOAD_ID, DiscardC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(UPGRADE_CARD_PAYLOAD_ID, UpgradeCardC2SPacket::receive);

    }

    public static void registerS2CPackets() {
    }
}
