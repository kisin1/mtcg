package at.bif3.swe1.kisin.httpServer.database;

import at.bif3.swe1.kisin.monsterTradingCards.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDAO implements DAO<User>{

    public static void initDb(){
        try(Connection connection = DbConnection.getInstance().connect()){
            DbConnection.executeSql(connection, "DROP DATABASE mtc_db", true);
            DbConnection.executeSql(connection, "CREATE DATABASE mtc_db", true);
        } catch (SQLException e){
            e.printStackTrace();
        }

        try {
            DbConnection.getInstance().executeSql("""
                CREATE TABLE IF NOT EXISTS "user"
                (
                    user_id  SERIAL PRIMARY KEY,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(50)  NOT NULL,
                    bio      VARCHAR(100),
                    token    VARCHAR(100) NOT NULL,
                    image    VARCHAR(100),
                    elo      int          NOT NULLl,
                    coins    int          NOT NULL,
                    admin    boolean      NOT NULL
                );
                                            
                                            
                """);
     } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Optional<User> get(int id){
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT user_id, username, password, bio, token, image, elo, coins, admin, name, games_won, rounds_played " +
                        "FROM \"user\" " +
                        "WHERE user_id = ?;")){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Optional.of(new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getInt(7),
                        resultSet.getInt(8),
                        resultSet.getBoolean(9),
                        resultSet.getString(10),
                        resultSet.getInt(11),
                        resultSet.getInt(12)
                ));
            }
        } catch (SQLException e){
            System.out.println("Error: get user by id. " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> get(String username){
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT user_id, username, password, bio, token, image, elo, coins, admin, name, games_won, rounds_played " +
                        "FROM \"user\" " +
                        "WHERE username = ?;")){
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Optional.of(new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getInt(7),
                        resultSet.getInt(8),
                        resultSet.getBoolean(9),
                        resultSet.getString(10),
                        resultSet.getInt(11),
                        resultSet.getInt(12)
                ));
            }
        } catch (SQLException e){
            System.out.println("Error: get user by username. " + e.getMessage());
        }
        return Optional.empty();
    }
    @Override
    public Collection<User> getAll(){
        ArrayList<User> result = new ArrayList<>();
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT user_id, username, password, bio, token, image, elo, coins, admin, name, games_won, rounds_played " +
                        "FROM \"user\";")){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                result.add(new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getInt(7),
                        resultSet.getInt(8),
                        resultSet.getBoolean(9),
                        resultSet.getString(10),
                        resultSet.getInt(11),
                        resultSet.getInt(12)
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void insert(User user){
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "INSERT INTO \"user\" (username, password, bio, token, image, elo, coins, admin, name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"
        )){
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getBio());
            statement.setString(4, user.getUsername() + "-mtcgToken");
            statement.setString(5, user.getImage());
            statement.setInt(6, 100);
            statement.setInt(7, 20);
            statement.setBoolean(8, (user.getUsername().equals("admin")));
            statement.setString(9, user.getName());
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int getInBattle(boolean inBattle) {
        try (PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT COUNT(*) " +
                        "FROM \"user\" " +
                        "WHERE \"in_battle\" = ?;"
        )) {
            statement.setBoolean(1, inBattle);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<User> getFighters(boolean inBattle){
        ArrayList<User> result = new ArrayList<>();
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT user_id, username, password, bio, token, image, elo, coins, admin, name, games_won, rounds_played " +
                        "FROM \"user\" " +
                        "WHERE \"in_battle\" = ?;")){
            statement.setBoolean(1, inBattle);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                result.add(new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getInt(7),
                        resultSet.getInt(8),
                        resultSet.getBoolean(9),
                        resultSet.getString(10),
                        resultSet.getInt(11),
                        resultSet.getInt(12)
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
    public void isInBattle(String username, boolean inBattle) {
        try (PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "UPDATE \"user\" " +
                        "SET \"in_battle\" = ? " +
                        "WHERE username = ?;"
        )) {
            statement.setBoolean(1, inBattle);
            statement.setString(2, username);


            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateElo(String username, int elo) {
        try (PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "UPDATE \"user\" " +
                        "SET elo = ? " +
                        "WHERE username = ?;"
        )) {
            statement.setInt(1, elo);
            statement.setString(2, username);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void afterBattleUpdate(User user) {
        try (PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "UPDATE \"user\" " +
                        "SET elo = ?, games_won = ?, rounds_played = ? " +
                        "WHERE username = ?;"
        )) {
            statement.setInt(1, user.getElo());
            statement.setInt(2, user.getRoundsWon());
            statement.setInt(3, user.getRoundsPlayed());
            statement.setString(4, user.getUsername());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void update(User user, String[] params){
        String oldUsername = user.getUsername();
        user.setUsername(Objects.requireNonNull(params[0], "Username cannot be null"));
        user.setPassword(Objects.requireNonNull(params[1], "Password cannot be null"));
        user.setBio(Objects.requireNonNullElse(params[2], "No bio"));
        user.setToken(Objects.requireNonNullElse(params[3], user.getUsername()+"-mtcgToken"));
        user.setImage(Objects.requireNonNullElse(params[4], "o.O"));
        user.setElo(Integer.parseInt(Objects.requireNonNull(params[5], "Elo cannot be null")));
        user.setCoins(Integer.parseInt(Objects.requireNonNull(params[6], "Coins cannot be null")));
        user.setAdmin(Boolean.parseBoolean(Objects.requireNonNull(params[7], "Admin cannot be null")));
        user.setName(Objects.requireNonNullElse(params[8], user.getUsername()));

        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "UPDATE \"user\" " +
                        "SET username = ?, password = ?, bio = ?, token = ?, image = ?, elo = ?, coins = ?, admin = ?, name = ? " +
                        "WHERE username = ?;"
        )){
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getBio());
            statement.setString(4, user.getToken());
            statement.setString(5, user.getImage());
            statement.setInt(6, user.getElo());
            statement.setInt(7, user.getCoins());
            statement.setBoolean(8, user.isAdmin());
            statement.setString(9, user.getName());
            statement.setString(10, oldUsername);

            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateCoins(User user, String[] params) {
        user.setCoins(Integer.parseInt(Objects.requireNonNull(params[0], "Coins cannot be null")));

        try (PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "UPDATE \"user\" " +
                        "SET coins = ? " +
                        "WHERE username = ?;"
        )) {
            statement.setInt(1, user.getCoins());
            statement.setString(2, user.getUsername());


            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void delete(User user){
        try (PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "DELETE FROM \"user\" " +
                        "WHERE user_id = ?;"
        )){
            statement.setInt(1, user.getUser_id());
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
