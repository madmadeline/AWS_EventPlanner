package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.db.DatabaseUtil;
import caml.group.demo.http.AddCreateChoiceRequest;
import caml.group.demo.http.AddCreateChoiceResponse;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import caml.group.demo.model.TestContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

// ALL TESTS WORK 12/11 2:51PM
public class CreateChoiceHandlerTest {
    Choice choice = null;
    ChoiceDAO choiceDAO;
    LambdaLogger logger;

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
        DatabaseUtil.connect();
        int index = 0;

        CreateChoiceHandler handler = new CreateChoiceHandler();
        AddCreateChoiceRequest req = new Gson().fromJson(incoming, AddCreateChoiceRequest.class);
        AddCreateChoiceResponse response = handler.handleRequest(req, createContext("post"));

        choice = choiceDAO.getChoice(response.choiceID);

        Assert.assertTrue(response.result);
        Assert.assertEquals(200, response.statusCode);

        // check that the choice description is correct
        Assert.assertEquals(req.getChoiceDescription(), response.description);

        // check that the max team size is correct
        Assert.assertEquals(req.getMaxTeamSize(), response.maxTeamSize);

        // check that all the alternatives are correct
        ArrayList<Alternative> alternatives = response.alternatives;
        int altsLen = alternatives.size();

        if (req.getAlt1ID() != null) {
            Assert.assertEquals(req.getAlt1Description(), alternatives.get(index).getDescription());
            index++;
        }
        if (req.getAlt2ID() != null) {
            Assert.assertEquals(req.getAlt2Description(), alternatives.get(index).getDescription());
            index++;
        }
        if (altsLen > 2 || req.getAlt3ID() != null) {
            Assert.assertEquals(req.getAlt3Description(),
                    alternatives.get(index).getDescription());
            index++;
        }
        if (altsLen > 3 || req.getAlt4ID() != null) {
            Assert.assertEquals(req.getAlt4Description(),
                    alternatives.get(index).getDescription());
            index++;
        }
        if (altsLen > 4 || req.getAlt5ID() != null) {
            Assert.assertEquals(req.getAlt5Description(),
                    alternatives.get(index).getDescription());
        }
    }

    void testFailInput(String incoming, String outgoing) {
        CreateChoiceHandler handler = new CreateChoiceHandler();
        AddCreateChoiceRequest req = new Gson().fromJson(incoming, AddCreateChoiceRequest.class);
        AddCreateChoiceResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertEquals(400, response.statusCode);
        Assert.assertEquals(outgoing, response.error);
    }

    @Before
    public void init() {
        logger = createContext("post").getLogger();
        choiceDAO = new ChoiceDAO(logger);
    }

    @After
    public void clean() throws Exception {
        if (choice != null) {
            choiceDAO.deleteSpecificChoice(choice.getID());
        }
    }

    @Test
    public void createChoice1Alt() {
        String SAMPLE_INPUT_STRING = "{\"choiceDescription\":\"Whaddaya cook with tomatoes??\"," +
                "\"maxTeamSize\":\"2\"," +
                "\"alt3ID\":\"spicy tomato soup\",\"alt3Description\":\"spicy tomato soup\"" +
                "}";
        testFailInput(SAMPLE_INPUT_STRING, "Failed. Not enough alternatives");
    }

    @Test
    public void createChoice4Alts() throws Exception {
        String SAMPLE_INPUT_STRING = "{\"choiceDescription\":\"Whaddaya cook with tomatoes??\"," +
                "\"maxTeamSize\":\"2\"," +
                "\"alt1ID\":\"spicy tomato soup\",\"alt1Description\":\"spicy tomato soup\"," +
                "\"alt2ID\":\"pizza\",\"alt2Description\":\"pizza\"," +
                "\"alt3ID\":\"chili mac\",\"alt3Description\":\"chili mac\"," +
                "\"alt4ID\":\"tomato and egg\",\"alt4Description\":\"tomato and egg\"" +
                "}";
        try {
            testInput(SAMPLE_INPUT_STRING);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

    @Test
    public void createChoiceTestDuplicateAlts() throws Exception {
        String SAMPLE_INPUT_STRING = "{\"choiceDescription\":\"Whaddaya cook with tomatoes??\"," +
                "\"maxTeamSize\":\"2\"," +
                "\"alt1ID\":\"spicy tomato soup\",\"alt1Description\":\"spicy tomato soup\"," +
                "\"alt2ID\":\"spicy tomato soup\",\"alt2Description\":\"spicy tomato soup\"" +
                "}";
        try {
            testInput(SAMPLE_INPUT_STRING);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

    @Test
    public void createChoiceAltsOutOfOrder() throws Exception {
        String SAMPLE_INPUT_STRING = "{\"choiceDescription\":\"Whaddaya cook with tomatoes??\"," +
                "\"maxTeamSize\":\"2\"," +
                "\"alt2ID\":\"pizza\",\"alt2Description\":\"pizza\"," +
                "\"alt4ID\":\"tomato and egg\",\"alt4Description\":\"tomato and egg\"" +
                "}";
        try {
            testInput(SAMPLE_INPUT_STRING);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }


    @Test
    public void createChoiceAltTooLong() {
        String SAMPLE_INPUT_STRING = "{\"choiceDescription\":\"Whaddaya cook with tomatoes??\"," +
                "\"maxTeamSize\":\"2\"," +
                "\"alt1ID\":\"spicy tomato soup\",\"alt1Description\":\"spicy tomato soup\"," +
                "\"alt2ID\":\"pizza\",\"alt2Description\":\"pizza\"," +
                "\"alt4ID\":\"tomato and egg\",\"alt4Description\":\"tomato and egg\"," +
                "\"alt5ID\":\"i don't care about tomatoes i just wanna sleep and rest a bit\"," +
                "\"alt5Description\":\"i don't care about tomatoes i just wanna sleep and rest a bit\"" +
                "}";
        testFailInput(SAMPLE_INPUT_STRING, "Either the choice description or an alternative " +
                    "description exceeds the 60 character limit");
    }

    @Test
    public void createChoiceDescTooLong() {
        String SAMPLE_INPUT_STRING = "{\"choiceDescription\":" +
                "\"i don't care about tomatoes i just wanna sleep and rest a bit\"," +
                "\"maxTeamSize\":\"2\"," +
                "\"alt1ID\":\"spicy tomato soup\",\"alt1Description\":\"spicy tomato soup\"," +
                "\"alt2ID\":\"pizza\",\"alt2Description\":\"pizza\"," +
                "\"alt3ID\":\"chili mac\",\"alt3Description\":\"chili mac\"" +
                "}";
        testFailInput(SAMPLE_INPUT_STRING, "Either the choice description or an alternative " +
                    "description exceeds the 60 character limit");
    }
}
