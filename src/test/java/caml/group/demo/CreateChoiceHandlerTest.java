package caml.group.demo;

import caml.group.demo.db.DatabaseUtil;
import caml.group.demo.http.AddCreateChoiceRequest;
import caml.group.demo.http.AddCreateChoiceResponse;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.TestContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CreateChoiceHandlerTest {
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
        int index = 0;

        CreateChoiceHandler handler = new CreateChoiceHandler();
        AddCreateChoiceRequest req = new Gson().fromJson(incoming, AddCreateChoiceRequest.class);
        AddCreateChoiceResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertTrue(response.result);
        Assert.assertEquals(200, response.statusCode);

        // check that the choice description is correct
        Assert.assertEquals(req.getChoiceDescription(), response.description);

        // check that the max team size is correct
        Assert.assertEquals(req.getMaxTeamSize(), response.maxTeamSize);

        // check that all the alternatives are correct
        ArrayList<Alternative> alts = response.alts;
        int altsLen = alts.size();

//        System.out.println("id " + req.getAlt1ID());
//        System.out.println("desc " + req.getAlt1Description());

        if (req.getAlt1ID() != null) {
//            Assert.assertEquals(req.getAlt1ID(), alts.get(index).getID().split("_")[0]);
            Assert.assertEquals(req.getAlt1Description(), alts.get(index).getDescription());
//            System.out.println("first");
            index++;
        }

        if (req.getAlt2ID() != null) {
//            Assert.assertEquals(req.getAlt2ID(), alts.get(index).getID().split("_")[0]);
            Assert.assertEquals(req.getAlt2Description(), alts.get(index).getDescription());
//            System.out.println("second");
            index++;
        }


        if (altsLen > 2 || req.getAlt3ID() != null) {
//            System.out.println("third");
//            Assert.assertEquals(req.getAlt3ID(), alts.get(index).getID().split("_")[0]);
            Assert.assertEquals(req.getAlt3Description(),
                    alts.get(index).getDescription());
            index++;
        }
        if (altsLen > 3 || req.getAlt4ID() != null) {
//            System.out.println("fourth");
//            Assert.assertEquals(req.getAlt4ID(), alts.get(index).getID().split("_")[0]);
            Assert.assertEquals(req.getAlt4Description(),
                    alts.get(index).getDescription());
            index++;
        }
        if (altsLen > 4 || req.getAlt5ID() != null) {
//            System.out.println("fifth");
//            Assert.assertEquals(req.getAlt5ID(), alts.get(index).getID().split("_")[0]);
            Assert.assertEquals(req.getAlt5Description(),
                    alts.get(index).getDescription());
        }
    }

    void testFailInput(String incoming, String outgoing) throws IOException, ClassNotFoundException, SQLException {
        CreateChoiceHandler handler = new CreateChoiceHandler();
        AddCreateChoiceRequest req = new Gson().fromJson(incoming, AddCreateChoiceRequest.class);
        AddCreateChoiceResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertEquals(400, response.statusCode);
        Assert.assertEquals(outgoing, response.error);
    }

    @Test
    public void createChoice1Alt() throws Exception {
        String SAMPLE_INPUT_STRING = "{\"choiceDescription\":\"Whaddaya cook with tomatoes??\"," +
                "\"maxTeamSize\":\"2\"," +
                "\"alt3ID\":\"spicy tomato soup\",\"alt3Description\":\"spicy tomato soup\"" +
                "}";
        try {
            testFailInput(SAMPLE_INPUT_STRING, "Failed. Not enough alternatives");
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
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
        String SAMPLE_INPUT_STRING = "{\"choiceDescription\":\"Test\"," +
                "\"maxTeamSize\":\"2\"," +
                "\"alt1ID\":\"a\",\"alt1Description\":\"a\"," +
                "\"alt2ID\":\"a\",\"alt2Description\":\"a\"" +
//                "\"alt3ID\":\"chili mac\",\"alt3Description\":\"chili mac\"," +
//                "\"alt4ID\":\"tomato and egg\",\"alt4Description\":\"tomato and egg\"" +
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
//                "\"alt1ID\":\"spicy tomato soup\",\"alt1Description\":\"spicy tomato soup\"," +
                "\"alt2ID\":\"pizza\",\"alt2Description\":\"pizza\"," +
//                "\"alt3ID\":\"chili mac\",\"alt3Description\":\"chili mac\"," +
                "\"alt4ID\":\"tomato and egg\",\"alt4Description\":\"tomato and egg\"" +
                "}";
        try {
            testInput(SAMPLE_INPUT_STRING);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }


    @Test
    public void createChoiceAltTooLong() throws Exception {
        String SAMPLE_INPUT_STRING = "{\"choiceDescription\":\"Whaddaya cook with tomatoes??\"," +
                "\"maxTeamSize\":\"2\"," +
                "\"alt1ID\":\"spicy tomato soup\",\"alt1Description\":\"spicy tomato soup\"," +
                "\"alt2ID\":\"pizza\",\"alt2Description\":\"pizza\"," +
//                "\"alt3ID\":\"chili mac\",\"alt3Description\":\"chili mac\"," +
                "\"alt4ID\":\"tomato and egg\",\"alt4Description\":\"tomato and egg\"," +
                "\"alt5ID\":\"i don't care about tomatoes i just wanna sleep and rest a bit\"," +
                "\"alt5Description\":\"i don't care about tomatoes i just wanna sleep and rest a bit\"" +
                "}";
        try {
            testFailInput(SAMPLE_INPUT_STRING, "Either the choice description, an " +
                    "alternative description exceeds the 60 character limit, or you have a duplicate " +
                    "alternative");
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

    @Test
    public void createChoiceDescTooLong() throws Exception {
        String SAMPLE_INPUT_STRING = "{\"choiceDescription\":" +
                "\"i don't care about tomatoes i just wanna sleep and rest a bit\"," +
                "\"maxTeamSize\":\"2\"," +
                "\"alt1ID\":\"spicy tomato soup\",\"alt1Description\":\"spicy tomato soup\"," +
                "\"alt2ID\":\"pizza\",\"alt2Description\":\"pizza\"," +
                "\"alt3ID\":\"chili mac\",\"alt3Description\":\"chili mac\"" +
                "}";
        try {
            testFailInput(SAMPLE_INPUT_STRING, "Either the choice description, an " +
                    "alternative description exceeds the 60 character limit, or you have a duplicate " +
                    "alternative");
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
}
