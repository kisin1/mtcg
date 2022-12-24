package at.bif3.swe1.kisin.monsterTradingCards;


public class SpellCard extends Card{

    public SpellCard(String name, int damage, ElementType elementType){
        super(name, damage, elementType);
        cardType = CardType.SPELL;
    }

    private SpellCard(SpellCard card) {
        super(card);
        this.cardType = CardType.SPELL;
    }

    @Override
    public SpellCard clone() {
        return new SpellCard(this);
    }

}
