package at.bif3.swe1.kisin.httpServer.databank;

import at.bif3.swe1.kisin.monsterTradingCards.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class UserDAO implements DAO<User>{
    public static void main(String[] args){
//        UserDAO userDAO = new UserDAOImpl();
//        User user = userDAO.get(3);
//        System.out.println(user);

//        UserDAO userDAO = new UserDAOImpl();
//        User user = new User(0, "njinji", "njet", "to much to do",":/", "njinji-mtcg", false, 20, 100);
//        int result = userDAO.insert(user);
//        System.out.println("Result: " + result);

//        UserDAO userDAO = new UserDAOImpl();
//        User user = new User(3, "njonjo", "deputamadre", "much ado about nothing", ":S", "njonjo-mtcg", true, 20, 100);
//        int result = userDAO.update(user);
//        System.out.println("Update user result: " + result);

//        UserDAO userDAO = new UserDAOImpl();
//        User user = userDAO.get(3);
//        System.out.println(user);
//
//        int result = userDAO.delete(user);
//        System.out.println("Delete result " + result);
    }

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
//        User user;
//        PreparedStatement preparedStatement;
//        try (Connection conn = DbConnection.getInstance().connect()) {
//            user = null;
//
//            String sql = "SELECT user_id, username, password, bio, token, image, elo, coins, admin FROM \"user\" WHERE user_id = ?";
//            preparedStatement = conn.prepareStatement(sql);
//        }

        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT user_id, username, password, bio, token, image, elo, coins, admin, name " +
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
                        resultSet.getString(10)
                ));

//                int uid = resultSet.getInt("user_id");
//                String username = resultSet.getString("username");
//                String password = resultSet.getString("password");
//                String bio = resultSet.getString("bio");
//                String token = resultSet.getString("token");
//                String image = resultSet.getString("image");
//                int elo = resultSet.getInt("elo");
//                int coins = resultSet.getInt("coins");
//                boolean isAdmin = resultSet.getBoolean("admin");

//                user = new User(uid, username, password, bio, token, image, isAdmin, coins, elo);
            }
        } catch (SQLException e){
            System.out.println("Error: get user by id. " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> get(String username){
//        User user;
//        PreparedStatement preparedStatement;
//        try (Connection conn = DbConnection.getInstance().connect()) {
//            user = null;
//
//            String sql = "SELECT user_id, username, password, bio, token, image, elo, coins, admin FROM \"user\" WHERE user_id = ?";
//            preparedStatement = conn.prepareStatement(sql);
//        }

        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT user_id, username, password, bio, token, image, elo, coins, admin, name " +
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
                        resultSet.getString(10)
                ));

//                int uid = resultSet.getInt("user_id");
//                String username = resultSet.getString("username");
//                String password = resultSet.getString("password");
//                String bio = resultSet.getString("bio");
//                String token = resultSet.getString("token");
//                String image = resultSet.getString("image");
//                int elo = resultSet.getInt("elo");
//                int coins = resultSet.getInt("coins");
//                boolean isAdmin = resultSet.getBoolean("admin");

//                user = new User(uid, username, password, bio, token, image, isAdmin, coins, elo);
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
                "SELECT user_id, username, password, bio, token, image, elo, coins, admin, name " +
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
                        resultSet.getString(10)
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

//    private int getUserId(User user){
//        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement("")){
//
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//        return 0;
//    }
    @Override
    public void insert(User user){
//        Connection conn = DbConnection.getInstance().connect();
//
//        String sql = "INSERT INTO \"user\" (username, password, bio, token, image, elo, coins, admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//
//        PreparedStatement stmnt = conn.prepareStatement(sql);
//        stmnt.setString(1, user.getUsername());
//        stmnt.setString(2, user.getPassword());
//        stmnt.setString(3, user.getBio());
//        stmnt.setString(4, user.getToken());
//        stmnt.setString(5, user.getImage());
//        stmnt.setInt(6, user.getElo());
//        stmnt.setInt(7, user.getCoins());
//        stmnt.setBoolean(8, user.isAdmin());
//
//        int result = stmnt.executeUpdate();
//
//        stmnt.close();
//        conn.close();
//        return result;
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
//    @Override
//    public void update(User playgroundPoint, String[] params) {
//        String oldUsername = playgroundPoint.getUsername();
//        // update the item
//        playgroundPoint.setUser_id( Integer.parseInt(Objects.requireNonNull( params[0], "Id cannot be null" )));
//        playgroundPoint.setCoins( Integer.parseInt(Objects.requireNonNull( params[1], "coins cannot be null" ) ) );
//        playgroundPoint.setUsername( Objects.requireNonNull( params[2] ) );
//        playgroundPoint.setPassword( Objects.requireNonNull( params[3] ) );
//        playgroundPoint.setElo( Integer.parseInt(Objects.requireNonNull( params[4], "elo cannot be null" ) ) );
//        playgroundPoint.setImage( Objects.requireNonNull( params[5], "image cannot be null" ) );
//        playgroundPoint.setToken( Objects.requireNonNull( params[6], "token cannot be null" ) );
//        playgroundPoint.setBio( params[7] );
//
//        // persist the updated item
//        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
//                UPDATE public.user
//                SET user_id = ?, username = ?, password = ?, bio = ?, token = ?, image = ?, coins = ?, elo = ?
//                WHERE username = ?;
//                """)
//        ) {
//            statement.setInt(1, playgroundPoint.getUser_id() );
//            statement.setString(2, playgroundPoint.getUsername() );
//            statement.setString( 3, playgroundPoint.getPassword() );
//            statement.setString( 4, playgroundPoint.getBio() );
//            statement.setString( 5, playgroundPoint.getToken() );
//            statement.setString( 6, playgroundPoint.getImage() );
//            statement.setInt( 7, playgroundPoint.getCoins() );
//            statement.setInt( 8, playgroundPoint.getElo() );
//            statement.setString(9, oldUsername);
//
//            statement.execute();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }
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

//        Connection conn = DbConnection.getInstance().connect();
//
//        String sql = "UPDATE \"user\" SET username = ?, password = ?, bio = ?, token = ?, image = ?, elo = ?, coins = ?, admin = ? WHERE user_id = ?";
//
//        PreparedStatement stmnt = conn.prepareStatement(sql);
//
//        stmnt.setString(1, user.getUsername());
//        stmnt.setString(2, user.getPassword());
//        stmnt.setString(3, user.getBio());
//        stmnt.setString(4, user.getToken());
//        stmnt.setString(5, user.getImage());
//        stmnt.setInt(6, user.getElo());
//        stmnt.setInt(7, user.getCoins());
//        stmnt.setBoolean(8, user.isAdmin());
//        stmnt.setInt(9, user.getUser_id());
//        int result = stmnt.executeUpdate();
//        stmnt.close();
//        conn.close();
//        return result;

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
//        Connection conn = DbConnection.getInstance().connect();
//
//        String sql = "DELETE FROM \"user\" WHERE user_id = ?";
//
//        PreparedStatement stmnt = conn.prepareStatement(sql);
//
//        stmnt.setInt(1, user.getUser_id());
//
//        int result = stmnt.executeUpdate();
//
//        stmnt.close();
//        conn.close();

//        return result;
    }
}
