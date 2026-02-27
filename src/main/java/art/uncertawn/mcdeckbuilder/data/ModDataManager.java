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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

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


    public static void testCardLoading() {
        System.out.println("=== TESTING CARD LOADING ===");

        // Create a test JSON string
        String testJson = "{\"name\":\"Zombie\",\"lvl\":1,\"uid\":\"324a55a2-c5ca-4794-8b86-e1efde9e6b5e\"}";

        // Load the same card twice
        Card card1 = CardManager.loadCardFromString(testJson);
        Card card2 = CardManager.loadCardFromString(testJson);

        System.out.println("Card1: " + card1.getName() + " lvl=" + card1.getLevel() + " uid=" + card1.getUid());
        System.out.println("Card2: " + card2.getName() + " lvl=" + card2.getLevel() + " uid=" + card2.getUid());

        // Check each condition separately
        System.out.println("\n=== Checking isSimilar conditions ===");
        System.out.println("Same level? " + (card1.getLevel() == card2.getLevel()));
        System.out.println("Same name? " + card1.getName().equals(card2.getName()));
        System.out.println("Different UID? " + (card1.getUid() != card2.getUid()));
        System.out.println("UIDs equal? " + card1.getUid().equals(card2.getUid()));
        System.out.println("isSimilar result: " + isSimilar(card1, card2));

        // Also check if the UIDs are the same object or different objects with same value
        System.out.println("\n=== UID object comparison ===");
        System.out.println("card1.uid hash: " + System.identityHashCode(card1.getUid()));
        System.out.println("card2.uid hash: " + System.identityHashCode(card2.getUid()));
        System.out.println("Same UUID object? " + (card1.getUid() == card2.getUid()));
    }

}
