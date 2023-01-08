package at.bif3.swe1.kisin.monsterTradingCards.cards;

import at.bif3.swe1.kisin.monsterTradingCards.enums.CardType;
import at.bif3.swe1.kisin.monsterTradingCards.enums.ElementType;
import at.bif3.swe1.kisin.monsterTradingCards.enums.MonsterType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Card implements Cloneable {
    @JsonProperty("Id")
    private String cardId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Damage")
    private float damage;
    private String owner;
    private float calcDamage;
    private ElementType elementType;
    private CardType cardType;
    private MonsterType monsterType;
    private boolean inDeck;
    private int packageId;


    public Card(String cardId, String name, float damage, ElementType elementType){
        this.cardId = cardId;
        this.name = name;
        this.damage = damage;
        this.elementType = elementType;
    }

    public Card(String cardId, String name, float damage){
        this.cardId = cardId;
        this.name = name;
        processCard();
        this.damage = damage;
    }
    private MonsterType parseName(String name){
        if(!name.contains("Spell")){
            if(name.contains("Water")){
                return MonsterType.valueOf(name.replace("Water", "").toUpperCase());
            } else if(name.contains("Fire")){
                return MonsterType.valueOf(name.replace("Fire", "").toUpperCase());
            } else {
                return MonsterType.valueOf(name.toUpperCase());
            }
        }
        return null;
    }

    public Card(String cardId, String name, MonsterType monsterType, ElementType elementType, float damage, String owner, boolean inDeck, int packageId  ){
        this.cardId = cardId;
        this.name = name;
        this.monsterType = monsterType;
        this.elementType = elementType;
        this.damage = damage;
        this.owner = owner;
        this.inDeck = inDeck;
        this.packageId = packageId;
    }
    protected Card(Card card){
        this.cardId = card.cardId;
        this.name = card.name;
        this.damage = card.damage;
        this.elementType = card.elementType;
        this.cardType = card.cardType;
    }

    public Card() {
    }

    public void processCard(){
        if(this.name.contains("Fire")){
            this.elementType = ElementType.FIRE;
        } else if (this.name.contains("Water")){
            this.elementType = ElementType.WATER;
        } else {
            this.elementType = ElementType.NORMAL;
        }
        this.cardType = (name.contains("Spell") ? CardType.SPELL : CardType.MONSTER);
        this.monsterType = parseName(this.name);
    }
    public void setName(String name){
        this.name = name;
    }

    public void setDamage(float damage){
        this.damage = damage;
    }

    public String getName(){
        return this.name;
    }

    public float getDamage(){
        return this.damage;
    }

    public void setCalcDamage(float damage){ this.calcDamage = damage; }

    public float getCalcDamage() { return calcDamage; }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Card clone() {
        return null;
    }
//    public Object clone(){
//        Object clone = null;
//
//        try {
//            clone = super.clone();
//        } catch (CloneNotSupportedException e){
//            e.printStackTrace();
//        }
//        return clone;
//    }

    public ElementType getElementType(){
        return this.elementType;
    }

    @Override
    public String toString() {
        return "\nCard{" +
                "cardId='" + cardId + '\'' +
                ", name='" + name + '\'' +
                ", damage=" + damage +
                ", owner='" + owner + '\'' +
                ", elementType=" + elementType +
                ", cardType=" + cardType +
                ", monsterType=" + monsterType +
                ", inDeck=" + inDeck +
                '}';
    }

//    public String formatPlain() {
//        return "\n{" +
//                "cardId='" + cardId + '\'' +
//                "\nname='" + name + '\'' +
//                "\ndamage=" + damage +
//                "\nowner='" + owner + '\'' +
//                "\nelementType=" + elementType +
//                "\ncardType=" + cardType +
//                "\nmonsterType=" + monsterType +
//                "\ninDeck=" + inDeck +
//                '}';
//    }

    //    @Override
//    public String toString() {
//        return "\nId: " + this.cardId + "\tName: " + this.name + "\tDamage: " + this.damage;
//    }

    public MonsterType getMonsterType() { return this.monsterType; }

    public String getOwner() { return this.owner; }

    public boolean getInDeck() { return this.inDeck; }

    public int getPackageId() { return this.packageId; }


    public void setPackageId(int i) { this.packageId = i; }
    public void setOwner(String owner){ this.owner = owner; }
}


