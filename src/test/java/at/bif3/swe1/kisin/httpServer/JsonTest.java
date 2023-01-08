package at.bif3.swe1.kisin.httpServer;

import at.bif3.swe1.kisin.httpServer.json.Json;
import at.bif3.swe1.kisin.httpServer.serializer.UserCredentials;
import at.bif3.swe1.kisin.monsterTradingCards.cards.Card;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    private final String simpleTestCase = "{\"Username\":\"kienboec\", \"Password\":\"daniel\"}";
    private final String arrayTestCase = "[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}, {\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"Name\":\"Dragon\", \"Damage\": 50.0}, {\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"Name\":\"WaterSpell\", \"Damage\": 20.0}, {\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Name\":\"Ork\", \"Damage\": 45.0}, {\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"Name\":\"FireSpell\", \"Damage\": 25.0}]";

    private final String stringifyTest = "\"951e886a-0fbf-425d-8df5-af2ee4830d85\"";

    @Test
    void parse() throws JsonProcessingException {

        JsonNode node = Json.parse(simpleTestCase);
        assertEquals(node.get("Username").asText(), "kienboec");
    }

    @Test
    void parseArray() throws JsonProcessingException {
        JsonNode node = Json.parse(arrayTestCase);
        for(int i = 0; i < node.size(); i++){
            System.out.println(i + ": " + node.get(i));
        }
        assertEquals(node.get(0).get("Id").asText(), "845f0dc7-37d0-426e-994e-43fc3ac83c08");
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
        Card cardData = (Card) cardDataList.get(0);
        assertEquals(cardData.getCardId(), "845f0dc7-37d0-426e-994e-43fc3ac83c08");
    }
    @Test
    void toJson(){
        UserCredentials userCred = new UserCredentials();
        userCred.setUsername("NjoNjo");
        userCred.setPassword("test");
        JsonNode node = Json.toJson(userCred);
        assertEquals(node.get("Username").asText(), "NjoNjo");
    }

    @Test
    void stringify() throws JsonProcessingException {
//        UserCredentials userCred = new UserCredentials();
//        userCred.setUsername("NjoNjo");
//        JsonNode node = Json.toJson(userCred);
//        Json.stringify(node);
//        assertEquals(node.get("Username").asText(), "NjoNjo");
        String testCase = Json.jsonToString(stringifyTest);
        System.out.println(testCase);
        //String copy = testCase.substring(1, testCase.length() -1);
        //System.out.println(copy);
    }

    @Test
    void prettyPrint() throws JsonProcessingException {
        UserCredentials userCred = new UserCredentials();
        userCred.setUsername("NjoNjo");
        JsonNode node = Json.toJson(userCred);
        System.out.println(Json.prettyPrint(node));
        assertEquals(node.get("Username").asText(), "NjoNjo");
    }

}