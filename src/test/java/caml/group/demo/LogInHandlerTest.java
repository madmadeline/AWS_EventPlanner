package caml.group.demo;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

//import caml.group.demo.LogInHandler;
import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.db.DatabaseUtil;
import caml.group.demo.db.UserDAO;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import caml.group.demo.model.TestContext;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import caml.group.demo.http.AddLogInRequest;
import caml.group.demo.http.AddLogInResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LogInHandlerTest {
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
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}


	void testInput(String incoming) throws Exception {
		java.sql.Connection conn;
		conn = DatabaseUtil.connect();

		LogInHandler handler = new LogInHandler();
		AddLogInRequest req = new Gson().fromJson(incoming, AddLogInRequest.class);
		AddLogInResponse response = handler.handleRequest(req, createContext("post"));

		Assert.assertTrue(response.result);
		Assert.assertEquals(200, response.statusCode);
		Assert.assertEquals(req.getUsername(), response.username);
		Assert.assertEquals(req.getPassword(), response.password);
		Assert.assertEquals(""+req.getChoiceID(), response.choice.getID());
	}

	void testFailInput(String incoming, String outgoing) throws IOException, ClassNotFoundException, SQLException {
		LogInHandler handler = new LogInHandler();
		AddLogInRequest req = new Gson().fromJson(incoming, AddLogInRequest.class);
		AddLogInResponse response = handler.handleRequest(req, createContext("post"));

		Assert.assertEquals(400, response.statusCode);
	}

	@Before
	public void createChoice() throws Exception {
		DatabaseUtil.connect();
		TestContext ctx = new TestContext();
		ctx.setFunctionName("post");
		logger = ctx.getLogger();

		choiceDAO = new ChoiceDAO(logger);

		ArrayList<Alternative> alts = new ArrayList<Alternative>();
		String newID = UUID.randomUUID().toString();

		// tested with 2, 3, 4, 5 alternatives
		alts.add(new Alternative(UUID.randomUUID().toString(), "vanilla"));
		alts.add(new Alternative(UUID.randomUUID().toString(), "strawberry"));
		alts.add(new Alternative(UUID.randomUUID().toString(), "chocolate"));
		alts.add(new Alternative(UUID.randomUUID().toString(), "mint choco chip"));
		alts.add(new Alternative(UUID.randomUUID().toString(), "cookie dough"));

		// adding new choice
		choice = new Choice(newID, "Best Ice Cream", alts,
				Timestamp.from(Instant.now()), 5);
		choiceDAO.addChoice(choice);
	}

	@After
	public void deleteChoice() throws Exception {
		choiceDAO.deleteChoice(choice);
	}

	// passed dec 3 2:39pm
	/*@Test
	public void testAddUserAndPass() throws Exception {
		createChoice();
		System.out.println(choice.getID());
		String SAMPLE_INPUT_STRING = "{\"username\": \"john doe\", \"password\": \"Hello\"," +
				"\"choiceID\": \""+choice.getID()+"\"}";

		try {
			testInput(SAMPLE_INPUT_STRING);
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
		deleteChoice();
	}

	@Test
	public void testAddUserNoPass() throws Exception {
		createChoice();

		System.out.println(choice.getID());

		String SAMPLE_INPUT_STRING = "{\"username\": \"jane doe\", \"password\": \"\"," +
				"\"choiceID\": \""+ choice.getID() + "\"}";


		try {
			testInput(SAMPLE_INPUT_STRING);
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}

		deleteChoice();
	}*/

//	@Test
//	public void testAddUserInvalidChoiceID() throws Exception {
//		String SAMPLE_INPUT_STRING = "{\"username\": \"jane doe\", \"password\": \"\"," +
//				"\"choiceID\": \"9999\"}";
//
//
//		try {
//			testFailInput(SAMPLE_INPUT_STRING, "hi");
//		} catch (IOException ioe) {
//			Assert.fail("Invalid:" + ioe.getMessage());
//		}
//	}
//
//	@Test
//	public void testLoadUserAndPass() throws Exception {
//		String SAMPLE_INPUT_STRING = "{\"username\": \"john doe\", \"password\": \"Hello\"," +
//				"\"choiceID\": \"1234\"}";
//
//		try {
//			testInput(SAMPLE_INPUT_STRING);
//		} catch (IOException ioe) {
//			Assert.fail("Invalid:" + ioe.getMessage());
//		}
//	}



//	@Test
//	public void testLoadUser() throws Exception {
//		String SAMPLE_INPUT_STRING = "{\"username\": \"john doe\", \"password\": \"pass101\"}";
//		String RESULT = "true";
//
//		try {
//			testInput(SAMPLE_INPUT_STRING);
//		} catch (IOException ioe) {
//			Assert.fail("Invalid:" + ioe.getMessage());
//		}
//	}
//
//	@Test
//	public void testFailInput() throws ClassNotFoundException, SQLException {
//		String SAMPLE_INPUT_STRING = "{\"username\": \"\", \"password\": \"pass10001\"}";
//		String RESULT = "false";
//
//		try {
//			testInput(SAMPLE_INPUT_STRING);
//		} catch (IOException ioe) {
//			Assert.fail("Invalid:" + ioe.getMessage());
//		}
//	}
//
//	@Test
//	public void testFailInputWrongPass() throws ClassNotFoundException, SQLException {
//		String SAMPLE_INPUT_STRING = "{\"username\": \"john doe\", \"password\": \"pass10001\"}";
//		String RESULT = "false";
//
//		try {
//			testInput(SAMPLE_INPUT_STRING);
//		} catch (IOException ioe) {
//			Assert.fail("Invalid:" + ioe.getMessage());
//		}
//	}


}
