package at.bif3.swe1.kisin.monsterTradingCards.cards;

import at.bif3.swe1.kisin.monsterTradingCards.enums.CardType;
import at.bif3.swe1.kisin.monsterTradingCards.enums.ElementType;
import at.bif3.swe1.kisin.monsterTradingCards.enums.MonsterType;

public class MonsterCard extends Card {
    MonsterType monsterType;

    public MonsterCard(String name, float damage, ElementType elementType, MonsterType monsterType) {
        //super(name, damage, elementType);
        this.setName(name);
        this.setDamage(damage);
        this.setElementType(elementType);
        this.setCardType(CardType.MONSTER);
        this.monsterType = monsterType;
    }
    private MonsterCard(MonsterCard card) {
        super(card);
        this.setCardType(CardType.MONSTER);
        this.monsterType = card.monsterType;
    }

    @Override
    public MonsterCard clone() {
        return new MonsterCard(this);
    }
}
