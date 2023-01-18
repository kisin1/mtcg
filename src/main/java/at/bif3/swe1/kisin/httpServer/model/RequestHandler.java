package at.bif3.swe1.kisin.httpServer.model;

import at.bif3.swe1.kisin.httpServer.database.CardDAO;
import at.bif3.swe1.kisin.httpServer.database.SessionDAO;
import at.bif3.swe1.kisin.httpServer.database.TradingsDAO;
import at.bif3.swe1.kisin.httpServer.database.UserDAO;
import at.bif3.swe1.kisin.httpServer.enums.ContentType;
import at.bif3.swe1.kisin.httpServer.enums.Method;
import at.bif3.swe1.kisin.httpServer.enums.StatusCode;
import at.bif3.swe1.kisin.httpServer.json.Json;
import at.bif3.swe1.kisin.monsterTradingCards.battle.Battle;
import at.bif3.swe1.kisin.monsterTradingCards.cards.Card;
import at.bif3.swe1.kisin.monsterTradingCards.trade.Trade;
import at.bif3.swe1.kisin.monsterTradingCards.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class RequestHandler implements Runnable {

    private final Socket connection;
    private Response response;
    private Request request;

    private static final UserDAO userDAO = new UserDAO();
    private static final CardDAO cardDAO = new CardDAO();
    private static final TradingsDAO tradingsDAO = new TradingsDAO();
    private List<User> players;

    private static int i = 0; //package_id

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            //message reader
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            in = new BufferedReader(inputStreamReader);
            //message writer
            OutputStream outputStream = connection.getOutputStream();
            out = new PrintWriter(outputStream);

            //receive and parse request
            request = new RequestParser().parseRequest(in);
            System.out.println(request);
            handleResponse();

            //System.out.println(response);
            out.write(response.get());
//            out.write(new Response(StatusCode.OK, ContentType.APPLICATION_JSON, "hola" + request.getBody()).get());
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    connection.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isValid(String firstString) {
        String[] validTables = {"users", "sessions", "packages", "transactions", "cards", "deck", "stats", "score", "battles", "tradings"};
        return Arrays.asList(validTables).contains(firstString);
    }

    private void handleUsers(String username) throws IOException {
        switch (request.getMethod()) {
            case POST -> signUp();
            case GET -> getUser(username);
            case PUT -> editUser();
            default -> this.response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "Method not implemented");
        }
    }

    private void signUp() throws IOException {
        if (!userExists()) {
            User user = Json.fromJson(Json.parse(request.getBody()), User.class);
            userDAO.insert(user);
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "INFO: User " + user.getUsername() + " signed up.");
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: User already exists");
        }
    }

    private boolean userExists() {
        String username = null;
        if (!request.getAuthorizationToken().isEmpty()) {
            username = getUsernameFromToken();
        } else if (request.getBody() != null) {
            username = request.parseBody().get("Username");
        }
        return userDAO.get(username).isPresent();
    }

    private void editUser() {
        String username = getUsernameFromToken();
        if (userDAO.get(username).isPresent() && request.getPath().get(1).equals(username) && isSignedIn(username)) {
            User user = userDAO.get(username).get();

            userDAO.update(user, new String[]{user.getUsername(), user.getPassword(),
            request.parseBody().get("Bio"), user.getToken(), request.parseBody().get("Image"), String.valueOf(user.getElo()),
            String.valueOf(user.getCoins()), String.valueOf(user.isAdmin()), request.parseBody().get("Name")});
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "INFO: User " + user.getUsername() + " is updated.");
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: No user with those credentials");
        }
    }

    private void getUser(String username) {
        if (request.getPath().get(1).equals(username) && isSignedIn(username)) {
            if (userDAO.get(username).isPresent()) {
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, userDAO.get(username).get().toString());
            } else {
                response = new Response(StatusCode.NO_CONTENT, ContentType.TEXT_PLAIN, "INFO: No user with these credentials");
            }
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: User not signed in");
        }
    }

    private boolean isSignedIn(String username) {
        SessionDAO sessionDAO = new SessionDAO();
        return sessionDAO.get(username + "-mtcgToken").isPresent();
    }

    private String getUsernameFromToken() {
        if (!request.getAuthorizationToken().isEmpty()) {
            String[] authorizationToken = request.getAuthorizationToken().split(" ");
            String[] username = authorizationToken[1].split("-");
            return username[0];
        }
        return null;
    }

    private void handlePackages() throws IOException {
        i++;
        if (userExists() && isSignedIn("admin") && request.getMethod().equals(Method.POST)) {
            ArrayList<Card> packages = Json.fromJsonToArray(Json.parse(request.getBody()));
            for (Card card : packages) {
                if (!cardExists(card)) {
                    card.processCard();
                    card.setPackageId(i);
                    cardDAO.insert(card);
                } else {
                    response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "Package already exists");
                }
            }
            if (response == null) {
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "Package with id: " + i + " added");
            }
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "INFO: User does not exist or is not admin");
        }
    }

    private boolean cardExists(Card card) {
        return cardDAO.get(card.getCardId()).isPresent();
    }

    private String getPassword(String username) {
        if (userDAO.get(username).isPresent()) {
            return userDAO.get(username).map(User::getPassword).get();
        } else if (request.getBody() != null && !request.getPath().get(0).equals("packages")) {
            return request.parseBody().get("Password");
        }
        return "";
    }

    private void handleResponse() throws IOException {
        if (!isValid(request.getPath().get(0))) {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: Invalid path");
        } else {
            String username = getUsername();
            String password = getPassword(username);
            String path = ((request.getPath() != null) ? request.getPath().get(0) : "");
            switch (path) {
                case "users" -> handleUsers(username);
                case "sessions" -> logIn(username, password);
                case "packages" -> handlePackages();
                case "transactions" -> handleTransactions(username);
                case "cards" -> handleCards(username);
                case "deck" -> handleDeck(username);
                case "stats" -> handleStats(username);
                case "score" -> handleScore(username);
                case "battles" -> handleBattle(username);
                //TODO: Save user queue in the database
                case "tradings" -> handleTradings(username); //TODO: when card for trade is -> not in use
                default -> response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "Bad request");
            }
        }
    }

    private String getUsername() {
        if (request.getAuthorizationToken().length() != 0) {
            return getUsernameFromToken();
        } else if (request.getBody() != null) {
            return request.parseBody().get("Username");
        }
        return "";
    }

    private void logIn(String username, String password) {
        if (userDAO.get(username).isPresent()) {
            User user = userDAO.get(username).get();
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                SessionDAO sessionDAO = new SessionDAO();
                if (sessionDAO.get(username + "-mtcgToken").isEmpty()) {
                    sessionDAO.insert(username + "-mtcgToken");
                    response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "INFO: User " + username + " logged in.");
                } else {
                    response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "ERROR: User " + username + " already logged in.");
                }
            } else {
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "ERROR: Wrong password or username.");
            }
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: No such user. Please sign up.");
        }
    }

    private void handleTransactions(String username) {
        if (request.getMethod().equals(Method.POST) && request.getPath().get(1).equals("packages")) {
            if (isSignedIn(username)) {
                buyPackage(username);
            } else {
                response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "User " + username + " is not logged in");
            }
        }
    }

    private void buyPackage(String username) {
        List<Card> cards = cardDAO.getPackage();
        //check if user has enough coins
        User user = userDAO.get(username).get();
        if (!cards.isEmpty()) {
            if (user.getCoins() > 0) {
                for (Card card : cards) {
                    //update owner and package_id
                    cardDAO.update(card, new String[]{username, "0"});
                }
                user.setCoins(user.getCoins() - 5);
                userDAO.updateCoins(user, new String[]{String.valueOf(user.getCoins())});
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "User " + username + " bought a package");
            } else {
                response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: User " + username + " does not have enough coins");
            }
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: No more available packages");
        }
    }

    private void handleCards(String username) {
        if (!username.isEmpty() && request.getMethod().equals(Method.GET)) {
            if (isSignedIn(username)) {
                Vector<Card> cards = cardDAO.getAllCards(username);
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, cards.toString());
            }
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: User not signed in (no token)");
        }
    }

    private void handleDeck(String username) {
        switch (request.getMethod()) {
            case GET -> getConfiguredDeck(username);
            case PUT -> configureDeck(username);
            default ->
                    response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "Method not implemented for deck");
        }
    }

    private void getConfiguredDeck(String username) {
        if (!username.isEmpty() && isSignedIn(username)) {
            if (cardDAO.getDeck(username).isEmpty()) {
                response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "User " + username + " has no cards in the deck");
            } else {
                Vector<Card> deck = cardDAO.getDeck(username);
                if (request.getPath().size() > 1) {
                    if (request.getPath().get(1).equals("format=plain")) {
                        try {
                            response = new Response(StatusCode.OK, ContentType.APPLICATION_JSON, Json.toJsonString(deck));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, username + " deck: " + deck.toString());
                }

            }
        }
    }

    private void configureDeck(String username) {
        if (!username.isEmpty() && isSignedIn(username)) {
            try {
                List<String> deck = Json.toArray(Json.parse(request.getBody()));
                if (deck.size() < 4) {
                    response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: Not enough cards to form a deck");
                } else if (!isOwner(username, deck)) {
                    response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: User " + username + " not the owner of these cards");
                } else {
                    for (String s : deck) {
                        cardDAO.setDeck(username, s);
                    }
                    response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, username + " deck configured");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isOwner(String username, List<String> deck) {
        for (String s : deck) {
            if (cardDAO.checkOwnedCards(username, s).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void handleStats(String username) {
        if (request.getMethod().equals(Method.GET) && isSignedIn(username) && !username.equals("admin")) {
            User user = userDAO.get(username).get();
            user.setStack(cardDAO.getAllCards(username));
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, user.userStats());
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: Cannot access stats. User not logged in");
        }
    }

    private void handleScore(String username) {
        //TODO: add number of battles played, number of wins, order based on score
        if (request.getMethod().equals(Method.GET) && isSignedIn(username) && !username.equals("admin")) {
            List<User> users = (List<User>) userDAO.getAll();
            users.forEach(user -> user.setStack(cardDAO.getAllCards(username)));
            users.removeIf(User::isAdmin);
            StringBuilder stringBuilder = new StringBuilder();
            for (User user : users) {
                stringBuilder.append(user.userScore());
            }
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, stringBuilder.toString());
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: Cannot access scoreboard. User not logged in");
        }
    }

    private User initPlayer(String username) {
        if (isSignedIn(username)) {
            User player = userDAO.get(username).get();
            player.setDeck(cardDAO.getDeck(username));
            player.setStack(cardDAO.getAllCards(username));
            for (Card card : player.getDeck()) {
                cardDAO.delete(card);
            }
            return player;
        }
        System.out.println("INIT PLAYER INFO: player added to the queue");
        return null;
    }

    private void handleBattle(String username) {
        //prepare user for a battle
        userDAO.isInBattle(username, false);
        //check how many users are waiting for a battle
        int size = userDAO.getInBattle(false);
        if (size < 2) {
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "User added to the battle queue");
            try {
                Thread.sleep(12000);
            } catch (InterruptedException e) {
                System.out.println("Thread sleep error: ");
                e.printStackTrace();
            }
        } else if (size == 2) {
            //init users ready for battle
            players = userDAO.getFighters(false);

            players.get(0).setDeck(cardDAO.getDeck(players.get(0).getUsername()));
            players.get(1).setDeck(cardDAO.getDeck(players.get(1).getUsername()));


            Battle battle = new Battle(players.get(0), players.get(1), 100);
            List<String> battleScore = battle.start();
            userDAO.isInBattle(players.get(0).getUsername(), true);
            userDAO.isInBattle(players.get(0).getUsername(), true);
            try {
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, Json.stringToJson(battleScore));
            } catch (JsonProcessingException e) {
                System.out.println("stringToJson error: ");
                e.printStackTrace();
            }
        } else {
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "Fight in progress. Wait your turn.");
        }
    }

    private void handleTradings(String username) {
        if (isSignedIn(username)) {
            if (request.getPath().size() < 2) {
                switch (request.getMethod()) {
                    case GET -> checkTradingDeals(username);
                    case POST -> createTradingDeal(username);
                    default ->
                            response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "Method not implemented");
                }
            } else {
                switch (request.getMethod()) {
                    case POST -> tradeCards(username);
                    case DELETE -> deleteTradingDeal(username);
                    default ->
                            response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "Method not implemented");
                }
            }

        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "User " + username + " not signed in. Trade not possible");
        }

    }

    private void tradeCards(String username) {
        String tradingId = request.getPath().get(1);
        if (tradingsDAO.get(tradingId).isPresent()) {
            Trade trade = tradingsDAO.get(tradingId).get();
            String tradeCardId = Json.jsonToString(request.getBody());
            if (trade.getOwner().equals(username) && cardDAO.checkOwner(username, tradeCardId)) {
                response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "You cannot trade with yourself.");
            } else {
                String card_id = trade.getCardId();
                cardDAO.updateOwner(card_id, username);
                cardDAO.updateOwner(tradeCardId, trade.getOwner());
                tradingsDAO.delete(tradingId);
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "Cards successfully traded.");
            }
        }
    }

    private void checkTradingDeals(String username) {
        List<Trade> tradings = (List<Trade>) tradingsDAO.getAll();
        if (tradings.isEmpty()) {
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "No trading deals available.\n");
        } else {
            tradings.removeIf(trade -> trade.getOwner().equals(username));
            if (tradings.isEmpty()) {
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "\nNo available trading deals for user " + username);
            } else {
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "\n" + tradings);
            }
        }
    }

    private void createTradingDeal(String username) {
        try {
            Trade trade = Json.fromJson(Json.parse(request.getBody()), Trade.class);
            trade.setOwner(username);
            tradingsDAO.insert(trade);
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "Trading deal (" + trade.getTradingId() + ") successfully created.\n");
        } catch (IOException e) {
            System.out.println("Create trading deal error: " + e.getMessage());
        }
    }

    private void deleteTradingDeal(String username) {
        String tradingId = (request.getPath() != null ? request.getPath().get(1) : "");
        if (tradingsDAO.get(tradingId).isPresent()) {
            tradingsDAO.delete(tradingId);
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "Trading deal (" + tradingId + ") deleted.\n");
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "No such trading deal.\n");
        }
    }
}
