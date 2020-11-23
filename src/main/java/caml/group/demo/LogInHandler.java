package caml.group.demo;

import caml.group.demo.http.AddLogInRequest;
import caml.group.demo.http.AddLogInResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import caml.group.demo.db.UserDAO;
//import caml.group.demo.model.Model;
import caml.group.demo.model.User;

public class LogInHandler implements RequestHandler<AddLogInRequest,AddLogInResponse> {
//	Model model;
	LambdaLogger logger;
	
	
	/**
	 * Try to get User from RDS. If the User doesn't exist, insert them.
	 * @param name, the specified username
	 * @param pass, the specified password
	 * @return the User
	 * @throws Exception if the user couldn't be loaded or registered
	 */
	public User loadOrInsertUser(String name, String pass, int choiceID) throws Exception {
		User user = null;
		logger.log("Attempting to load or insert User into table\n");
		try { // try getting the User from RDS
			UserDAO dao = new UserDAO(logger);
			user = dao.getUser(name, pass, choiceID);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log("Failed to load or insert user\n");
		}
		return user;
	}
	
	
	@Override
	public AddLogInResponse handleRequest(AddLogInRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of RequestHandler");
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

		return response; 
	}
    

}
