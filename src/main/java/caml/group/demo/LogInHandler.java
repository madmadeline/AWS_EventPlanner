package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.http.AddLogInRequest;
import caml.group.demo.http.AddLogInResponse;

import caml.group.demo.model.Choice;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import caml.group.demo.db.UserDAO;
import caml.group.demo.model.User;

/**
 * For handling JSON requests for registering and logging in a user to a choice.
 * List of functions:
 * 		logInOrRegister(String name, String pass, String choiceID) --> User
 * 		handleRequest(AddLogInRequest req, Context context) --> AddLogInResponse
 * @author Group Caml
 */
public class LogInHandler implements RequestHandler<AddLogInRequest,AddLogInResponse> {
	LambdaLogger logger;

	/**
	 * Handles a log in request and either
	 * 	1. Registers a new user for the choice,
	 * 	2. Logs in the user, or
	 * 	3. Throws an error because either:
	 * 		a. No username was given
	 * 		b. The username was too long
	 * 		c. The password was incorrect
	 * 		d. There is no Choice with the given ID
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
		String choiceID = "";
		AddLogInResponse response;
		Choice choice = null;

		name = req.getUsername();
		if (name.equals("")) {
			failMessage = "No username was given.";
			response = new AddLogInResponse(400, failMessage);
			logger.log(response.toString());
			return response;

		} else if (name.length() > 30) {
			failMessage = "The username is longer than 30 characters.";
			response = new AddLogInResponse(400, failMessage);
			logger.log(response.toString());
			return response;
		}

		// make sure that the choice ID is valid
		try {
			choiceID = String.valueOf(req.getChoiceID());
			ChoiceDAO cdao = new ChoiceDAO(logger);
			choice = cdao.getChoice("" + choiceID);
			if (choice == null) {
				failMessage = "Invalid Choice ID";
				response = new AddLogInResponse(400, failMessage);
				logger.log(response.toString());
				return response;
			}
		} catch (Exception e) {
			failMessage = "Database connection error.";
			fail = true;
		}

		// try to log in
		try {
			pass = req.getPassword();
			if (pass.length() > 30) {
				failMessage = "The password is longer than 30 characters.";
				fail = true;
			}

			// make sure that the choice isn't full
			logger.log(String.valueOf(choice.getMaxTeamSize()));
			logger.log(String.valueOf(choice.getUsers().size()));
			if(choice.getMaxTeamSize() <= choice.getUsers().size() ) {
				failMessage = "The choice is full.";
				fail = true;

				for(User aUser: choice.getUsers()) {
					if(name.equals(aUser.getName()) && pass.equals(aUser.getPassword())) {
						fail = false;
					}
				}
			}

			// login or register
			UserDAO dao = new UserDAO(logger);
			user = dao.loadOrInsertUser(name, pass, choiceID);
			if (user == null) {
				failMessage = "Incorrect password: " + req.getPassword() + ".";
				fail = true;
			}
		} catch (Exception e) {
			failMessage = "Database connection error";
			fail = true;
		}
		
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
