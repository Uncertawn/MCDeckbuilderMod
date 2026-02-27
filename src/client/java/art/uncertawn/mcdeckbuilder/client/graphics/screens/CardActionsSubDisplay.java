package art.uncertawn.mcdeckbuilder.client.graphics.screens;

import art.uncertawn.mcdeckbuilder.card.Card;
import art.uncertawn.mcdeckbuilder.card.CardManager;
import art.uncertawn.mcdeckbuilder.client.graphics.element.DisplayCard;
import art.uncertawn.mcdeckbuilder.data.ModDataManager;
import art.uncertawn.mcdeckbuilder.networking.packets.DiscardC2SPacket;
import art.uncertawn.mcdeckbuilder.networking.packets.InitializePlayerDeckC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CardActionsSubDisplay extends Screen {

    ModScreen prevScreen;
    DisplayCard card;

    protected CardActionsSubDisplay(ModScreen prevScreen, DisplayCard card) {
        super(Text.empty());

        this.prevScreen = prevScreen;
        this.card = card;
    }



    @Override
    protected void init() {
        super.init();

        card.x = 50;
        card.y = 50;
        card.width = card.width * 2;
        card.height = card.height * 2;

        String packedCard = CardManager.packCardToString(card.getCard());
        System.out.println(packedCard);

        ButtonWidget discardButton = ButtonWidget.builder(Text.of("Discard"), (btn) -> {
            ClientPlayNetworking.send(new DiscardC2SPacket.DiscardPayload(packedCard));
            ModDataManager.removeCardFromDeck(this.client.player, packedCard);
            prevScreen.initScreen();
            MinecraftClient.getInstance().setScreen(prevScreen);
        }).dimensions(card.width+card.x+20, card.y, 120, 20).build();

        /// IMPLEMENT AFTER ADDING MORE CARD LEVELS BESIDES THE CURRENT ONE
        ButtonWidget upgradeCardButton = ButtonWidget.builder(Text.of("Upgrade"), (btn) -> {
            Card card1 = ModDataManager.getSimilar(this.client.player, card.getCard());


            MinecraftClient.getInstance().setScreen(prevScreen);
        }).dimensions(card.width+card.x+20, card.y+(card.height/2)-10, 120, 20).build();
        upgradeCardButton.active = ModDataManager.containsSimilar(this.client.player, card.getCard());
        ///

        ButtonWidget backButton = ButtonWidget.builder(Text.of("Go back"), (btn) -> {
            MinecraftClient.getInstance().setScreen(prevScreen);
        }).dimensions(card.width+card.x+20, card.y+card.height, 120, 20).build();

        this.addDrawableChild(discardButton);
        this.addDrawableChild(upgradeCardButton);
        this.addDrawableChild(backButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        card.draw(context, this.textRenderer);
    }

    @Override
    public void close() {
        super.close();
        MinecraftClient.getInstance().setScreen(prevScreen);
    }
}
