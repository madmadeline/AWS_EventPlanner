package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.http.AddLogInRequest;
import caml.group.demo.http.AddLogInResponse;

import caml.group.demo.model.Choice;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import caml.group.demo.db.UserDAO;
//import caml.group.demo.model.Model;
import caml.group.demo.model.User;

/**
 * For handling JSON requests for registering and logging in a user to a choice.
 * List of functions:
 * 		logInOrRegister(String name, String pass, int choiceID) --> User
 * 		handleRequest(AddLogInRequest req, Context context) --> AddLogInResponse
 * @author Group Caml
 */
public class LogInHandler implements RequestHandler<AddLogInRequest,AddLogInResponse> {
//	Model model;
	LambdaLogger logger;
	
	
	/**
	 * Try to get User from RDS. If the User doesn't exist, insert them.
	 * @param name The specified username
	 * @param pass The specified password
	 * @return the User
	 * @throws Exception if the user couldn't be loaded or registered
	 */
	public User logInOrRegister(String name, String pass, int choiceID) throws Exception {
		User user;
		logger.log("Attempting to log in or register User\n");
		try { // try getting the User from RDS
			UserDAO dao = new UserDAO(logger);
			user = dao.loadOrInsertUser(name, pass, choiceID);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log("Failed to load or insert user\n");
		}
		return null;
	}


	/**
	 * Handles a log in request and either
	 * 	1. Registers a new user for the choice,
	 * 	2. States that the password was incorrect, or
	 * 	3. Logs in the user
	 * @param req The log in request
	 * @param context The request context
	 * @return the
	 */
	@Override
	public AddLogInResponse handleRequest(AddLogInRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of RequestHandler\n");
		logger.log(req.toString()); // "Add (username,password)"

		boolean fail = false;
		String failMessage = "";
		User user = null;
		String name;
		String pass;
		int choiceID = 0;
		AddLogInResponse response;
		Choice choice = null;

		try { // get the given username and password
			name = req.getUsername();
			if (name.equals("")) {
				failMessage = "No username was given.";
				fail = true;
			} else if (name.length() > 30) {
				failMessage = "The username is longer than 30 characters.";
				fail = true;
			}
			try {
				choiceID = req.getChoiceID();
				ChoiceDAO cdao = new ChoiceDAO(logger);
				choice = cdao.getChoice(""+choiceID);
				try {
					pass = req.getPassword();
					user = logInOrRegister(name, pass, choiceID); // try to log in
				} catch (Exception e) { // can't get password from request
					failMessage = "Invalid password: " + req.getPassword() + ".";
					fail = true;
				}
			} catch (Exception e) { // can't get choice ID from request
				failMessage = "Invalid choice ID: " + req.getChoiceID() + ".";
			}
		} catch (Exception e) { // can't get username from request
			failMessage = "Invalid username: " + req.getUsername() + ".";
			fail = true;
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		response = new AddLogInResponse(400, failMessage);

		if (!fail) {
			try {
				response = new AddLogInResponse(user, choice,200);  // success
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		logger.log(response.toString());
		return response; 
	}
    

}
