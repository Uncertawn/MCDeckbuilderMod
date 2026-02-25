package art.uncertawn.mcdeckbuilder.data;

import art.uncertawn.mcdeckbuilder.Mcdeckbuilder;
import art.uncertawn.mcdeckbuilder.card.CardManager;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ModDataManager {

    public static final List<String> STARTER_DECK = Arrays.asList(
            "{\"name\":\"Creeper\",\"lvl\":1,\"uid\":\""+ UUID.randomUUID() +"\"}",
            "{\"name\":\"Cow\",\"lvl\":1,\"uid\":\""+ UUID.randomUUID() +"\"}"
    );

    public static final AttachmentType<List<String>> DECK = AttachmentRegistry.create(
            Identifier.of(Mcdeckbuilder.MODID, "deck"),
            builder -> builder
                    .initializer(ArrayList::new)  // Default empty list
                    .persistent(Codec.STRING.listOf())  // List codec
                    .syncWith(
                            PacketCodecs.registryCodec(Codec.STRING.listOf()),
                            AttachmentSyncPredicate.targetOnly()
                    )
                    .copyOnDeath()
    );

    public static void register() {}

    public static void initializePlayer(AttachmentTarget player) {
        if (!player.hasAttached(ModDataManager.DECK)) {
            player.setAttached(DECK, STARTER_DECK);
        }
    }

    public static void addCardsToDeck(PlayerEntity player, List<String> newCards) {
        player.modifyAttached(DECK, existingDeck -> {
            if (existingDeck.size()+1 > CardManager.maxCardsInDeck) return existingDeck;
            List<String> updatedDeck = new ArrayList<>(existingDeck);
            updatedDeck.addAll(newCards);
            return updatedDeck;
        });
    }

    public static void removeCardFromDeck(ServerPlayerEntity player, String card) {
        if (player.hasAttached(DECK)) {
            player.modifyAttached(DECK, deck -> {
                List<String> updatedDeck = new ArrayList<>(deck);
                updatedDeck.remove(card);
               return updatedDeck;
            });
        }
    }

    public static List<String> getDeck(PlayerEntity player) {
        return player.getAttachedOrElse(DECK, new ArrayList<>());
    }

}
