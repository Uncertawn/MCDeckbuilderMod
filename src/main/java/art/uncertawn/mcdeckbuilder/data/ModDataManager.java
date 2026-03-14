package art.uncertawn.mcdeckbuilder.data;

import art.uncertawn.mcdeckbuilder.Mcdeckbuilder;
import art.uncertawn.mcdeckbuilder.card.Card;
import art.uncertawn.mcdeckbuilder.card.CardManager;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;

import java.util.*;

public class ModDataManager {

    public static List<String> getStarterDeck() {
        return Arrays.asList(
                "{\"name\":\"Creeper\",\"lvl\":1,\"uid\":\"" + UUID.randomUUID() + "\"}",
                "{\"name\":\"Cow\",\"lvl\":1,\"uid\":\"" + UUID.randomUUID() + "\"}"
        );
    }


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

    public static final AttachmentType<List<String>> PLANNED_CARDS = AttachmentRegistry.create(
            Identifier.of(Mcdeckbuilder.MODID, "planned_cards"),
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

    public static void initializeEntity(AttachmentTarget entity) {
        if (!entity.hasAttached(ModDataManager.DECK)) {
            entity.setAttached(DECK, getStarterDeck());
        }
        if (!entity.hasAttached(ModDataManager.PLANNED_CARDS)) {
            entity.setAttached(PLANNED_CARDS, new ArrayList<>());
        }
    }

    public static void addCardsToDeck(PlayerEntity player, List<String> newCards) {
        player.modifyAttached(DECK, existingDeck -> {
            if (existingDeck.size()+1 > Variables.MAX_CARDS_IN_DECK) return existingDeck;
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

    public static void planCardsToPlay(Card[] cards, AttachmentTarget entity) {
        String[] queue = new String[Variables.MAX_CARD_NUMBER_TO_PLAY];
        for (int i = 0; i < cards.length; i++) {
            queue[i] = CardManager.packCardToString(cards[i]);
        }
        planCardsToPlay(queue, entity);
    }

    public static void planCardsToPlay(String[] cards, AttachmentTarget entity) {
        entity.modifyAttached(PLANNED_CARDS, queue -> {
            ArrayList<String> queueCopy = new ArrayList<>(queue);
            queueCopy.addAll(Arrays.stream(cards).toList());
            return queueCopy;
        });
    }
}
