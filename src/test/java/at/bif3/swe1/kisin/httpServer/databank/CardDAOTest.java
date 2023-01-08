package at.bif3.swe1.kisin.httpServer.databank;

import at.bif3.swe1.kisin.httpServer.json.Json;
import at.bif3.swe1.kisin.monsterTradingCards.User;
import at.bif3.swe1.kisin.monsterTradingCards.cards.Card;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardDAOTest {

    java.lang.String request =   "[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}, {\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"Name\":\"Dragon\", \"Damage\": 50.0}, {\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"Name\":\"WaterSpell\", \"Damage\": 20.0}, {\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Name\":\"Ork\", \"Damage\": 45.0}, {\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"Name\":\"FireSpell\",    \"Damage\": 25.0}]";

    CardDAO cardDAO = new CardDAO();

    @Test
    void get() {
        System.out.println(cardDAO.get("845f0dc7-37d0-426e-994e-43fc3ac83c08").get());
    }

    @Test
    void getAll() {
        java.lang.String username = "kienboec";
        int min = cardDAO.getMin();
        System.out.println("Min is " + min);
        List<Card> cards = cardDAO.getAll(min);
        //check if user has enough coins
        UserDAO userDAO = new UserDAO();
        User user = userDAO.get(username).get();
        if(user.getCoins() > 0){
            for(Card card : cards){
                //update owner and package_id
                cardDAO.update(card, new java.lang.String[]{username, "0"});
            }
            user.setCoins(user.getCoins() - 5);
            userDAO.updateCoins(user, new java.lang.String[]{java.lang.String.valueOf(user.getCoins())});
            System.out.println("User " + username + " bought a package");
        } else {
            System.out.println("ERROR: User " + username + " does not have enough coins");
        }
    }

    @Test
    void insert() {
        Card card = new Card("b1c3-47ac-b9a3-91deab093531","WaterGoblin", 11.0F);
        cardDAO.insert(card);
        assertEquals(cardDAO.get("b1c3-47ac-b9a3-91deab093531").map(Card::getCardId).orElse("null"), card.getCardId());
    }

    @Test
    void delete() throws IOException {
        System.out.println(Json.parse( "[ {\"cardId\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"name\":\"WaterGoblin\", \"damage\":\"10.0\", \"owner\":\"kienboec\", \"elementType\":\"WATER\", \"cardType\":\"null\", \"monsterType\":\"GOBLIN\", \"inDeck\":\"true\"}, {\"cardId\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"name\":\"WaterGoblin\", \"damage\":\"10.0\", \"owner\":\"kienboec\", \"elementType\":\"WATER\", \"cardType\":\"null\", \"monsterType\":\"GOBLIN\", \"inDeck\":\"true\"}]"));

    }
}