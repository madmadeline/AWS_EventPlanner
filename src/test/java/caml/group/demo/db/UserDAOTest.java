package caml.group.demo.db;

import caml.group.demo.model.Choice;
import caml.group.demo.model.TestContext;
import caml.group.demo.model.User;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

// ALL TESTS RUN 12/11 4:40PM
public class UserDAOTest {
    UserDAO userDAO;
    ChoiceDAO choiceDAO;
    Choice choice;

    @Before
    public void init() throws Exception {
        DatabaseUtil.connect();
        TestContext ctx = new TestContext();
        ctx.setFunctionName("post");
        LambdaLogger logger = ctx.getLogger();

        userDAO = new UserDAO(logger);
        choiceDAO = new ChoiceDAO(logger);

        choice = new Choice("1234", "abacaba", Timestamp.from(Instant.now()), 3);
        choiceDAO.addChoice(choice);
    }

    @After
    public void clean() throws Exception {
        choiceDAO.deleteSpecificChoice(choice.getID());
    }

    @Test
    public void testAddUser() {
        User user = new User("1000", "testAddUser","pass:testAddUser");
        boolean result;

        try { // user doesn't exist yet, can add
            result = userDAO.addUser(user, choice.getID());
            Assert.assertTrue(result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try { // same user --> can't add
            init();
            result = userDAO.addUser(user, "1234");
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
            result = userDAO.addUser(user, choice.getID());
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
            result = userDAO.addUser(user, "000000");
            Assert.assertFalse(result);
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
        String choiceID = "1234";
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
        String choiceID = "1234";
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
        String choiceID = "9030";
        User user;

        try { // user doesn't exist yet, can add
            init();
            user = userDAO.loadOrInsertUser(username, password, choiceID);
            // should throw error: "Choice can't be found in the table - invalid ID"
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
        String choiceID = "1234";
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
        String choiceID = "1234";

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


}
