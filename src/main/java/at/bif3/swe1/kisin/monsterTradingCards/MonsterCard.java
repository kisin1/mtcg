package at.bif3.swe1.kisin.monsterTradingCards;

public class MonsterCard extends Card{
    MonsterType monsterType;

    public MonsterCard(String name, int damage, ElementType elementType, MonsterType monsterType) {
        super(name, damage, elementType);
        this.cardType = CardType.MONSTER;
        this.monsterType = monsterType;
    }
    private MonsterCard(MonsterCard card) {
        super(card);
        this.cardType = CardType.MONSTER;
        this.monsterType = card.monsterType;
    }

    @Override
    public MonsterCard clone() {
        return new MonsterCard(this);
    }
}
