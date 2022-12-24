package at.bif3.swe1.kisin.monsterTradingCards;

import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class User {
    private String username;
    private String password;
    private int coins;
    private int elo;
    private Vector<Card> stack;
    private Vector<Card> deck;
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.coins = 20;
        stack = new Vector<>();
        fillStack();
    }

    private void fillStack(){
        Random rand = new Random();
        CardCache cardCache = new CardCache();
        Object[] values = cardCache.getCachedCards().values().toArray();
        for(int i = 0; i < 20; i++){
            Card randomCard = (Card) values[rand.nextInt(values.length)];
            stack.add(randomCard);
        }
    }

    public void printStack(){
        for(Card card : stack){
            System.out.println(card);
        }
    }

    public Vector<Card> chooseDeck(){
        deck = new Vector<>();
        Random rand = new Random();
        int limit = 20;
        for(int i = 0; i < 4; i++){
            int index = rand.nextInt(limit);
            deck.add(stack.elementAt(index));
            stack.removeElementAt(index);
            limit--;
        }
        return deck;
    }

    public void  listDeck(){
        System.out.println("Your deck has the following cards: ");
        for(int i = 1; i < 5; i++){
            System.out.println(i + ". " + deck.elementAt(i-1));
        }
    }

    public Card chooseCard(){
        Random rand = new Random();
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

    public String getUsername(){ return this.username; }

    @Override
    public String toString(){
        return "Username: " + this.username + "\nPassword: " + this.password;
    }
}
