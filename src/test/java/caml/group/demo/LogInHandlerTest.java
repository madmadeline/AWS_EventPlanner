package caml.group.demo;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import caml.group.demo.http.AddLogInRequest;
import caml.group.demo.http.AddLogInResponse;
import caml.group.demo.model.Model;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LogInHandlerTest {
	Model model;

	/**
	 * Helper method that creates a context that supports logging so you can test lambda functions
	 * in JUnit without worrying about the logger anymore.
	 * 
	 * @param apiCall      An arbitrary string to identify which API is being called.
	 * @return
	 */
	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}
	
	
	@Before
	public void init() throws ClassNotFoundException, SQLException {
		this.model = new Model("admin", "adminPass");
	}

	void testInput(String incoming, String outgoing) throws IOException, ClassNotFoundException, SQLException {
		init();
		LogInHandler handler = new LogInHandler(model);
		AddLogInRequest req = new Gson().fromJson(incoming, AddLogInRequest.class);
		AddLogInResponse response = handler.handleRequest(req, createContext("compute"));

		Assert.assertEquals(outgoing, response.result);
		Assert.assertEquals(200, response.statusCode);
	}

	void testFailInput(String incoming, String outgoing) throws IOException, ClassNotFoundException, SQLException {
		init();
		LogInHandler handler = new LogInHandler(model);
		AddLogInRequest req = new Gson().fromJson(incoming, AddLogInRequest.class);
		AddLogInResponse response = handler.handleRequest(req, createContext("compute"));

		Assert.assertEquals(400, response.statusCode);
	}

	@Test
	public void testAddUserSimple() throws ClassNotFoundException, SQLException {
		String SAMPLE_INPUT_STRING = "{\"username\": \"john doe\", \"password\": \"\"}";
		String RESULT = "true";

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
	
	@Test
	public void testAddUser() throws ClassNotFoundException, SQLException {
		String SAMPLE_INPUT_STRING = "{\"username\": \"john doe\", \"password\": \"pass101\"}";
		String RESULT = "true";

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}

	@Test
	public void testLoadUser() throws ClassNotFoundException, SQLException {
		String SAMPLE_INPUT_STRING = "{\"username\": \"john doe\", \"password\": \"pass101\"}";
		String RESULT = "true";

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
	
	@Test
	public void testFailInput() throws ClassNotFoundException, SQLException {
		String SAMPLE_INPUT_STRING = "{\"username\": \"\", \"password\": \"pass10001\"}";
		String RESULT = "false";

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
	
	@Test
	public void testFailInputWrongPass() throws ClassNotFoundException, SQLException {
		String SAMPLE_INPUT_STRING = "{\"username\": \"john doe\", \"password\": \"pass10001\"}";
		String RESULT = "false";

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
}
