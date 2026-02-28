package art.uncertawn.mcdeckbuilder.data;

import art.uncertawn.mcdeckbuilder.Mcdeckbuilder;
import art.uncertawn.mcdeckbuilder.card.Card;
import art.uncertawn.mcdeckbuilder.card.CardManager;
import art.uncertawn.mcdeckbuilder.networking.packets.UpgradeCardC2SPacket;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.Supplier;

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

    public static void removeCardFromDeck(PlayerEntity player, String card) {
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

    public static boolean containsSimilar(PlayerEntity player, String card) {
        return containsSimilar(player, CardManager.loadCardFromString(card));
    }

    public static boolean isSimilar(Card card1, Card card2) {
        return card1.getLevel() == card2.getLevel() &&
                card1.getName().equals(card2.getName()) &&
                !card1.getUid().equals(card2.getUid());
    }

    public static boolean containsSimilar(PlayerEntity player, Card card) {
        boolean flag = false;
        System.out.println(getDeck(player));
        for (String card2 : getDeck(player)) {
            Card loaded = CardManager.loadCardFromString(card2);
            if (isSimilar(loaded, card)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static Card getSimilar(PlayerEntity player, Card card) {
        if (containsSimilar(player, card)) {
            for (String card2 : getDeck(player)) {
                Card loaded = CardManager.loadCardFromString(card2);
                if (isSimilar(loaded, card))
                    return loaded;
            }
        }
        return null;
    }
}
