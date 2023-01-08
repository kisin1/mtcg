package at.bif3.swe1.kisin.monsterTradingCards;

import at.bif3.swe1.kisin.monsterTradingCards.enums.CardType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Trade {
    @JsonProperty("Id")
    private String tradingId;
    @JsonProperty("CardToTrade")
    private String cardId;
    @JsonProperty("Type")
    private CardType cardType;
    @JsonProperty("MinimumDamage")
    private float minDamage;
    private String owner;

    public Trade(){}
    public Trade(String tradingId, String cardId, CardType cardType, float minDamage, String owner) {
        this.tradingId = tradingId;
        this.cardId = cardId;
        this.cardType = cardType;
        this.minDamage = minDamage;
        this.owner = owner;
    }

    public String getTradingId() {
        return tradingId;
    }

    public void setTradingId(String tradingId) {
        this.tradingId = tradingId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public float getMinDamage() {
        return minDamage;
    }

    public void setMinDamage(float minDamage) {
        this.minDamage = minDamage;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradingId='" + tradingId + '\'' +
                ", cardId='" + cardId + '\'' +
                ", cardType=" + cardType +
                ", minDamage=" + minDamage +
                ", owner='" + owner + '\'' +
                '}';
    }
}
