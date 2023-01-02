package at.bif3.swe1.kisin.monsterTradingCards.enums;

public enum MonsterType {
    GOBLIN("Goblin"), DRAGON("Dragon"), WIZARD("Wizard"), ORK("Ork"), KNIGHT("Knight"), KRAKEN("Kraken"), ELF("Elf"), TROLL("Troll"), NULL("Null");

    private String monster;

    MonsterType(String monster) {
        this.monster = monster;
    }
}
