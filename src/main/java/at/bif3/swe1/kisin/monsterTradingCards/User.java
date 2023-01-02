package at.bif3.swe1.kisin.monsterTradingCards;

import at.bif3.swe1.kisin.monsterTradingCards.cards.Card;
import at.bif3.swe1.kisin.monsterTradingCards.stacks.Deck;
import at.bif3.swe1.kisin.monsterTradingCards.stacks.Stack;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Vector;

public class User {
    private int user_id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("Bio")
    private String bio;
    @JsonProperty("Image")
    private String image;
    private String token;
    private boolean isAdmin;
    private int coins;
    private int elo;
    private Stack stack;
    private Deck deck;

    public User(){ this.username = "Unknown"; this.password = "Unknown"; }
    public User(int user_id, String username, String password, String bio, String token, String image, int elo, int coins, boolean isAdmin, String name) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.token = token;
        this.image = image;
        this.elo = elo;
        this.coins = coins;
        this.isAdmin = isAdmin;
        this.name = name;
        this.stack = new Stack();
        this.deck = new Deck();
    }
    //for testing
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.coins = 20;
        this.stack = new Stack();
        stack.fillStack();
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = Math.max(coins, 0);
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public Stack getStack() {
        return this.stack;
    }

    public Vector<Card> getDeck() {
        return deck.getDeck();
    }

    public void setDeck(Vector<Card> deck) {
        this.deck.setDeck(deck);
    }

    @Override
    public String toString() {
        return "User: " + username + " {" +
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", bio='" + bio + '\'' +
                ", image='" + image + '\'' +
                ", token='" + token + '\'' +
                ", isAdmin=" + isAdmin +
                ", coins=" + coins +
                ", elo=" + elo +
                '}';
    }
}
