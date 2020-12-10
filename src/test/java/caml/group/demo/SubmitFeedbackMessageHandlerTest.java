package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.db.DatabaseUtil;
import caml.group.demo.db.UserDAO;
import caml.group.demo.http.AddSubmitFeedbackMessageRequest;
import caml.group.demo.http.AddSubmitFeedbackMessageResponse;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import caml.group.demo.model.TestContext;
import caml.group.demo.model.User;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

// ALL TESTS WORK 12/10 5:03PM
public class SubmitFeedbackMessageHandlerTest {
    UserDAO userDAO;
    ChoiceDAO choiceDAO;
    LambdaLogger logger;
    Choice choice;

    /**
     * Helper method that creates a context that supports logging so you can test lambda functions
     * in JUnit without worrying about the logger anymore.
     *
     * @param apiCall      An arbitrary string to identify which API is being called.
     * @return
     */
    Context createContext(String apiCall) {
        caml.group.demo.model.TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }


    void testInput(String incoming) throws Exception {
        java.sql.Connection conn;
        conn = DatabaseUtil.connect();
        System.out.println("testing input string");

        SubmitFeedbackMessageHandler handler = new SubmitFeedbackMessageHandler();
        AddSubmitFeedbackMessageRequest req = new Gson().fromJson(incoming, AddSubmitFeedbackMessageRequest.class);
        AddSubmitFeedbackMessageResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertTrue(response.result);
//        System.out.println("passed");
        Assert.assertEquals(200, response.statusCode);
        System.out.println("passed");
    }

    void testFailInput(String incoming) throws IOException, ClassNotFoundException, SQLException {
        SubmitFeedbackMessageHandler handler = new SubmitFeedbackMessageHandler();
        AddSubmitFeedbackMessageRequest req = new Gson().fromJson(incoming, AddSubmitFeedbackMessageRequest.class);
        AddSubmitFeedbackMessageResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertEquals(400, response.statusCode);
    }

    @Before
    public void init() throws Exception {
        logger = createContext("post").getLogger();
        choiceDAO = new ChoiceDAO(logger);
        userDAO = new UserDAO(logger);
        User user;

        // create mock choice
        System.out.println("creating mock choice");
        ArrayList<Alternative> alts = new ArrayList<>();
        alts.add(new Alternative(UUID.randomUUID().toString(), "Yes"));
        alts.add(new Alternative(UUID.randomUUID().toString(), "No"));

        choice = new Choice(UUID.randomUUID().toString(), "Do u kno da wae?",
                alts, Timestamp.from(Instant.now()), 5);

        choiceDAO.addChoice(choice);

        // register coronavirus
        System.out.println("registering corona virus");
        user = new User("9999","Corona","Virus");
        userDAO.addUser(user, choice.getID());
        choice.addUser(user);
    }

    @After
    public void clean() throws Exception {
        System.out.println("deleting mock choice");
        choice = choiceDAO.getChoice(choice.getID());
        choiceDAO.deleteSpecificChoice(choice.getID());
    }

//    @Test
//    public void testAddMessageToYes() throws Exception {
//        String altID = choice.getAlternatives().get(0).getID(); // Alt: Yes
//        String userID = choice.getUsers().get(0).getID(); // Corona Virus
//        String username = choice.getUser(userID).getName();
//        String message = "Heck yea i kno da wae";
//
//        String SAMPLE_INPUT_STRING = "{\"altID\":\""+altID+"\",\"userID\":" +
//                "\""+userID+"\",\"username\":\""+username+"\"," +
//                "\"message\":\""+message+"\"}";
//        try {
//            System.out.println("testing now!!");
//            testInput(SAMPLE_INPUT_STRING);
//        } catch (IOException ioe) {
//            Assert.fail("Invalid:" + ioe.getMessage());
//        }
//    }
//
//    @Test
//    public void testAddMessageToBothAlts() throws Exception {
//        String altID = choice.getAlternatives().get(0).getID(); // Alt: Yes
//        String userID = choice.getUsers().get(0).getID(); // Corona Virus
//        String username = choice.getUser(userID).getName();
//        String message = "Heck yea i kno da wae";
//
//        String SAMPLE_INPUT_STRING = "{\"altID\":\""+altID+"\",\"userID\":" +
//                "\""+userID+"\",\"username\":\""+username+"\"," +
//                "\"message\":\""+message+"\"}";
//        try {
//            testInput(SAMPLE_INPUT_STRING);
//        } catch (IOException ioe) {
//            Assert.fail("Invalid:" + ioe.getMessage());
//        }
//
//        message = "No???????????????????????????";
//        SAMPLE_INPUT_STRING = "{\"altID\":\""+altID+"\",\"userID\":" +
//                "\""+userID+"\",\"username\":\""+username+"\"," +
//                "\"message\":\""+message+"\"}";
//        try {
//            testInput(SAMPLE_INPUT_STRING);
//        } catch (IOException ioe) {
//            Assert.fail("Invalid:" + ioe.getMessage());
//        }
//    }
//
//    @Test
//    public void testEmptyMessage() throws Exception {
//        String altID = choice.getAlternatives().get(0).getID(); // Alt: Yes
//        String userID = choice.getUsers().get(0).getID(); // Corona Virus
//        String username = choice.getUser(userID).getName();
//        String message = "";
//
//        String SAMPLE_INPUT_STRING = "{\"altID\":\""+altID+"\",\"userID\":" +
//                "\""+userID+"\",\"username\":\""+username+"\"," +
//                "\"message\":\""+message+"\"}";
//        try {
////            System.out.println("testing now!!");
//            testFailInput(SAMPLE_INPUT_STRING);
//        } catch (IOException ioe) {
//            Assert.fail("Invalid:" + ioe.getMessage());
//        }
//    }

}
