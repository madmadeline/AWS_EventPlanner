package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.db.DatabaseUtil;
import caml.group.demo.db.UserDAO;
import caml.group.demo.http.AddCreateChoiceRequest;
import caml.group.demo.http.AddCreateChoiceResponse;
import caml.group.demo.http.AddReportRequest;
import caml.group.demo.http.AddReportResponse;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import caml.group.demo.model.TestContext;
import caml.group.demo.model.User;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class ReportHandlerTest {
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

        ReportHandler handler = new ReportHandler();
        AddReportRequest req = new Gson().fromJson(incoming, AddReportRequest.class);
        AddReportResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertTrue(response.result);
        Assert.assertEquals(200, response.statusCode);
    }

    @Test
    public void testReport() throws Exception {
        testInput("");
    }

}
