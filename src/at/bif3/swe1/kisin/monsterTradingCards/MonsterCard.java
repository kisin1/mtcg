package at.bif3.swe1.kisin.monsterTradingCards;

public class MonsterCard extends Card{
    MonsterType monsterType;

    public MonsterCard(String name, int damage, ElementType elementType, CardType cardType, MonsterType monsterType) {
        super(name, damage, elementType, cardType);
        this.monsterType = monsterType;
    }
    private MonsterCard(MonsterCard card) {
        super(card);
        this.monsterType = card.monsterType;
    }

    @Override
    public MonsterCard clone() {
        return new MonsterCard(this);
    }
}
