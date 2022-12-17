package com.monsterTradingCards;
import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private int coins;
    private ArrayList<Card> stack;
    private ArrayList<Card> deck;
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.coins = 20;
    }

    public void printUser(){
        System.out.println(this.username);
        System.out.println(this.password);
    }
}
