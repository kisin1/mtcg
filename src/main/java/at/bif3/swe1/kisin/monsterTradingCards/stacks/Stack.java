package at.bif3.swe1.kisin.monsterTradingCards.stacks;

import at.bif3.swe1.kisin.monsterTradingCards.cards.Card;
import at.bif3.swe1.kisin.monsterTradingCards.cards.CardCache;

import java.util.Random;
import java.util.Vector;

public class Stack {
    private Vector<Card> stack;
    private final Random rand = new Random();

    public Stack() { this.stack = new Vector<>(); }

    public void fillStack(){
        CardCache cardCache = new CardCache();
        Object[] values = cardCache.getCachedCards().values().toArray();
        for(int i = 0; i < 20; i++){
            Card randomCard = (Card) values[rand.nextInt(values.length)];
            stack.add(randomCard);
        }
    }

    public Card randomCard(){
        int index = rand.nextInt(this.stack.size());
        Card card = this.stack.get(index);
        this.stack.remove(index);
        return card;
    }

    public Vector<Card> getStack() { return this.stack; }
}
