package at.bif3.swe1.kisin.httpServer.database;

import at.bif3.swe1.kisin.monsterTradingCards.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO userDAO;
    private User user;
    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
    }

    @Test
    void get() {
        User user = new User(0, "getTest", "pass", "much ado about nothing", "getTest", ":@", 100, 20, true, "Test", 3, 45);
        userDAO.insert(user);
        assertEquals("getTest-mtcgToken", userDAO.get("getTest").map(User::getToken).orElse("Unknown"));
    }


    @Test
    void insert() {
        User user = new User(0, "insertTest", "pass", "much ado about nothing", "test-mtcg", ":@", 100, 20, true, "Test", 3, 45);
        userDAO.insert(user);
        assertEquals("much ado about nothing", userDAO.get("insertTest").map(User::getBio).orElse("Unknown"));
    }

    @Test
    void update() {
        User user = new User(0, "test", "password", "much ado about nothing", "test-mtcg", ":@", 100, 20, true, "Test", 3, 45);
        userDAO.insert(user);
        User userDb = userDAO.get("test").orElse(new User());
        userDAO.update(userDb, new String[]{"testCase", "word", "me testing...", "testCase-mtcgToken", "", "105", "20", "false", "mmmmhh"});
        assertEquals("word", userDAO.get("testCase").map(User::getPassword).orElse("Empty"));
    }

    @Test
    void delete() {
        User user = new User(0, "deleteTest", "password", "much ado about nothing", "test-mtcg", ":@", 100, 20, true, "Test", 3, 45);
        userDAO.insert(user);
        User userDb = userDAO.get("deleteTest").orElse(new User("Unknown", "Unknown"));
        assertEquals("deleteTest", userDb.getUsername());
        userDAO.delete(userDb);
        assertEquals("null", userDAO.get("deleteTest").map(t -> userDb.getUsername()).orElse("null"));
    }
}