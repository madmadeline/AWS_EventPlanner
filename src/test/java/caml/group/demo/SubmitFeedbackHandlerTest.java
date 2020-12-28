package caml.group.demo;

import caml.group.demo.db.AlternativeDAO;
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

// ALL TESTS WORK 12/11 4:07PM
public class SubmitFeedbackHandlerTest {
    UserDAO userDAO;
    ChoiceDAO choiceDAO;
    AlternativeDAO alternativeDAO;
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
        SubmitFeedbackHandler handler = new SubmitFeedbackHandler();
        AddSubmitFeedbackRequest req = new Gson().fromJson(incoming, AddSubmitFeedbackRequest.class);
        AddSubmitFeedbackResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertTrue(response.result);
        Assert.assertEquals(200, response.statusCode);
    }

    void testFailInput(String incoming, String outgoing) {
        SubmitFeedbackHandler handler = new SubmitFeedbackHandler();
        AddSubmitFeedbackRequest req = new Gson().fromJson(incoming, AddSubmitFeedbackRequest.class);
        AddSubmitFeedbackResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertEquals(400, response.statusCode);
        Assert.assertEquals(outgoing, response.error);
    }

    @Before
    public void init() throws Exception {
        DatabaseUtil.connect();
        logger = createContext("post").getLogger();
        choiceDAO = new ChoiceDAO(logger);
        userDAO = new UserDAO(logger);
        alternativeDAO = new AlternativeDAO(logger);

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

    @After
    public void clean() throws Exception {
        System.out.println("deleting mock choice");
        choice = choiceDAO.getChoice(choice.getID());
//        choice = choiceDAO.getChoice("f3e8ba22-350b-45e3-9562-422af7f31c3f");
        choiceDAO.deleteSpecificChoice(choice.getID());
    }

    @Test
    public void testSubmitApproveWormhole() throws Exception {
        String altID = choice.getAlternatives().get(4).getID();
        String userID = user.getID();
        String username = user.getName();

        String SAMPLE_INPUT_STRING = "{\"userID\":\"" + userID + "\",\"altID\":" +
                "\"" + altID + "\",\"rating\":\"A\"," +
                "\"username\":\"" + username + "\"}";
        try {
            System.out.println("Adding approval for " + choice.getAlternatives().get(4).getDescription());
            testInput(SAMPLE_INPUT_STRING);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

    @Test
    public void testSubmitDisapproveGiantGippslandEarthworm() throws Exception {
        String altID = choice.getAlternatives().get(3).getID();
        String userID = user.getID();
        String username = user.getName();

        String SAMPLE_INPUT_STRING = "{\"userID\":\"" + userID + "\",\"altID\":" +
                "\"" + altID + "\",\"rating\":\"D\"," +
                "\"username\":\"" + username + "\"}";
        try {
            System.out.println("Adding disapproval for " + choice.getAlternatives().get(3).getDescription());
            testInput(SAMPLE_INPUT_STRING);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

    @Test
    public void testSubmitDuplicateDisapproval() throws Exception {
        String altID = choice.getAlternatives().get(3).getID(); // Giant Gippsland earthworm
        String userID = user.getID();
        String username = user.getName();

        String SAMPLE_INPUT_STRING = "{\"userID\":\"" + userID + "\",\"altID\":" +
                "\"" + altID + "\",\"rating\":\"D\"," +
                "\"username\":\"" + username + "\"}";
        try {
            System.out.println("Adding disapproval for " + choice.getAlternatives().get(3).getDescription());
            testInput(SAMPLE_INPUT_STRING);
            System.out.println("Adding disapproval for " + choice.getAlternatives().get(3).getDescription());
            testFailInput(SAMPLE_INPUT_STRING, "Duplicate approval");
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

    @Test
    public void testChangeRating() throws Exception {
        String altID = choice.getAlternatives().get(0).getID(); // Earthworm
        String altDesc = choice.getAlternatives().get(0).getDescription();
        String userID = user.getID();
        String username = user.getName();

        String SAMPLE_INPUT_STRING_A = "{\"userID\":\"" + userID + "\",\"altID\":" +
                "\"" + altID + "\",\"rating\":\"A\"," +
                "\"username\":\"" + username + "\"}";
        String SAMPLE_INPUT_STRING_D = "{\"userID\":\"" + userID + "\",\"altID\":" +
                "\"" + altID + "\",\"rating\":\"D\"," +
                "\"username\":\"" + username + "\"}";
        try {
            System.out.println("Adding approval for " + altDesc);
            testInput(SAMPLE_INPUT_STRING_A);
            choice = choiceDAO.getChoice(choice.getID());
            Assert.assertEquals(1, alternativeDAO.getAlternativeByID(altID).getTotalApprovals());

            System.out.println("Changing approval to disapproval for " + altDesc);
            testInput(SAMPLE_INPUT_STRING_D);
            choice = choiceDAO.getChoice(choice.getID());
            Assert.assertEquals(0, alternativeDAO.getAlternativeByID(altID).getTotalApprovals());
            Assert.assertEquals(1, alternativeDAO.getAlternativeByID(altID).getTotalDisapprovals());

            System.out.println("Changing disapproval to approval for " + altDesc);
            testInput(SAMPLE_INPUT_STRING_A);
            choice = choiceDAO.getChoice(choice.getID());
            Assert.assertEquals(1, alternativeDAO.getAlternativeByID(altID).getTotalApprovals());
            Assert.assertEquals(0, alternativeDAO.getAlternativeByID(altID).getTotalDisapprovals());
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

    @Test
    public void testClearApproval() throws Exception {
        String altID = choice.getAlternatives().get(3).getID(); // Giant Gippsland earthworm
        String userID = user.getID();
        String username = user.getName();

        String SAMPLE_INPUT_STRING_A = "{\"userID\":\"" + userID + "\",\"altID\":" +
                "\"" + altID + "\",\"rating\":\"A\"," +
                "\"username\":\"" + username + "\"}";
        String SAMPLE_INPUT_STRING_O = "{\"userID\":\"" + userID + "\",\"altID\":" +
                "\"" + altID + "\",\"rating\":\"O\"," +
                "\"username\":\"" + username + "\"}";
        try {
            System.out.println("Adding approval for " + choice.getAlternatives().get(3).getDescription());
            testInput(SAMPLE_INPUT_STRING_A);
            choice = choiceDAO.getChoice(choice.getID());
            Assert.assertEquals(1, alternativeDAO.getAlternativeByID(altID).getTotalApprovals());

            System.out.println("Clearing approval for " + choice.getAlternatives().get(3).getDescription());
            testInput(SAMPLE_INPUT_STRING_O);
            choice = choiceDAO.getChoice(choice.getID());
            Assert.assertEquals(0, alternativeDAO.getAlternativeByID(altID).getTotalApprovals());
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

    @Test
    public void testClearNoApproval() throws Exception {
        String altID = choice.getAlternatives().get(3).getID(); // Giant Gippsland earthworm
        String userID = user.getID();
        String username = user.getName();

        String SAMPLE_INPUT_STRING_O = "{\"userID\":\"" + userID + "\",\"altID\":" +
                "\"" + altID + "\",\"rating\":\"O\"," +
                "\"username\":\"" + username + "\"}";

        System.out.println("Clearing approval for " + choice.getAlternatives().get(3).getDescription());
        testInput(SAMPLE_INPUT_STRING_O);
    }
}
