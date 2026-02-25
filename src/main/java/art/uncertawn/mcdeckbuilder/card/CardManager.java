package art.uncertawn.mcdeckbuilder.card;

import art.uncertawn.mcdeckbuilder.card.cards.CowCard;
import art.uncertawn.mcdeckbuilder.card.cards.CreeperCard;
import art.uncertawn.mcdeckbuilder.card.cards.ZombieCard;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;

public class CardManager {

    static HashMap<String, Card> cards = new HashMap<>();

    public static int maxCardsInDeck = 21;

    public static void loadCards() {
        cards.put("Creeper", new CreeperCard());
        cards.put("Cow", new CowCard());
        cards.put("Zombie", new ZombieCard());
    }

    public static HashMap<String, Card> getCards() {
        return cards;
    }

    public static boolean isCard(String card) {
        JsonObject c = JsonParser.parseString(card).getAsJsonObject();
        return getCards().containsKey(c.get("name").getAsString());
    }

    /**
     * Since we store all the card data in a string (e.g. "Creeper:1")
     * This function will return a relevant Card class (in this example this would be CreeperCard class)
     * @param card
     * @return Card
     */
    public static Card loadCardFromString(String card) {
        JsonObject c = JsonParser.parseString(card).getAsJsonObject();
        return getCards().get(c.get("name").getAsString());
    }
}
