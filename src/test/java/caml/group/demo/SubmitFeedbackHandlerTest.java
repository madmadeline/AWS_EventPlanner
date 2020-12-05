package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.db.DatabaseUtil;
import caml.group.demo.db.UserDAO;
import caml.group.demo.http.AddLogInRequest;
import caml.group.demo.http.AddLogInResponse;
import caml.group.demo.http.AddSubmitFeedbackRequest;
import caml.group.demo.http.AddSubmitFeedbackResponse;
import caml.group.demo.model.Choice;
import caml.group.demo.model.TestContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

public class SubmitFeedbackHandlerTest {
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

//    @Test
//    public void testSubmitApproval() throws Exception {
//        String SAMPLE_INPUT_STRING = "{\"userID\":\"9102\",\"altID\":" +
//                "\"385c4a3d-a9dd-4150-b32b-603e6773d0fb\",\"rating\":\"A\"," +
//                "\"username\":\"d\"}";
//        try {
//            testInput(SAMPLE_INPUT_STRING);
//        } catch (IOException ioe) {
//            Assert.fail("Invalid:" + ioe.getMessage());
//        }
//    }
//
//    @Test
//    public void testSubmitDuplicateApproval() throws Exception {
//        String SAMPLE_INPUT_STRING = "{\"userID\":\"9102\",\"altID\":" +
//                "\"385c4a3d-a9dd-4150-b32b-603e6773d0fb\",\"rating\":\"A\"," +
//                "\"username\":\"d\"}";
//        try {
//            testFailInput(SAMPLE_INPUT_STRING);
//        } catch (IOException ioe) {
//            Assert.fail("Invalid:" + ioe.getMessage());
//        }
//    }

}
