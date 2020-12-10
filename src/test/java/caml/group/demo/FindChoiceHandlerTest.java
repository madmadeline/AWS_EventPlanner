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
        java.sql.Connection conn;
        conn = DatabaseUtil.connect();

        FindChoiceHandler handler = new FindChoiceHandler();
        AddFindChoiceRequest req = new Gson().fromJson(incoming, AddFindChoiceRequest.class);
        AddFindChoiceResponse response = handler.handleRequest(req, createContext("post"));

        choice = choiceDAO.getChoice(choice.getID());
//        ArrayList<Alternative> alts = response.alternatives;
//        for (Alternative alt : response.alternatives) {
//            if (alt.getDescription().equals("Spoon")) {
//                System.out.println("first alt " + alt.getDescription());
//                System.out.println(alt.getFeedback().get(0).getMessage());
//            }
//        }


        Assert.assertTrue(response.result);
//        System.out.println(response.toString());
        Assert.assertEquals(200, response.statusCode);
    }

    void testFailInput(String incoming) throws IOException, ClassNotFoundException, SQLException {
        LogInHandler handler = new LogInHandler();
        AddLogInRequest req = new Gson().fromJson(incoming, AddLogInRequest.class);
        AddLogInResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertEquals(400, response.statusCode);
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

//    @Test
//    public void testFindChoice() throws Exception {
//        String SAMPLE_INPUT_STRING = "{\"choiceID\":\""+choice.getID()+"\"}";
//        try {
//            testInput(SAMPLE_INPUT_STRING);
//        } catch (IOException ioe) {
//            Assert.fail("Invalid:" + ioe.getMessage());
//        }
//    }
//
//    @Test
//    public void testFindInvalidChoice() throws Exception {
//        String SAMPLE_INPUT_STRING = "{\"choiceID\":\"100e01\"}";
//        try {
//            testFailInput(SAMPLE_INPUT_STRING);
//        } catch (IOException ioe) {
//            Assert.fail("Invalid:" + ioe.getMessage());
//        }
//    }
//
//    @Test
//    public void testFindChoiceWithFeedback() throws Exception {
//        String SAMPLE_INPUT_STRING = "{\"choiceID\":\""+choice.getID()+"\"}";
//        try {
//            SubmitFeedbackMessageHandler fbHandler = new SubmitFeedbackMessageHandler();
//            AddSubmitFeedbackMessageRequest request = new AddSubmitFeedbackMessageRequest();
//            request.setAltID(choice.getAlternatives().get(0).getID());
//            request.setUsername(user.getName());
//            request.setMessage("From the spoon");
//            request.setUserID(user.getID());
//
//            fbHandler.handleRequest(request, context);
//
//            testInput(SAMPLE_INPUT_STRING);
//        } catch (IOException ioe) {
//            Assert.fail("Invalid:" + ioe.getMessage());
//        }
//    }
}
