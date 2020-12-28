package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.db.DatabaseUtil;
import caml.group.demo.db.UserDAO;
import caml.group.demo.http.*;
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

public class FindChoiceHandlerTest {
    UserDAO userDAO;
    ChoiceDAO choiceDAO;
    LambdaLogger logger;
    Choice choice;
    User user;
    Context context;

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
        context = ctx;
        return ctx;
    }


    void testInput(String incoming) throws Exception {
        DatabaseUtil.connect();

        FindChoiceHandler handler = new FindChoiceHandler();
        AddFindChoiceRequest req = new Gson().fromJson(incoming, AddFindChoiceRequest.class);

        System.out.println("Testing valid input");

        AddFindChoiceResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertTrue(response.result);
        Assert.assertEquals(200, response.statusCode);
    }

    void testFailInput(String incoming, String outgoing) {
        FindChoiceHandler handler = new FindChoiceHandler();
        AddFindChoiceRequest req = new Gson().fromJson(incoming, AddFindChoiceRequest.class);

        System.out.println("testing fail input");
        AddFindChoiceResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertEquals(400, response.statusCode);
        Assert.assertEquals(outgoing, response.error);
    }

    @Before
    public void init() throws Exception {
        logger = createContext("post").getLogger();
        choiceDAO = new ChoiceDAO(logger);
        userDAO = new UserDAO(logger);

        // create mock choice
        System.out.println("creating mock choice");
        ArrayList<Alternative> alts = new ArrayList<>();
        alts.add(new Alternative(UUID.randomUUID().toString(), "Spoon"));
        alts.add(new Alternative(UUID.randomUUID().toString(), "Fork"));
        alts.add(new Alternative(UUID.randomUUID().toString(), "Knife"));
        alts.add(new Alternative(UUID.randomUUID().toString(), "Chair"));

        choice = new Choice(UUID.randomUUID().toString(), "Best silverware to steal from DAKA",
                alts, Timestamp.from(Instant.now()), 5);

        choiceDAO.addChoice(choice);

        // register coronavirus
        System.out.println("registering corona virus");
        user = new User("8888","Corona","Virus");
        userDAO.addUser(user, choice.getID());
        choice.addUser(user);
    }

    @After
    public void clean() throws Exception {
        System.out.println("deleting mock choice");
        choice = choiceDAO.getChoice(choice.getID());
        choiceDAO.deleteSpecificChoice(choice.getID());
    }

    @Test
    public void testFindChoice() throws Exception {
        String SAMPLE_INPUT_STRING = "{\"choiceID\":\""+choice.getID()+"\"}";
        try {
            testInput(SAMPLE_INPUT_STRING);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

    @Test
    public void testFindInvalidChoice() throws Exception {
        String SAMPLE_INPUT_STRING = "{\"choiceID\":\"100e01\"}";
        testFailInput(SAMPLE_INPUT_STRING, "Choice does not exist");
    }

    @Test
    public void testFindChoiceWithFeedback() throws Exception {
        String SAMPLE_INPUT_STRING = "{\"choiceID\":\""+choice.getID()+"\"}";
        try {
            System.out.println("Adding feedback");

            String altID = choice.getAlternatives().get(0).getID();
            String userID = user.getID();
            String username = user.getName();
            String message = "From the spoon";

            SubmitFeedbackMessageHandler fbHandler = new SubmitFeedbackMessageHandler();
            AddSubmitFeedbackMessageRequest request = new AddSubmitFeedbackMessageRequest();
            request.setAltID(altID);
            request.setUsername(username);
            request.setMessage(message);
            request.setUserID(userID);

            AddSubmitFeedbackMessageResponse response = fbHandler.handleRequest(request, context);

            testInput(SAMPLE_INPUT_STRING);
            Assert.assertEquals(message, response.message);
            Assert.assertEquals(username, response.username);
            Assert.assertEquals(userID, response.userID);
            Assert.assertEquals(altID, response.altID);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
}
