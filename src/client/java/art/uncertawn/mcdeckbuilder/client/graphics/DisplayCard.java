package art.uncertawn.mcdeckbuilder.client.graphics;

import art.uncertawn.mcdeckbuilder.card.Card;
import art.uncertawn.mcdeckbuilder.card.CardTextureIdentifiers;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;

public class DisplayCard {
    public int x;
    public int y;
    public int y0;
    double f = 1;
    public int width = (int)(80/f);
    public int height = (int)(128/f);
    public Card card;

    public DisplayCard(Card card, int x, int y) {
        this.card = card;
        this.x = x;
        this.y = y;
    }

    public DisplayCard(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void draw(DrawContext context, TextRenderer textRenderer) {

        switch (card.getLevel()) {
            case 2:
                context.drawTexture(RenderPipelines.GUI_TEXTURED, CardTextureIdentifiers.CARD_BACKGROUND_LVL2, x, y, 0, 0, width, height, width, height);
                break;
            default:
                context.drawTexture(RenderPipelines.GUI_TEXTURED, CardTextureIdentifiers.CARD_BACKGROUND_LVL1, x, y, 0, 0, width, height, width, height);
                break;
        }
//        context.drawTexture(RenderPipelines.GUI_TEXTURED, card.getTexture(), x, y, 0, 0, width, height, width, height);
        context.drawText(textRenderer, card.getName(), x+6, y+height/2+ textRenderer.fontHeight/2+2, 0xFFFFFFFF, true);
        context.drawText(textRenderer, String.valueOf(card.getLevel()), x+width-textRenderer.getWidth(String.valueOf(card.getLevel()))-5, y+2, 0xFFFFFFFF, true);
    }

}
