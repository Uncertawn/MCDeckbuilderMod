package art.uncertawn.mcdeckbuilder;

import art.uncertawn.mcdeckbuilder.card.CardManager;
import art.uncertawn.mcdeckbuilder.data.ModDataManager;
import art.uncertawn.mcdeckbuilder.networking.ModPackets;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static art.uncertawn.mcdeckbuilder.data.ModDataManager.DECK;

/*
TODO:
[X] In DeckDisplay add card interactions
[ ] Add card levels
[ ] PvE screen
[ ] When attacking/getting attacked by an entity initialize PvE screen
[ ]
[ ]

If a player gets an extra card they have to go to a deck menu to discard one

Maybe you could combine cards of the same lvl to upgrade them?

Add a new attachment to the player with debuff information
 */

public class Mcdeckbuilder implements ModInitializer {

    public static String MODID = "mcdeckbuilder";

    @Override
    public void onInitialize() {
        ModPackets.registerC2SPackets();

        CardManager.loadCards();
        ModDataManager.register();

        ServerPlayerEvents.JOIN.register((player) -> {
            if (!player.hasAttached(ModDataManager.DECK)) {
                ModDataManager.initializePlayer(player);
            }
        });

//        AttackEntityCallback.EVENT.register(((playerEntity, world, hand, entity, entityHitResult) -> {
//
//            return null;
//        }));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("addCard")
                    .then(CommandManager.argument("card", StringArgumentType.string())
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                String card = StringArgumentType.getString(context, "card");

                                ModDataManager.addCardsToDeck(player, List.of(card));
                                player.sendMessage(Text.of("Successfully added " + card));
                                return 1;
                            })
                    )
            );
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("removeCard")
                            .then(CommandManager.argument("card", StringArgumentType.string())
                                    .executes(context -> {
                                        ServerPlayerEntity player = context.getSource().getPlayer();
                                        String card = StringArgumentType.getString(context, "card");

                                        ModDataManager.removeCardFromDeck(player, card);
                                        player.sendMessage(Text.of("Successfully removed " + card));
                                        return 1;
                                    })
                            )
            );
        });
    }
}
