package at.bif3.swe1.kisin.monsterTradingCards;

public enum ElementType {
    WATER("Water"), FIRE("Fire"), NORMAL("Regular");

    private String type;
    ElementType(String type) {
        this.type = type;
    }
}
