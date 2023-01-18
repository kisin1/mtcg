package at.bif3.swe1.kisin.httpServer.database;

import java.sql.*;
import java.io.Closeable;

public class DbConnection implements Closeable{

    private static DbConnection instance;
    private Connection connection;
    private static final String database = "mtc_db";
    private static final String dbUsername = "kisin";
    private static final String dbPassword = "mtc321";

    public static void main(String[] args) throws SQLException {
        DbConnection db = new DbConnection();
        db.connect();
    }

    public DbConnection(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Postgresql JDBC driver not found");
            e.printStackTrace();
        }
    }
    public Connection connect(String db) throws SQLException {
        return connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + db, dbUsername, dbPassword);

    }
    public Connection connect() throws SQLException {
        return connect(database);
    }

    private Connection getConnection() {
        if(connection == null){
            try {
               connection = DbConnection.getInstance().connect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }
    
    public boolean executeSql(String sql) throws SQLException {
        return executeSql(getConnection(), sql, false);
    }

    public static boolean executeSql(Connection connection, String sql, boolean ignoreIfFails) throws SQLException {
        try(Statement statement = connection.createStatement()){
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            if(!ignoreIfFails)
                throw e;
            return false;
        }
    }

    public static boolean executeSql(Connection connection, String sql) throws SQLException {
        return executeSql(connection, sql, false);
    }

    @Override
    public void close(){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
    }
    
    public static DbConnection getInstance(){
        if(instance == null){
            instance = new DbConnection();
        }
        return instance;
    }
}
