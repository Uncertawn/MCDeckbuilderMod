package art.uncertawn.mcdeckbuilder.card;

import art.uncertawn.mcdeckbuilder.card.cards.CowCard;
import art.uncertawn.mcdeckbuilder.card.cards.CreeperCard;
import art.uncertawn.mcdeckbuilder.card.cards.ZombieCard;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class CardManager {

    static Map<String, Supplier<? extends Card>> cards = new HashMap<>();

    public static int maxCardsInDeck = 21;

    public static void loadCards() {
        cards.put("Creeper", CreeperCard::new);
        cards.put("Cow", CowCard::new);
        cards.put("Zombie", ZombieCard::new);
    }

    public static Map<String, Supplier<? extends Card>> getCards() {
        return cards;
    }

    public static boolean isCard(String card) {
        JsonObject c = JsonParser.parseString(card).getAsJsonObject();
        return getCards().containsKey(c.get("name").getAsString());
    }

    public static boolean isCard(Card card) {
        return getCards().containsKey(card.getName());
    }

    /**
     * Since we store all the card data in a json string (e.g. {"name":"Creeper", "lvl":1, "uid":UUID})
     * side note: pretend the formatting is a proper json string ^
     * This function will return a relevant Card class (in this example this would be CreeperCard class)
     * @param card
     * @return Card
     */
    public static Card loadCardFromString(String card) {
        JsonObject c = JsonParser.parseString(card).getAsJsonObject();
        // "{\"name\":\"Creeper\",\"lvl\":1,\"uid\":\""+ UUID.randomUUID() +"\"}",
        String name = c.get("name").getAsString();
        int level = c.get("lvl").getAsInt();
        UUID uid = UUID.fromString(c.get("uid").getAsString());

        Card resultCard = getCards().get(name).get();
        resultCard.setLevel(level);
        resultCard.setUid(uid);
        return resultCard;
    }

    /**
     * Since we store all the card data in a json string (e.g. {"name":"Creeper", "lvl":1, "uid":UUID})
     * side note: pretend the formatting is a proper json string ^
     * This function will take a card and deconstruct it into the string presented above
     * returns an empty string if the card isn't registered in the CardManager.cards
     * @return String
     */
    public static String packCardToString(Card card) {
        if (!isCard(card)) return "";
        String result = new StringBuilder()
                .append("{")
                .append("\"name\":\""+card.getName()).append("\"").append(",")
                .append("\"lvl\":"+card.getLevel()).append(",")
                .append("\"uid\":\""+card.getUid()).append("\"")
//                .append("\"name\":\""+card.getName()).append("\"").append(",")
                .append("}")
                .toString();
        return result;
    }
}
