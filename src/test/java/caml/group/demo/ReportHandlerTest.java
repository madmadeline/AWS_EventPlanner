package caml.group.demo;

import caml.group.demo.db.DatabaseUtil;
import caml.group.demo.http.AddCreateChoiceRequest;
import caml.group.demo.http.AddCreateChoiceResponse;
import caml.group.demo.http.AddReportRequest;
import caml.group.demo.http.AddReportResponse;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.TestContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import org.junit.Assert;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportHandlerTest {
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

        ReportHandler handler = new ReportHandler();
        AddReportRequest req = new Gson().fromJson(incoming, AddReportRequest.class);
        AddReportResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertTrue(response.result);
        Assert.assertEquals(200, response.statusCode);
    }

    void testFailInput(String incoming, String outgoing) throws IOException, ClassNotFoundException, SQLException {
        CreateChoiceHandler handler = new CreateChoiceHandler();
        AddCreateChoiceRequest req = new Gson().fromJson(incoming, AddCreateChoiceRequest.class);
        AddCreateChoiceResponse response = handler.handleRequest(req, createContext("post"));

        Assert.assertEquals(400, response.statusCode);
        Assert.assertEquals(outgoing, response.error);
    }


}
