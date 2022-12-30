package at.bif3.swe1.kisin.httpServer.databank;

import at.bif3.swe1.kisin.monsterTradingCards.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements DAO<T>{
    public static void main(String[] args) throws SQLException {
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

        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.get(3);
        System.out.println(user);

        int result = userDAO.delete(user);
        System.out.println("Delete result " + result);
    }

    @Override
    public Optional<User> get(int id) throws SQLException {
        User user;
        PreparedStatement preparedStatement;
        try (Connection conn = DbConnection.getInstance().connect()) {
            user = null;

            String sql = "SELECT user_id, username, password, bio, token, image, elo, coins, admin FROM \"user\" WHERE user_id = ?";
            preparedStatement = conn.prepareStatement(sql);
        }


        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            int uid = resultSet.getInt("user_id");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String bio = resultSet.getString("bio");
            String token = resultSet.getString("token");
            String image = resultSet.getString("image");
            int elo = resultSet.getInt("elo");
            int coins = resultSet.getInt("coins");
            boolean isAdmin = resultSet.getBoolean("admin");

            user = new User(uid, username, password, bio, token, image, isAdmin, coins, elo);
        }

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAll() throws SQLException {
        return null;
    }

    @Override
    public int save(T t) throws SQLException {
        return 0;
    }

    @Override
    public int insert(T t) throws SQLException {
        return 0;
    }

    @Override
    public int update(T t) throws SQLException {
        return 0;
    }

    @Override
    public int delete(T t) throws SQLException {
        return 0;
    }

    @Override
    public int save(User user) throws SQLException {
        return 0;
    }

    @Override
    public int insert(User user) throws SQLException {
        Connection conn = DbConnection.getInstance().connect();

        String sql = "INSERT INTO \"user\" (username, password, bio, token, image, elo, coins, admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmnt = conn.prepareStatement(sql);
        stmnt.setString(1, user.getUsername());
        stmnt.setString(2, user.getPassword());
        stmnt.setString(3, user.getBio());
        stmnt.setString(4, user.getToken());
        stmnt.setString(5, user.getImage());
        stmnt.setInt(6, user.getElo());
        stmnt.setInt(7, user.getCoins());
        stmnt.setBoolean(8, user.isAdmin());

        int result = stmnt.executeUpdate();

        stmnt.close();
        conn.close();
        return result;
    }

    @Override
    public int update(User user) throws SQLException {
        Connection conn = DbConnection.getInstance().connect();

        String sql = "UPDATE \"user\" SET username = ?, password = ?, bio = ?, token = ?, image = ?, elo = ?, coins = ?, admin = ? WHERE user_id = ?";

        PreparedStatement stmnt = conn.prepareStatement(sql);

        stmnt.setString(1, user.getUsername());
        stmnt.setString(2, user.getPassword());
        stmnt.setString(3, user.getBio());
        stmnt.setString(4, user.getToken());
        stmnt.setString(5, user.getImage());
        stmnt.setInt(6, user.getElo());
        stmnt.setInt(7, user.getCoins());
        stmnt.setBoolean(8, user.isAdmin());
        stmnt.setInt(9, user.getUser_id());
        int result = stmnt.executeUpdate();
        stmnt.close();
        conn.close();
        return result;
    }

    @Override
    public int delete(User user) throws SQLException {
        Connection conn = DbConnection.getInstance().connect();

        String sql = "DELETE FROM \"user\" WHERE user_id = ?";

        PreparedStatement stmnt = conn.prepareStatement(sql);

        stmnt.setInt(1, user.getUser_id());

        int result = stmnt.executeUpdate();

        stmnt.close();
        conn.close();

        return result;
    }
}
