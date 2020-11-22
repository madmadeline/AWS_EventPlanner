package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.http.AddCreateChoiceRequest;
import caml.group.demo.http.AddCreateChoiceResponse;
import caml.group.demo.http.AddLogInRequest;
import caml.group.demo.http.AddLogInResponse;

import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import caml.group.demo.db.UserDAO;
//import caml.group.demo.model.Model;
import caml.group.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.internal.org.objectweb.asm.TypeReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateChoiceHandler implements RequestHandler<AddCreateChoiceRequest,AddCreateChoiceResponse> {
//	Model model;
	LambdaLogger logger;
	
	
	public void createChoice(Choice choice) throws Exception {
		logger.log("In createChoice in CreateChoiceHandler");
		ChoiceDAO dao = new ChoiceDAO(logger);
		logger.log("Retrieved Dao in CreateChoiceHandler");
	}
	
	
	@Override
	public AddCreateChoiceResponse handleRequest(AddCreateChoiceRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

		boolean fail = false;
		String failMessage = "";
		Choice choice = new Choice(req.getChoiceID(), req.getChoiceDescription(), null,
				req.getDateOfCreation());
	}
    

}
