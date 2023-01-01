package at.bif3.swe1.kisin.httpServer;

import at.bif3.swe1.kisin.httpServer.databank.UserDAO;
import at.bif3.swe1.kisin.httpServer.enums.ContentType;
import at.bif3.swe1.kisin.httpServer.enums.StatusCode;
import at.bif3.swe1.kisin.httpServer.json.Json;
import at.bif3.swe1.kisin.monsterTradingCards.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class RequestHandler implements Runnable {

    private final Socket connection;
    private Response response;
    private Request request;

    private static final UserDAO userDAO = new UserDAO();


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

    private String getUsernameFromBody() throws JsonProcessingException {
        HashMap<String, String> body = Json.toHashMap(request.getBody());
        return body.get("Username");
    }
    private String getPasswordFromBody() throws JsonProcessingException {
        HashMap<String, String> body = Json.toHashMap(request.getBody());
        return body.get("Password");
    }
    private void signUp() throws IOException {
        if(!userExists()){
            User user = Json.fromJson(Json.parse(request.getBody()), User.class);
            userDAO.insert(user);
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "INFO: User " + user.getUsername() + " signed up.");
        }else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: User already exists");
        }
        //TODO: handle user already exists
    }
    private boolean userExists() throws JsonProcessingException {
        HashMap<String, String> hashMap = Json.toHashMap(this.request.getBody());
        return userDAO.get(hashMap.get("Username")).isPresent();
    }

    private void editUser() throws JsonProcessingException {
        String username = getUsernameFromToken();
        if(userDAO.get(username).isPresent() && request.getPath().get(2).equals(username)){
            User user = userDAO.get(username).get();
            HashMap<String, String> body = Json.toHashMap(request.getBody());

            userDAO.update(user, new String[]{user.getUsername(), user.getPassword(),
                    body.get("Bio"), user.getToken(), body.get("Image"), String.valueOf(user.getElo()),
                    String.valueOf(user.getCoins()), String.valueOf(user.isAdmin()), body.get("Name")});
            response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "INFO: User " + user.getUsername() + " is updated.");
        }else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: No user with those credentials");
        }
        //TODO: is the user logged in -> check sessions table

    }

    private void getUser(String username) {
        if(request.getPath().get(2).equals(username) && isSignedIn(username)){
            if(userDAO.get(username).isPresent()){
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, userDAO.get(username).get().toString());
            } else {
                response = new Response(StatusCode.NO_CONTENT, ContentType.TEXT_PLAIN, "INFO: No user with these credentials");
            }
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: Wrong credentials");
        }
    }

    private boolean isSignedIn(String username){
        //TODO: Check database sessions if signed in
        String[] authorizationToken = request.getAuthorizationToken().split(" ");
        String[] name = authorizationToken[1].split("-");
        return name[0].equals(username);
    }

    private String getUsernameFromToken(){
        if(!request.getAuthorizationToken().equals("Unknown")){
            String[] authorizationToken = request.getAuthorizationToken().split(" ");
            String[] username = authorizationToken[1].split("-");
            return username[0];
        }
        return null;
    }
    private void handleResponse() throws IOException {
        if(!isValid(request.getPath().get(1))){
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: Invalid path");
        } else {
        String username = getUsernameFromToken() != null ? getUsernameFromToken() : getUsernameFromBody();

        String path = (request.getPath() != null ? request.getPath().get(1) : "test");
        switch (path){
           case "users" -> handleUsers(username);
           case "sessions" -> logIn(username);
           case "test" -> response = new Response(StatusCode.NO_CONTENT, ContentType.TEXT_PLAIN, "TEST: No Content");
           default -> response = new Response(StatusCode.NOT_IMPLEMENTED, ContentType.TEXT_PLAIN, "INFO: Not yet implemented");
            }
        }
    }

    private void logIn(String username) throws JsonProcessingException {
        User user;
        if(userDAO.get(username).isPresent()){
            user = userDAO.get(username).get();
            //TODO: get token from sessions table
            if(user.getToken().equals(username + "-mtcgToken") && user.getPassword().equals(getPasswordFromBody())){
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "INFO: User " + username + " logged in.");
            } else {
                response = new Response(StatusCode.OK, ContentType.TEXT_PLAIN, "ERROR: Wrong password or username.");
            }
        } else {
            response = new Response(StatusCode.BAD_REQUEST, ContentType.TEXT_PLAIN, "ERROR: No such user. Please sign up.");
        }
    }
}
