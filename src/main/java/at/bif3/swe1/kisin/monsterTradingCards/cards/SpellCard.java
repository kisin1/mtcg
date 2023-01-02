package at.bif3.swe1.kisin.monsterTradingCards.cards;


import at.bif3.swe1.kisin.monsterTradingCards.enums.CardType;
import at.bif3.swe1.kisin.monsterTradingCards.enums.ElementType;

public class SpellCard extends Card {

    public SpellCard(String name, int damage, ElementType elementType){
        //super(name, damage, elementType);
        this.setCardType(CardType.SPELL);
    }

    private SpellCard(SpellCard card) {
        super(card);
        this.setCardType(CardType.SPELL);
    }

    @Override
    public SpellCard clone() {
        return new SpellCard(this);
    }

}
