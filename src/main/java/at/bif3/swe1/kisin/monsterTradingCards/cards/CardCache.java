package at.bif3.swe1.kisin.monsterTradingCards.cards;

import at.bif3.swe1.kisin.monsterTradingCards.enums.ElementType;
import at.bif3.swe1.kisin.monsterTradingCards.enums.MonsterType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardCache {
    private final Map<String, Card> cache = new HashMap<>();

    public CardCache(){
        SpellCard spellCard1 = new SpellCard("WatterSpell", 50, ElementType.WATER);
        SpellCard spellCard2 = new SpellCard("FireSpell", 65, ElementType.FIRE);
        SpellCard spellCard3 = new SpellCard("RegularSpell", 35, ElementType.NORMAL);

        MonsterCard monsterCard1 = new MonsterCard("Knight", 45, ElementType.NORMAL, MonsterType.KNIGHT);
        MonsterCard monsterCard2 = new MonsterCard("Goblin", 70, ElementType.WATER, MonsterType.GOBLIN);
        MonsterCard monsterCard3 = new MonsterCard("Dragon", 75, ElementType.FIRE, MonsterType.DRAGON);
        MonsterCard monsterCard4 = new MonsterCard("Wizard", 60, ElementType.NORMAL, MonsterType.WIZARD);
        MonsterCard monsterCard5 = new MonsterCard("Troll", 55, ElementType.WATER, MonsterType.TROLL);
        MonsterCard monsterCard6 = new MonsterCard("Elf", 35, ElementType.NORMAL, MonsterType.ELF);
        MonsterCard monsterCard7 = new MonsterCard("Kraken", 60, ElementType.WATER, MonsterType.KRAKEN);
        MonsterCard monsterCard8 = new MonsterCard("Ork", 40, ElementType.FIRE, MonsterType.ORK);


        cache.put("WatterSpell", spellCard1);
        cache.put("FireSpell", spellCard2);
        cache.put("RegularSpell", spellCard3);
        cache.put("Knight", monsterCard1);
        cache.put("Goblin", monsterCard2);
        cache.put("Dragon", monsterCard3);
        cache.put("Wizard", monsterCard4);
        cache.put("Troll", monsterCard5);
        cache.put("Elf", monsterCard6);
        cache.put("Kraken", monsterCard7);
        cache.put("Ork", monsterCard8);
    }

    public Card getCard(String key){
        Card cachedCard = cache.get(key);
        return cachedCard.clone();
    }

    public void print(){
        for (String s : cache.keySet()){
            System.out.println("Key: " + s + " Value: " + cache.get(s));
        }
    }

    public HashMap<String, Card> getCachedCards(){
        return (HashMap<String, Card>) cache;
    }

    public void put(List<Card> cards){
        cards.forEach(card -> cache.put(card.getName(), card));
    }
}
