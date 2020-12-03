package caml.group.demo.db;

import caml.group.demo.model.TestContext;
import caml.group.demo.model.User;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class UserDAOTest {
    UserDAO userDAO;

    @Before
    public void init() throws Exception {
        DatabaseUtil.connect();
        TestContext ctx = new TestContext();
        ctx.setFunctionName("post");
        LambdaLogger logger = ctx.getLogger();
        userDAO = new UserDAO(logger);
    }

    @Test
    public void testAddUser() {
        User user = new User("1000", "testAddUser","pass:testAddUser");
        boolean result;

        try { // user doesn't exist yet, can add
            init();
            result = userDAO.addUser(user, 1234);
            Assert.assertTrue(result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try { // same user --> can't add
            init();
            result = userDAO.addUser(user, 1234);
            Assert.assertFalse(result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddUserNoUsername() {
        User user = new User("1001", "","pass:testAddUser");
        boolean result;

        try {
            init();
            result = userDAO.addUser(user, 1234);
            Assert.assertFalse(result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddUserInvalidChoiceID() {
        User user = new User("1002", "testAddUserIVCID","pass:testAddUser");
        boolean result;

        try {
            init();
            result = userDAO.addUser(user, 000000);
            Assert.assertFalse(result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteUser() {
        User user = new User("1000", "testAddUser","pass:testAddUser");
        boolean result;

        try {
            init();
            result = userDAO.deleteUser(user, 1234);
            Assert.assertTrue(result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try { // user doesn't exist anymore
            init();
            result = userDAO.deleteUser(user, 1234);
            Assert.assertFalse(result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUserIDExists() {
        try {
            init();
            Assert.assertTrue(userDAO.userIDExists("1501"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            init();
            Assert.assertFalse(userDAO.userIDExists("1111"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadUser() {
        String username = "testAddUser";
        String password = "pass:testAddUser";
        String userID = "1000";
        int choiceID = 1234;
        User user;


        try { // user doesn't exist yet, can add
            init();
            user = userDAO.loadOrInsertUser(username, password, choiceID);
            Assert.assertEquals(user.getName(), username);
            Assert.assertEquals(user.getPassword(), password);
            Assert.assertEquals(user.getID(), userID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadUserIncorrectPass() {
        String username = "testAddUser";
        String password = "incorrectPass";
        String userID = "1000";
        int choiceID = 1234;
        User user;


        try { // user doesn't exist yet, can add
            init();
            user = userDAO.loadOrInsertUser(username, password, choiceID);
            // should throw error: "Incorrect password"
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegisterUser() {
        String username = "Sherry Bull";
        String password = "black cherry";
        int choiceID = 1234;
        User user;

        try { // user doesn't exist yet, can add
            init();
            user = userDAO.loadOrInsertUser(username, password, choiceID);
            Assert.assertEquals(username, user.getName());
            Assert.assertEquals(password, user.getPassword());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testRegisterUserNoUsername() {
        String username = "";
        String password = "black cherry";
        int choiceID = 1234;
        User user;

        try { // user doesn't exist yet, can add
            init();
            user = userDAO.loadOrInsertUser(username, password, choiceID);
            // should throw error: "No username was specified"
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegisterUserInvalidChoiceID() {
        String username = "Terry Bull";
        String password = "black cherry";
        int choiceID = 9030;
        User user;

        try { // user doesn't exist yet, can add
            init();
            user = userDAO.loadOrInsertUser(username, password, choiceID);
            // should throw error: "Invalid choice ID"
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegisterUsernameTooLong() {
        String username = "one of jyalu's favorite scentss"; // 31 char (1 above limit)
        String password = "black cherry";
        int choiceID = 1234;
        User user;

        try { // user doesn't exist yet, can add
            init();
            user = userDAO.loadOrInsertUser(username, password, choiceID);
            // should throw error: "The username is too long"
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegisterPasswordTooLong() {
        String username = "cinnamon";
        String password = "one of jyalu's favorite scentss"; // 31 char (1 above limit)
        int choiceID = 1234;

        try { // user doesn't exist yet, can add
            init();
            userDAO.loadOrInsertUser(username, password, choiceID);
            // should throw error: "The password is too long"
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRowToUserObject() {

    }


}
