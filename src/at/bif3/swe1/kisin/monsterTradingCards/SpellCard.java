package at.bif3.swe1.kisin.monsterTradingCards;


public class SpellCard extends Card{

    public SpellCard(String name, int damage, ElementType elementType, CardType cardType){
        super(name, damage, elementType, cardType);
    }

    private SpellCard(SpellCard card) {
        super(card);
    }

    @Override
    public SpellCard clone() {
        return new SpellCard(this);
    }

}
