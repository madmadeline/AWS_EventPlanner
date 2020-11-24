// TODO right now, can't have same username for diff users in diff choices
//  make the values in User table not unique

// TODO if password isn't specified by user when registering, store in db as NULL or ""?
// 	make default value of password in User table as ""

// TODO delete isAdmin column in User table

// TODO delete login, register funks in Model class

package caml.group.demo;

import caml.group.demo.http.AddLogInRequest;
import caml.group.demo.http.AddLogInResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import caml.group.demo.db.UserDAO;
//import caml.group.demo.model.Model;
import caml.group.demo.model.User;

/**
 * For handling JSON requests for registering and logging in a user to a choice.
 * List of functions:
 * 		loadOrInsertUser(String name, String pass, int choiceID) --> User
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
	public User loadOrInsertUser(String name, String pass, int choiceID) throws Exception {
		User user;
		logger.log("Attempting to load or insert User into table\n");
		try { // try getting the User from RDS
			UserDAO dao = new UserDAO(logger);
			user = dao.getUser(name, pass, choiceID);
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
		int choiceID;
		AddLogInResponse response;

		try { // get the given username and password
			name = req.getUsername();
			try {
				choiceID = req.getChoiceID();
				try {
					pass = req.getPassword();
					user = loadOrInsertUser(name, pass, choiceID); // try to log in
				} catch (Exception e) { // can't get password from request
					failMessage = "Invalid password: " + req.getPassword();
					fail = true;
				}
			} catch (Exception e) { // can't get choice ID from request
				failMessage = "Invalid choice ID: " + req.getChoiceID();
			}
		} catch (Exception e) { // can't get username from request
			failMessage = "Invalid username: " + req.getUsername();
			fail = true;
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		if (fail) {
			response = new AddLogInResponse(400, failMessage);
		} else {
			response = new AddLogInResponse(user, 200);  // success
		}

		logger.log(response.toString());
		return response; 
	}
    

}
