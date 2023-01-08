package at.bif3.swe1.kisin.httpServer.databank;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public class SessionDAO implements DAO<String>{
    @Override
    public Optional<String> get(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<String> get(String token) {//check if logged in
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT * " +
                        "FROM session " +
                        "WHERE token = ?;")){
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Optional.of(token);
            }
        } catch (SQLException e){
            System.out.println("Error: get token " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Collection<String> getAll() {
        return null;
    }

    @Override
    public void insert(String token) { //log in
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "INSERT INTO session (token) " +
                        "VALUES (?);"
        )){
            statement.setString(1, token);

            statement.execute();
        } catch (SQLException e){
            System.out.println("ERROR: insert token " + e.getMessage());
        }
    }

    @Override
    public void update(String s, String[] params) {

    }

    @Override
    public void delete(String token) {
        try (PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "DELETE FROM session " +
                        "WHERE token = ?;"
        )){
            statement.setString(1, token);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
