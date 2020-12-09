package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.db.DatabaseUtil;
import caml.group.demo.db.UserDAO;
import caml.group.demo.http.AddLogInRequest;
import caml.group.demo.http.AddLogInResponse;
import caml.group.demo.http.AddSubmitFeedbackRequest;
import caml.group.demo.http.AddSubmitFeedbackResponse;
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

public class SubmitFeedbackHandlerTest {
    UserDAO userDAO;
    ChoiceDAO choiceDAO;
    LambdaLogger logger;
    Choice choice;
    User user;

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

        SubmitFeedbackHandler handler = new SubmitFeedbackHandler();
        AddSubmitFeedbackRequest req = new Gson().fromJson(incoming, AddSubmitFeedbackRequest.class);
        AddSubmitFeedbackResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertTrue(response.result);
        Assert.assertEquals(200, response.statusCode);
    }

    void testFailInput(String incoming) throws IOException, ClassNotFoundException, SQLException {
        SubmitFeedbackHandler handler = new SubmitFeedbackHandler();
        AddSubmitFeedbackRequest req = new Gson().fromJson(incoming, AddSubmitFeedbackRequest.class);
        AddSubmitFeedbackResponse response = handler.handleRequest(req, createContext("post"));

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
        alts.add(new Alternative(UUID.randomUUID().toString(), "Earthworm"));
        alts.add(new Alternative(UUID.randomUUID().toString(), "Inchworm"));
        alts.add(new Alternative(UUID.randomUUID().toString(), "Silkworm"));
        alts.add(new Alternative(UUID.randomUUID().toString(), "Giant Gippsland earthworm"));
        alts.add(new Alternative(UUID.randomUUID().toString(), "Wormhole"));

        choice = new Choice(UUID.randomUUID().toString(), "Best worm",
                alts, Timestamp.from(Instant.now()), 5);

        choiceDAO.addChoice(choice);

        // register coronavirus
        System.out.println("registering corona virus");
        user = new User("9999","Corona","Virus");
        userDAO.addUser(user, choice.getID());
        choice.addUser(user);
    }

//    @After
    public void clean() throws Exception {
        System.out.println("deleting mock choice");
        choice = choiceDAO.getChoice(choice.getID());
        choiceDAO.deleteSpecificChoice(choice.getID());
    }

    @Test
    public void testSubmitApprovalGiantGippslandEarthworm() throws Exception {
        String altID = choice.getAlternatives().get(3).getID();
        String userID = user.getID();
        String username = user.getName();

        String SAMPLE_INPUT_STRING = "{\"userID\":\"" + userID + "\",\"altID\":" +
                "\"" + altID + "\",\"rating\":\"A\"," +
                "\"username\":\"" + username + "\"}";
        try {
            testInput(SAMPLE_INPUT_STRING);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

    @Test
    public void testSubmitDuplicateApproval() throws Exception {
        String SAMPLE_INPUT_STRING = "{\"userID\":\"9102\",\"altID\":" +
                "\"385c4a3d-a9dd-4150-b32b-603e6773d0fb\",\"rating\":\"A\"," +
                "\"username\":\"d\"}";
        try {
            testFailInput(SAMPLE_INPUT_STRING);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

}
