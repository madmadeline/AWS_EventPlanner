package caml.group.demo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.db.DatabaseUtil;
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

// ALL TESTS PASSED 12/11 3:45PM
/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LogInHandlerTest {
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


	void testInput(String incoming) {
		System.out.println("testing valid input");
		LogInHandler handler = new LogInHandler();
		AddLogInRequest req = new Gson().fromJson(incoming, AddLogInRequest.class);
		AddLogInResponse response = handler.handleRequest(req, createContext("post"));

		Assert.assertTrue(response.result);
		Assert.assertEquals(200, response.statusCode);
		Assert.assertEquals(req.getUsername(), response.username);
		Assert.assertEquals(req.getPassword(), response.password);
		Assert.assertEquals(req.getChoiceID(), response.choice.getID());
	}

	void testFailInput(String incoming, String outgoing) {
		System.out.println("testing invalid input");
		LogInHandler handler = new LogInHandler();
		AddLogInRequest req = new Gson().fromJson(incoming, AddLogInRequest.class);
		AddLogInResponse response = handler.handleRequest(req, createContext("post"));

		Assert.assertEquals(400, response.statusCode);
		Assert.assertEquals(outgoing, response.error);
	}

	@Before
	public void createChoice() throws Exception {
		System.out.println("creating mock choice");

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
//	@Test
	public void deleteChoice() throws Exception {
		System.out.println("deleting mock choice");
		choice = choiceDAO.getChoice(choice.getID());
//		choice = choiceDAO.getChoice("709eba57-c49b-4dae-a3dc-121cecada2b2");
		choiceDAO.deleteSpecificChoice(choice.getID());
	}

	@Test
	public void testAddUserAndPass() {
		String SAMPLE_INPUT_STRING = "{\"username\": \"john doe\", \"password\": \"Hello\"," +
				"\"choiceID\": \""+choice.getID()+"\"}";

		testInput(SAMPLE_INPUT_STRING);
	}

	@Test
	public void testAddUserNoPass() {
		String SAMPLE_INPUT_STRING = "{\"username\": \"jane doe\", \"password\": \"\"," +
				"\"choiceID\": \""+ choice.getID() + "\"}";


		testInput(SAMPLE_INPUT_STRING);
	}

	@Test
	public void testAddUserInvalidChoiceID() {
		String SAMPLE_INPUT_STRING = "{\"username\": \"jane doe\", \"password\": \"\"," +
				"\"choiceID\": \"9999\"}";


		testFailInput(SAMPLE_INPUT_STRING, "Invalid Choice ID");
	}

	@Test
	public void testLoadUser() throws Exception {
		String SAMPLE_INPUT_STRING = "{\"username\": \"jane doe\", \"password\": \"\"," +
				"\"choiceID\": \""+ choice.getID() + "\"}";

		testInput(SAMPLE_INPUT_STRING);
		testInput(SAMPLE_INPUT_STRING);

		choice = choiceDAO.getChoice(choice.getID());
		Assert.assertEquals(1, choice.getUsers().size());
	}

	@Test
	public void testNoUsername() {
		String SAMPLE_INPUT_STRING = "{\"username\": \"\", \"password\": \"pass10001\"}";

		testFailInput(SAMPLE_INPUT_STRING, "No username was given.");
	}

	@Test
	public void testWrongPass() throws Exception {
		String SAMPLE_INPUT_STRING = "{\"username\": \"scary\", \"password\": \"\"," +
				"\"choiceID\": \""+ choice.getID() + "\"}";
		String SAMPLE_INPUT_STRING_WRONG = "{\"username\": \"scary\", \"password\": \"boo\"," +
				"\"choiceID\": \""+ choice.getID() + "\"}";

		testInput(SAMPLE_INPUT_STRING);
		testFailInput(SAMPLE_INPUT_STRING_WRONG, "Incorrect password: boo.");

		choice = choiceDAO.getChoice(choice.getID());
		Assert.assertEquals(1, choice.getUsers().size());
	}


}
