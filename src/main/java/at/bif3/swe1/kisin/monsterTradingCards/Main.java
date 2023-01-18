package at.bif3.swe1.kisin.monsterTradingCards;

import at.bif3.swe1.kisin.monsterTradingCards.battle.Battle;
import at.bif3.swe1.kisin.monsterTradingCards.stacks.Deck;
import at.bif3.swe1.kisin.monsterTradingCards.user.User;

public class Main {
    public static void main(String[] args) {
        //User player1 =
        Battle battle = new Battle();
        battle.start();
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();
//        deck1 = this.player1.getUserDeck();
//        deck2 = this.player2.getUserDeck();

//        User player;
//        player = new User("Player", "player1");
//        System.out.println(player);
//        System.out.println("===============================");
//
//        List<Card> cards = List.of(
//                new SpellCard("FireSpell", 65, ElementType.FIRE, CardType.SPELL),
//                new MonsterCard("WatterGoblin", 45, ElementType.WATER, CardType.MONSTER, MonsterType.GOBLIN)
//        );
//
//        List<Card> copyList = new ArrayList<>();
//        for (Card card : cards){
//            copyList.add(card.clone());
//        }
//
//        copyList.forEach(System.out::println);
//
//        System.out.println("===================================");
//
//        CardCache registry = new CardCache();
//        registry.put(cards);
//        registry.print();

    }

}