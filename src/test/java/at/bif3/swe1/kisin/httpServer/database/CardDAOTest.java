package at.bif3.swe1.kisin.httpServer.database;

import at.bif3.swe1.kisin.httpServer.json.Json;
import at.bif3.swe1.kisin.monsterTradingCards.cards.Card;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardDAOTest {

    CardDAO cardDAO = new CardDAO();


    @Test
    void getAll() {
        Card card1 = new Card("b1c3-47ac-b9a3-91deab2423432","WaterGoblin", 11.0F);
        Card card2 = new Card("b1c3-47ac-b9a3-91dea24254651","FireDragon", 15.0F);
        Card card3 = new Card("b1c3-47ac-b9a3-91rge5353f331","Knight", 23.0F);
        cardDAO.insert(card1);
        cardDAO.insert(card2);
        cardDAO.insert(card3);
        List<Card> cardList = (List<Card>) cardDAO.getAll();
        assertEquals(card1.getCardId(), cardList.get(0).getCardId());
        assertEquals(card2.getCardId(), cardList.get(1).getCardId());
        assertEquals(card3.getCardId(), cardList.get(2).getCardId());

    }

    @Test
    void insert() {
        Card card = new Card("b1c3-47ac-b9a3-91deab093531","WaterGoblin", 11.0F);
        cardDAO.insert(card);
        assertEquals(card.getCardId(), cardDAO.get("b1c3-47ac-b9a3-91deab093531").map(Card::getCardId).orElse("null"));
    }

    @Test
    void delete() {
        Card card = new Card("845f0dc7-37d0-426e-994e-43fc3ac83c043","WaterGoblin", 11.0F);
        cardDAO.insert(card);
        cardDAO.delete(card);
        assertEquals("null", cardDAO.get("845f0dc7-37d0-426e-994e-43fc3ac83c043").map(Card::getCardId).orElse("null"));

    }
}