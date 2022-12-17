package at.bif3.swe1.kisin.monsterTradingCards;

public class SpellCard extends ACard{
//    private String name;
//    private int damage;
//    private ElementType elementType;
//    private CardType cardType;


    public SpellCard(SpellCard card){
        super(card);
    }

    protected SpellCard(ACard aCard) {
        super(aCard);
    }

    @Override
    public SpellCard clone() {
        return new SpellCard(this);
    }

    @Override
    public String toString(){
        return "Name: " + this.getName() + "\nDamage: " + this.getDamage();
    }
}
