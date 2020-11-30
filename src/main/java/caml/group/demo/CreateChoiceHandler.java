package caml.group.demo;

import caml.group.demo.http.AddLogInRequest;
import caml.group.demo.http.AddLogInResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import caml.group.demo.db.UserDAO;
//import caml.group.demo.model.Model;
import caml.group.demo.model.User;

public class CreateChoiceHandler implements RequestHandler<AddLogInRequest,AddLogInResponse> {
//	Model model;
	LambdaLogger logger;
	
	
	/**
	 * Try to get User from RDS.
	 */
	public User loadUser(String name, String pass, int choiceID) throws Exception {
		User user = null;
		try {
			if (logger != null) { logger.log("in loadUser"); }
			UserDAO dao = new UserDAO(logger);
			if (logger != null) { logger.log("retrieved DAO"); }
			user = dao.getUser(name, pass, choiceID);
			if (logger != null) { logger.log("retrieved user"); }
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	
	@Override
	public AddLogInResponse handleRequest(AddLogInRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

		boolean fail = false;
		String failMessage = "";
		User user = null;
		String name = "";
		String pass = "";
		int choiceID;
		
		try {
			name = req.getUsername();
			choiceID = req.getChoiceID();
			try {
				pass = req.getPassword();
				user = loadUser(name, pass, choiceID);
			} catch (Exception e) {
				failMessage = req.getPassword() + " is an invalid password.";
				fail = true;
			}
		} catch (Exception e) {
			failMessage = req.getUsername() + " is an invalid username.";
			fail = true;
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		AddLogInResponse response;
		if (fail) {
			response = new AddLogInResponse(400, failMessage);
		} else {
			response = new AddLogInResponse(user, 200);  // success
		}

		return response; 
	}
    

}
