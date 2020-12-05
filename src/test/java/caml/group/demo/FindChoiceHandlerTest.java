package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.db.DatabaseUtil;
import caml.group.demo.db.UserDAO;
import caml.group.demo.http.*;
import caml.group.demo.model.Choice;
import caml.group.demo.model.TestContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

public class FindChoiceHandlerTest {
    UserDAO udao;
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

        FindChoiceHandler handler = new FindChoiceHandler();
        AddFindChoiceRequest req = new Gson().fromJson(incoming, AddFindChoiceRequest.class);
        AddFindChoiceResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertTrue(response.result);
        System.out.println(response.toString());
        Assert.assertEquals(200, response.statusCode);
    }

    void testFailInput(String incoming, String outgoing) throws IOException, ClassNotFoundException, SQLException {
        LogInHandler handler = new LogInHandler();
        AddLogInRequest req = new Gson().fromJson(incoming, AddLogInRequest.class);
        AddLogInResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertEquals(400, response.statusCode);
    }

//    @Test
//    public void testFindChoice() throws Exception {
//        String SAMPLE_INPUT_STRING = "{\"choiceID\":\"899cdca0-61dc-4e78-9f15-6608d2af2348\"}";
//        try {
//            testInput(SAMPLE_INPUT_STRING);
//        } catch (IOException ioe) {
//            Assert.fail("Invalid:" + ioe.getMessage());
//        }
//    }
}
