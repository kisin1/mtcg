package at.bif3.swe1.kisin.monsterTradingCards;

public abstract class Card implements Cloneable {
    private String name;
    private float damage;
    private ElementType elementType;
    private CardType cardType;

    //private int cardId;

    public Card(String name, float damage, ElementType elementType, CardType cardType){
        this.name = name;
        this.damage = damage;
        this.elementType = elementType;
        this.cardType = cardType;
    }
    protected Card(Card card){
        this.name = card.name;
        this.damage = card.damage;
        this.elementType = card.elementType;
        this.cardType = card.cardType;
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

    public abstract Card clone();
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
        return "Name: " + this.name + "\tDamage: " + this.damage;
    }
}


