package at.bif3.swe1.kisin.monsterTradingCards;

import java.util.Vector;

public class Battle {
    User player1;
    User player2;
    Vector<Card> deck1;
    Vector<Card> deck2;

    int round;
    boolean winner;

    public Battle(){
        player1 = new User("Njo", "pass");
        player2 = new User("Nji", "word");
        winner = false;
    }
    public void fight(){
        deck1 = player1.chooseDeck();
        deck2 = player2.chooseDeck();
        while (!winner){


            Card card1 = player1.chooseCard();
            Card card2 = player2.chooseCard();

            printStats(card1, card2);
            effectiveFight(card1, card2);
            if(card1.getDamage() > card2.getDamage()){
                System.out.println(" => " + card1.getName() + " wins");
                deck1.add(card2);
                deck2.remove(card2);
            } else if(card1.getDamage() < card2.getDamage()){
                System.out.println(" => " + card2.getName() + " wins");
                deck2.add(card1);
                deck1.remove(card1);
            } else {
                System.out.println(" => It's a draw(No action)");
            }
            round++;
            if(round == 100){ break; }
            if(deck1.isEmpty() || deck2.isEmpty()){
                break;
            }

        }
        System.out.println("Game Over");
    }

    public void effectiveFight(Card card1, Card card2){
        elementFight(card1, card2);
        elementFight(card2, card1);
    }

    private void elementFight(Card card1, Card card2) {
        if((card1.getElementType().equals(ElementType.WATER) && card2.getElementType().equals(ElementType.FIRE))
                || (card1.getElementType().equals(ElementType.FIRE) && card2.getElementType().equals(ElementType.NORMAL))
                || (card1.getElementType().equals(ElementType.NORMAL) && card2.getElementType().equals(ElementType.WATER))){
            card1.setDamage(card1.getDamage()*2);
            card2.setDamage(card2.getDamage()/2);
            System.out.print(" => " + card1.getDamage() + " vs " + card2.getDamage());
        }
    }

    public void printStats(Card card1, Card card2){
        System.out.print(player1.getUsername() + ": " + card1.getName() + "(" + card1.getDamage() + " Damage) vs "
                            + player2.getUsername() + ": " + card2.getName() + "(" + card2.getDamage() + " Damage) ");
    }
}
