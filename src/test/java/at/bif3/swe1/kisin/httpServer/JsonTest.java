package at.bif3.swe1.kisin.httpServer;

import at.bif3.swe1.kisin.httpServer.json.Json;
import at.bif3.swe1.kisin.httpServer.serializer.UserCredentials;
import at.bif3.swe1.kisin.monsterTradingCards.cards.Card;
import at.bif3.swe1.kisin.monsterTradingCards.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    private final String simpleTestCase = "{\"Username\":\"kienboec\", \"Password\":\"daniel\"}";
    private final String arrayTestCase = "[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}, {\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"Name\":\"Dragon\", \"Damage\": 50.0}, {\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"Name\":\"WaterSpell\", \"Damage\": 20.0}, {\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Name\":\"Ork\", \"Damage\": 45.0}, {\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"Name\":\"FireSpell\", \"Damage\": 25.0}]";

    private final String userTestCase = "{\"Username\":\"kienboec\", \"Password\":\"daniel\", \"Elo\":\"67\", \"Coins\":\"15\"}";
    @Test
    void parse() throws JsonProcessingException {
        JsonNode node = Json.parse(simpleTestCase);
        assertEquals("kienboec", node.get("Username").asText());
    }

    @Test
    void parseArray() throws JsonProcessingException {
        JsonNode node = Json.parse(arrayTestCase);
        assertEquals("845f0dc7-37d0-426e-994e-43fc3ac83c08", node.get(0).get("Id").asText());
        assertEquals("99f8f8dc-e25e-4a95-aa2c-782823f36e2a", node.get(1).get("Id").asText());
        assertEquals("e85e3976-7c86-4d06-9a80-641c2019a79f", node.get(2).get("Id").asText());
        assertEquals("1cb6ab86-bdb2-47e5-b6e4-68c5ab389334", node.get(3).get("Id").asText());
        assertEquals("dfdd758f-649c-40f9-ba3a-8657f4b3439f", node.get(4).get("Id").asText());
    }

    @Test
    void fromJson() throws IOException {
        JsonNode node = Json.parse(simpleTestCase);
        UserCredentials userCredentials = Json.fromJson(node, UserCredentials.class);
        assert userCredentials != null;
        assertEquals(userCredentials.getUsername(), "kienboec");
    }

    @Test
    void fromJsonArray() throws IOException {
        JsonNode node = Json.parse(arrayTestCase);
        ArrayList<Card> cardDataList = Json.fromJsonToArray(node);
        assertEquals("845f0dc7-37d0-426e-994e-43fc3ac83c08", cardDataList.get(0).getCardId());
        assertEquals("99f8f8dc-e25e-4a95-aa2c-782823f36e2a", cardDataList.get(1).getCardId());
        assertEquals("e85e3976-7c86-4d06-9a80-641c2019a79f", cardDataList.get(2).getCardId());
        assertEquals("1cb6ab86-bdb2-47e5-b6e4-68c5ab389334", cardDataList.get(3).getCardId());
        assertEquals("dfdd758f-649c-40f9-ba3a-8657f4b3439f", cardDataList.get(4).getCardId());


    }
    @Test
    void toJson(){
        UserCredentials userCred = new UserCredentials();
        userCred.setUsername("testUser");
        userCred.setPassword("testPassword");
        JsonNode node = Json.toJson(userCred);
        assertEquals("testUser", node.get("Username").asText());
    }

    @Test
    void prettyPrint() {
        UserCredentials userCred = new UserCredentials();
        userCred.setUsername("testUser");
        JsonNode node = Json.toJson(userCred);
        assertEquals("testUser", node.get("Username").asText());
    }

    @Test
    void hashMapTest(){
        Map<String, String> hashMap;
        hashMap = Json.toHashMap(simpleTestCase);
        assertEquals("daniel", hashMap.get("Password"));
    }

    @Test
    void fromJsonToUser() throws IOException {
        JsonNode jsonNode = Json.parse(userTestCase);
        User user = Json.fromJson(jsonNode, User.class);
        assertEquals("kienboec", user.getUsername());
        assertEquals(67, user.getElo());
    }

}