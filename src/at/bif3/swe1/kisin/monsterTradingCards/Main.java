package com.monsterTradingCards;
import com.monsterTradingCards.User;
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        User player;
        player = new User("Player", "player1");
        player.printUser();
    }

}