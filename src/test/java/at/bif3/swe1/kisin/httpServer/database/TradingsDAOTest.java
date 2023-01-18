package at.bif3.swe1.kisin.httpServer.database;

import at.bif3.swe1.kisin.httpServer.json.Json;
import at.bif3.swe1.kisin.monsterTradingCards.trade.Trade;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TradingsDAOTest {
    TradingsDAO tradingsDAO = new TradingsDAO();
    static String tradeString = "{\"Id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", \"CardToTrade\": \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Type\": \"monster\", \"MinimumDamage\": 15}";
    static Trade trade;

    @BeforeAll
    static void setUp() throws IOException {
        trade = Json.fromJson(Json.parse(tradeString), Trade.class);
    }



    @Test
    void insert() {
        trade.setOwner("username");
        tradingsDAO.insert(trade);
        assertEquals("6cd85277-4590-49d4-b0cf-ba0a921faad0", tradingsDAO.get("6cd85277-4590-49d4-b0cf-ba0a921faad0").map(t -> trade.getTradingId()).orElse("Empty"));
    }


    @Test
    void get() {
        trade.setOwner("owner");
        tradingsDAO.insert(trade);
        assertEquals("owner", tradingsDAO.get("6cd85277-4590-49d4-b0cf-ba0a921faad0").map(t -> trade.getOwner()).orElse("Empty"));
    }


    @Test
    void deleteTrade() {
        trade.setOwner("deleteTrade");
//        tradingsDAO.insert(trade);
        tradingsDAO.delete(trade);
        assertEquals("null", tradingsDAO.get("6cd85277-4590-49d4-b0cf-ba0a921faad0").map(t -> trade.getTradingId()).orElse("null"));
    }

    @Test
    void delete() {
        trade.setOwner("delete");
        tradingsDAO.insert(trade);
        tradingsDAO.delete(trade.getTradingId());
        assertEquals("null", tradingsDAO.get("6cd85277-4590-49d4-b0cf-ba0a921faad0").map(t -> trade.getTradingId()).orElse("null"));
    }
}