package at.bif3.swe1.kisin.httpServer.json;

import at.bif3.swe1.kisin.monsterTradingCards.cards.Card;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Json {

    private static ObjectMapper objectMapper = defaultObjectMapper();

    private static ObjectMapper defaultObjectMapper(){
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        //options
        //defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return defaultObjectMapper;
    }

    //parse json string src into json node
    public static JsonNode parse(String src) throws JsonProcessingException {
        return objectMapper.readTree(src);
    }

    //from json to java object
    public static <A> A fromJson(JsonNode json, Class<A> returnClass) throws IOException {
            return objectMapper.treeToValue(json, returnClass);
    }

    public static Map<String,String> toHashMap(String json){
        Map<String, String> map = null;
        try {
            map = objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e){
            System.out.println("ERROR: Json to HashMap: " + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    public static ArrayList<Card> fromJsonToArray(JsonNode json) throws IOException {
        ArrayList<Card> dataList = new ArrayList<>();
        for(int i = 0; i < json.size(); i++){
            dataList.add(Json.fromJson(json.get(i), Card.class));
        }
        return dataList;
    }

    //from java object to json
    public static JsonNode toJson(Object a){
        return objectMapper.valueToTree(a);
    }

    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateString(node, false);
    }

    public static String prettyPrint(JsonNode node) throws JsonProcessingException {
        return generateString(node, true);
    }

    private static String generateString(JsonNode node, boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = objectMapper.writer();
        if(pretty)
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        return objectWriter.writeValueAsString(node);
    }
}
