package art.uncertawn.mcdeckbuilder.card;

import art.uncertawn.mcdeckbuilder.card.cards.CowCard;
import art.uncertawn.mcdeckbuilder.card.cards.CreeperCard;
import art.uncertawn.mcdeckbuilder.card.cards.ZombieCard;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.UUID;

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
        int level = c.get("lvl").getAsInt();
        UUID uid = UUID.fromString(c.get("uid").getAsString());

        Card resultCard = getCards().get(c.get("name").getAsString());
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
//                .append("\"name:\""+card.getName())
//                .append("\"name:\""+card.getName())
                .append("}")
                .toString();
        return result;
    }
}
