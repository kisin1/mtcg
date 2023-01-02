package at.bif3.swe1.kisin.httpServer.model;

import at.bif3.swe1.kisin.httpServer.databank.CardDAO;
import at.bif3.swe1.kisin.httpServer.databank.SessionDAO;
import at.bif3.swe1.kisin.httpServer.databank.UserDAO;
import at.bif3.swe1.kisin.httpServer.enums.ContentType;
import at.bif3.swe1.kisin.httpServer.enums.Method;
import at.bif3.swe1.kisin.httpServer.enums.StatusCode;
import at.bif3.swe1.kisin.httpServer.json.Json;
import at.bif3.swe1.kisin.monsterTradingCards.User;
import at.bif3.swe1.kisin.monsterTradingCards.cards.Card;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestHandler implements Runnable {

    private final Socket connection;
    private Response response;
    private Request request;

    private static final UserDAO userDAO = new UserDAO();
    private static final CardDAO cardDAO = new CardDAO();

    private static int i = 0;

    public RequestHandler(Socket connection){ this.connection = connection; }

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
        }
        finally {
            try {
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                    connection.close();
                }
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }


    private boolean isValid(String firstString){
        String[] validTables = {"users", "sessions", "packages", "transactions", "cards", "deck", "stats", "score", "battles", "tradings"};
        return Arrays.asList(validTables).contains(firstString);
    }

    private void handleUsers(String username) throws IOException {
        switch (request.getMethod()){
            case POST -> signUp();
            case GET -> getUser(username);
            case PUT -> editUser();
            default -> this.response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "Method not implemented");
        }
    }

    private void signUp() throws IOException {
        if(!userExists()){
            User user = Json.fromJson(Json.parse(request.getBody()), User.class);
            userDAO.insert(user);
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "INFO: User " + user.getUsername() + " signed up.");
        }else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: User already exists");
        }
    }
    private boolean userExists() {
        String username = null;
        if(!request.getAuthorizationToken().isEmpty()){
            username = getUsernameFromToken();
        } else if(request.getBody() != null){
            username = request.parseBody().get("Username");
        }
        return userDAO.get(username).isPresent();
    }

    private void editUser() {
        String username = getUsernameFromToken();
        if(userDAO.get(username).isPresent() && request.getPath().get(2).equals(username) && isSignedIn(username)){
            User user = userDAO.get(username).get();

            userDAO.update(user, new String[]{user.getUsername(), user.getPassword(),
                    request.parseBody().get("Bio"), user.getToken(), request.parseBody().get("Image"), String.valueOf(user.getElo()),
                    String.valueOf(user.getCoins()), String.valueOf(user.isAdmin()), request.parseBody().get("Name")});
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "INFO: User " + user.getUsername() + " is updated.");
        }else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: No user with those credentials");
        }
    }

    private void getUser(String username) {
        if(request.getPath().get(2).equals(username) && isSignedIn(username)){
            if(userDAO.get(username).isPresent()){
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, userDAO.get(username).get().toString());
            } else {
                response = new Response(StatusCode.NO_CONTENT, ContentType.TEXT_PLAIN, "INFO: No user with these credentials");
            }
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: User not signed in");
        }
    }

    private boolean isSignedIn(String username){
        SessionDAO sessionDAO = new SessionDAO();
        return sessionDAO.get(username + "-mtcgToken").isPresent();
    }

    private String getUsernameFromToken(){
        if(!request.getAuthorizationToken().isEmpty()){
            String[] authorizationToken = request.getAuthorizationToken().split(" ");
            String[] username = authorizationToken[1].split("-");
            return username[0];
        }
        return null;
    }

    private void handlePackages() throws IOException {
        i++;
        if(userExists() && isSignedIn("admin") && request.getMethod().equals(Method.POST)){
            ArrayList<Card> packages = Json.fromJsonToArray(Json.parse(request.getBody()));
            for(Card card : packages){
                if(!cardExists(card)){
                    card.processCard();
                    card.setPackageId(i);
                    cardDAO.insert(card);
                } else {
                    response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "Card with id: " + card.getCardId() + " already exists");
                }
            }
            if(response == null){
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "Packages with id: " + i + " added");
            }
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "INFO: User does not exist or is not admin");
        }
    }

    private boolean cardExists(Card card) {
        return cardDAO.get(card.getCardId()).isPresent();
    }

    private String getPassword(String username){
       if(userDAO.get(username).isPresent()){
            return userDAO.get(username).map(User::getPassword).get();
        } else if(request.getBody() != null && !request.getPath().get(1).equals("packages")){
               return request.parseBody().get("Password");
           }
       return "";
    }

    private void handleResponse() throws IOException {
        if(!isValid(request.getPath().get(1))){
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: Invalid path");
        } else {
        String username = getUsername();
        String password = getPassword(username);
        String path = ((request.getPath() != null) ? request.getPath().get(1) : "test");
        switch (path){
           case "users" -> handleUsers(username);
           case "sessions" -> logIn(username, password);
           case "packages" -> handlePackages();
           case "transactions" -> handleTransactions(username);
           case "cards" -> response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "INFO: Cards are not yet handled");
           case "deck" -> response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "INFO: Deck are not yet handled");
           case "stats" -> response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "INFO: Stats are not yet handled");
           case "score" -> response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "INFO: Score are not yet handled");
           case "battles" -> response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "INFO: Battles are not yet handled");
           case "tradings" -> response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "INFO: Tradings are not yet handled");
           case "test" -> response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "TEST: No Content");
           default -> response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "INFO: Not yet implemented");
            }
        }
    }

    private String getUsername() {
        if(request.getAuthorizationToken().length() != 0){
             return getUsernameFromToken();
        } else if(request.getBody() != null ){
            return request.parseBody().get("Username");
        }
        return "";
    }

    private void logIn(String username, String password) {
        if(userDAO.get(username).isPresent()){
            User user = userDAO.get(username).get();
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                SessionDAO sessionDAO = new SessionDAO();
                if(sessionDAO.get(username + "-mtcgToken").isEmpty()){
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

    private void handleTransactions(String username){
        if(request.getMethod().equals(Method.POST) && request.getPath().get(2).equals("packages")){
            if(isSignedIn(username)){
                buyPackage(username);
            } else {
                response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "User " + username + " is not logged in");
            }
        }
    }
    private void buyPackage(String username){
        //select min package_id where owner is null
        int min = cardDAO.getMin();
        List<Card> cards = cardDAO.getAll(min);
        //check if user has enough coins
        User user = userDAO.get(username).get();
        if(!cards.isEmpty()){
            if(user.getCoins() > 0){
                for(Card card : cards){
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
}
