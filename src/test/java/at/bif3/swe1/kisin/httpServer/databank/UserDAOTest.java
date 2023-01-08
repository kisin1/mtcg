package at.bif3.swe1.kisin.httpServer.databank;

import at.bif3.swe1.kisin.monsterTradingCards.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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
        Optional<User> user = userDAO.get(2);
        assertEquals("Nji", user.map(User::getUsername).orElse("Unknown"));
    }

    @Test
    void getAll() {

    }

    @Test
    void insert() {
        User user = new User(0, "test", "pan", "much ado about nothing", "gwu-mtcg", ":@", 100, 20, true, "Test");
        userDAO.insert(user);
        assertEquals("test", userDAO.get("test").map(User::getUsername).orElse("Unknown"));
    }

    @Test
    void update() {
        user = userDAO.get("Nji").orElse(new User());
        userDAO.update(user, new String[]{"Njwana", "pili", "prska", "nji-mtcgToken", "", "105", "20", "false", "Njett"});
        System.out.println(userDAO.get("Njo").map(User::getUsername).orElse("Unknown"));
        assertEquals("pili", userDAO.get("Njwana").map(User::getPassword).orElse("Empty"));
    }

    @Test
    void delete() {
        user = userDAO.get("mh").orElse(new User("Unknown", "Unknown"));
        userDAO.delete(user);
        assertEquals("Unknown", userDAO.get("mh").map(User::getUsername).orElse("Unknown"));
    }
}