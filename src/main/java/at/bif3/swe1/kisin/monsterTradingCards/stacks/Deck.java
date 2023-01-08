package at.bif3.swe1.kisin.monsterTradingCards.stacks;

import at.bif3.swe1.kisin.monsterTradingCards.User;
import at.bif3.swe1.kisin.monsterTradingCards.cards.Card;

import java.util.Random;
import java.util.Vector;

public class Deck {
    private Vector<Card> deck;
    private final int SIZE = 4;
    private final Random rand = new Random();
    public Deck(){ this.deck = new Vector<>(); }
    public Deck(User user){
        this.deck = new Vector<>();
        for(int i = 0; i < SIZE; i++){
            deck.add(user.getStack().randomCard());
        }
    }

    public void add(Card card) {
        deck.add(card);
    }

    public void remove(Card card) {
        deck.remove(card);
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public Card chooseCard(){
        int index = rand.nextInt(deck.size());
        return deck.elementAt(index);
//        System.out.println(this.username + " choose a card to play the next round!");
//        listDeck();
//        Scanner in = new Scanner(System.in);
//
//        int choice = in.nextInt();
//        return switch (choice) {
//            case 1 -> deck.elementAt(0);
//            case 2 -> deck.elementAt(1);
//            case 3 -> deck.elementAt(2);
//            case 4 -> deck.elementAt(3);
//            default -> null;
//        };
    }

    public void  listDeck(){
        System.out.println("Your deck has the following cards: ");
        for(int i = 1; i < 5; i++){
            System.out.println(i + ". " + deck.elementAt(i-1));
        }
    }

    public Vector<Card> getDeck() {
        return deck;
    }

    public void setDeck(Vector<Card> deck) {
        this.deck = deck;
    }
}
