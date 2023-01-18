package at.bif3.swe1.kisin.monsterTradingCards.battle;


import at.bif3.swe1.kisin.httpServer.database.CardDAO;
import at.bif3.swe1.kisin.httpServer.database.UserDAO;
import at.bif3.swe1.kisin.monsterTradingCards.user.User;
import at.bif3.swe1.kisin.monsterTradingCards.cards.Card;
import at.bif3.swe1.kisin.monsterTradingCards.enums.ElementType;
import at.bif3.swe1.kisin.monsterTradingCards.stacks.Deck;

import java.util.ArrayList;
import java.util.List;

public class Battle {
    User player1;
    User player2;
    CardDAO cardDAO = new CardDAO();
    UserDAO userDAO = new UserDAO();

    int rounds;
    boolean winner;

    public Battle() {
        player1 = new User("usr1", "pass");
        player2 = new User("usr2", "word");
        winner = false;
    }

    public Battle(User player1, User player2, int rounds) {
        this.player1 = player1;
        this.player2 = player2;
        this.rounds = rounds;
        this.player1.setUserDeck(player1.getUserDeck());
        this.player2.setUserDeck(player2.getUserDeck());
        winner = false;
    }

    public List<String> start() {
        List<String> result = new ArrayList<>();
        int pl1RoundsWon = 0;
        int pl2RoundsWon = 0;

        while (true) {
            String log = "";
            Card card1 = player1.getUserDeck().chooseCard();
            Card card2 = player2.getUserDeck().chooseCard();

            log += battleLog(card1, card2);
            log += effectiveFight(card1, card2);
            if (card1.getCalcDamage() > card2.getCalcDamage()) {
                log += (" => ");
                log += card1.getName();
                log += " wins";

                player1.getUserDeck().add(card2);
                player1.eloUp();
                player2.eloDown();
                pl1RoundsWon++;
                //cardDAO.updateOwner(player1.getUsername(), card2.getCardId());
                player2.getUserDeck().remove(card2);
            } else if (card1.getCalcDamage() < card2.getCalcDamage()) {
                log += (" => ");
                log += card2.getName();
                log += " wins";

                player2.getUserDeck().add(card1);
                player2.eloUp();
                player2.eloDown();
                pl2RoundsWon++;
                //cardDAO.updateOwner(player2.getUsername(), card1.getCardId());
                player1.getUserDeck().remove(card1);
            } else {
                log += (" => It's a draw(No action)");
            }
            result.add(log);
            rounds++;
            if (rounds == 100) {
                break;
            }
            if (player1.getUserDeck().isEmpty() || player2.getUserDeck().isEmpty()) {
                break;
            }

        }
        player1.setRoundsWon(pl1RoundsWon);
        player2.setRoundsWon(pl2RoundsWon);
        player1.setRoundsPlayed(rounds);
        player2.setRoundsPlayed(rounds);
        result.add("========Game Over========");
//        userDAO.updateElo(player1.getUsername(), player1.getElo());
//        userDAO.updateElo(player2.getUsername(), player2.getElo());
       userDAO.afterBattleUpdate(player1);
       userDAO.afterBattleUpdate(player2);
        return result;
    }

    public String effectiveFight(Card card1, Card card2) {
        String result1 = elementFight(card1, card2);
        String result2 = elementFight(card2, card1);
        return (result1.isEmpty() ? result2 : result1);
    }

    private String elementFight(Card card1, Card card2) {
        if ((card1.getElementType().equals(ElementType.WATER) && card2.getElementType().equals(ElementType.FIRE))
                || (card1.getElementType().equals(ElementType.FIRE) && card2.getElementType().equals(ElementType.NORMAL))
                || (card1.getElementType().equals(ElementType.NORMAL) && card2.getElementType().equals(ElementType.WATER))) {
            card1.setCalcDamage(card1.getDamage() * 2);
            card2.setCalcDamage(card2.getDamage() / 2);
            return (" => " + card1.getCalcDamage() + " vs " + card2.getCalcDamage());
        }
        return "";
    }

    public String battleLog(Card card1, Card card2) {
        return (player1.getUsername() + ": " + card1.getName() + "(" + card1.getDamage() + " Damage) vs "
                + player2.getUsername() + ": " + card2.getName() + "(" + card2.getDamage() + " Damage) ");
    }
}
