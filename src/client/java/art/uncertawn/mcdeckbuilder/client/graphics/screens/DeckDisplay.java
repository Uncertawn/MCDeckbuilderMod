package art.uncertawn.mcdeckbuilder.client.graphics.screens;


import art.uncertawn.mcdeckbuilder.card.Card;
import art.uncertawn.mcdeckbuilder.card.CardManager;
import art.uncertawn.mcdeckbuilder.client.graphics.element.DisplayCard;
import art.uncertawn.mcdeckbuilder.data.ModDataManager;
import art.uncertawn.mcdeckbuilder.networking.packets.AddToDeckC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.text.Text;


import net.minecraft.client.gui.DrawContext;  // Changed from GuiGraphics
import net.minecraft.client.gui.widget.ButtonWidget;  // Changed from components.Button
import net.minecraft.client.toast.SystemToast;

import java.util.*;
import java.util.function.Supplier;

public class DeckDisplay extends ModScreen {
    public DeckDisplay() {
        super(Text.of(""));
    }

    List<String> deck;
    List<DisplayCard> cards;
    DisplayCard selectedCard = null;

    @Override
    protected void init() {
        super.init(); // Call super.init() first


//        deck = this.client.player.getAttachedOrElse(ModDataManager.DECK, new ArrayList<>());
//        deck = ModDataManager.getDeck(this.client.player);
        initScreen();

        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("Hello World"), (btn) -> {
            // When the button is clicked, we can display a toast to the screen.
            if (this.client != null) { // Add null check
                this.client.getToastManager().add(
                        SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE,
                                Text.of("Hello World!"),
                                Text.of("Adding a new card!"))
                );
                UUID uid = UUID.randomUUID();
                ClientPlayNetworking.send(new AddToDeckC2SPacket.AddToDeckPayload("{\"name\":\"Zombie\",\"lvl\":1,\"uid\":\""+uid+"\"}"));
                if (this.client.player.hasAttached(ModDataManager.DECK)) {
                    List<String> cards = new ArrayList<>();
                    cards.add("{\"name\":\"Zombie\",\"lvl\":1,\"uid\":\""+uid+"\"}");
//                ModDataManager.initializePlayer(player, cards);
                    ModDataManager.addCardsToDeck(this.client.player, cards);
                }

                // reload the ui?
                initScreen();
            }
        }).dimensions(40, 140, 120, 20).build();

        // Register the button widget
        this.addDrawableChild(buttonWidget);
    }

    private int scrollOffset = 0;
    private int maxScrollOffset = 0;
    private static final int SCROLL_SPEED = 20;
    int startY = 10 - scrollOffset;

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (maxScrollOffset > 0) {
            scrollOffset = (int) Math.max(0, Math.min(maxScrollOffset,
                    scrollOffset - verticalAmount * SCROLL_SPEED));
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        initScreen();
    }

    @Override
    public void initScreen() {
        // get all the cards                        [X]
        // sort cards by card name                  [X]
        // sort cards based on lvl low -> high      [X]
        // give card display positions              [X]

        deck = ModDataManager.getDeck(this.client.player);
        cards = new ArrayList<>();
        HashMap<DisplayCard, Integer> cardSorter = new HashMap<>();

        for (int i = 0; i < deck.size(); i++) {
            if (CardManager.isCard(deck.get(i))) {
                Card cardInfo = CardManager.loadCardFromString(deck.get(i));
                DisplayCard dc = new DisplayCard(cardInfo);

                cardSorter.put(dc, cardInfo.getLevel());
            }
        }
        List<Map.Entry<DisplayCard, Integer>> list = new ArrayList<>(cardSorter.entrySet());
        Map<String, Supplier<? extends Card>> existingCards = CardManager.getCards();
        list.sort(Comparator.comparing(entry ->
                existingCards.get(entry.getKey().getCard().getName()).get().getName())
        );
//        list.sort((e1, e2) -> {
////            String n1 = existingCards.get(e1.getKey()).getName();
//            String n1 = existingCards.get(e1.getKey().getCard().getName()).getName();
//            String n2 = existingCards.get(e2.getKey().getCard().getName()).getName();
//            return n1.compareTo(n2);
//        });
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
//        System.out.println(cardSorter);

        int cardSpacingX = 5;
        int cardSpacingY = 5;
        int cardWidth = 0;
        int cardHeight = 0;
        int columns = 7;
        int startX = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                DisplayCard c = list.get(i).getKey();
                cardWidth = c.width;
                cardHeight = c.height;
                int cardDisplaySize = (cardWidth + cardSpacingX) * columns;
                if (cardDisplaySize>width) {
                    for (int j = columns-1; j > 0; j--) {
                        cardDisplaySize = (cardWidth + cardSpacingX) * j;
                        if (cardDisplaySize <= width) {
                            columns = j;
                            break;
                        }
                    }
                }
                startX = this.width / 2 - (cardDisplaySize / 2);
                maxScrollOffset = Math.max(0,
                        (int)(Math.ceil((double) list.size()/columns) * (cardHeight+cardSpacingY)-cardSpacingY) - (this.height - startY - startY)
                );
                c.x = startX;
                c.y0 = startY;
                cards.add(c);
            } else {
                DisplayCard c = list.get(i).getKey();
                c.x = (i%columns) * (cardWidth + cardSpacingX) + startX;
                c.y0 = (i/columns) * (cardHeight + cardSpacingY) + startY;
//                System.out.println(c.x + " " + c.y);
                cards.add(c);
            }
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        boolean hoveringACard = false;
        for (DisplayCard card : cards) {
            if (card.isMouseOver((int)mouseX, (int)mouseY)) {
                selectedCard = card;
                hoveringACard = true;
            }
        }
        if (!hoveringACard)
            selectedCard = null;
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (selectedCard != null) {
            MinecraftClient.getInstance().setScreen(new CardActionsSubDisplay(this, selectedCard));

        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta); // This renders background and widgets

        for (DisplayCard cardDisplay : cards) {
            cardDisplay.y = cardDisplay.y0 - scrollOffset;
            cardDisplay.draw(context, this.textRenderer);
        }
    }
}
