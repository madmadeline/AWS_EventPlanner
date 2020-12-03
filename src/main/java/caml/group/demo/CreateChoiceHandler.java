package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.http.AddCreateChoiceRequest;
import caml.group.demo.http.AddCreateChoiceResponse;
import caml.group.demo.http.AddLogInResponse;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateChoiceHandler implements RequestHandler<AddCreateChoiceRequest,AddCreateChoiceResponse> {
//	Model model;
	LambdaLogger logger;
	
	
	// TESTED
	public boolean createChoice(Choice choice) throws Exception {
		logger.log("In createChoice in CreateChoiceHandler");
		ChoiceDAO dao = new ChoiceDAO(logger);
		logger.log("Retrieved Dao in CreateChoiceHandler");
		return dao.addChoice(choice);
	}

	// TESTED
	public boolean checkChoice(String id) throws Exception {
		logger.log("Checking choice");
		ChoiceDAO dao = new ChoiceDAO(logger);
		logger.log("dao created");
		return dao.checkChoice(id);
	}
	
	@Override
	public AddCreateChoiceResponse handleRequest(AddCreateChoiceRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

		boolean fail = false;
		String failMessage = "";
		String randString;

		while(true){
			Random rand = new Random();
			int randInt = rand.nextInt(9000) + 1000;
			randString = String.valueOf(randInt);
			try {
				if(checkChoice(randString)) break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Timestamp time = Timestamp.from(Instant.now());
		String alt1Append = req.getAlt1ID() + "_" + randString;
		String alt2Append = req.getAlt2ID() + "_" + randString;
		String alt3Append = req.getAlt3ID() + "_" + randString;
		String alt4Append = req.getAlt4ID() + "_" + randString;
		String alt5Append = req.getAlt5ID() + "_" + randString;

		Alternative alt1 = new Alternative(alt1Append, req.getAlt1Description());
		Alternative alt2 = new Alternative(alt2Append, req.getAlt2Description());
		Alternative alt3 = new Alternative(alt3Append, req.getAlt3Description());
		Alternative alt4 = new Alternative(alt4Append, req.getAlt4Description());
		Alternative alt5 = new Alternative(alt5Append, req.getAlt5Description());
		ArrayList<Alternative> alts = new ArrayList<>();
		if(req.getAlt1ID() != null) alts.add(alt1);
		if(req.getAlt2ID() != null) alts.add(alt2);
		if(req.getAlt3ID() != null) alts.add(alt3);
		if(req.getAlt4ID() != null) alts.add(alt4);
		if(req.getAlt5ID() != null) alts.add(alt5);

		Choice choice = new Choice(randString, req.getChoiceDescription(), alts,
				time, req.getMaxTeamSize());
		if(alts.size() >= 2){
			try {
				boolean result = createChoice(choice);
				if (!result) {
					failMessage = "Either the choice description, an alternative " +
							"description exceeds the 60 character limit, or you have " +
							"a duplicate alternative";
					fail = true;
				}
			} catch (Exception e) {
//				logger.log("failed here");
				failMessage = "Failed to create choice";
				fail = true;
			}
		}
		else{
//			logger.log("failed here 2");
			failMessage = "Failed. Not enough alternatives";
			fail = true;
		}

//		logger.log("fail " + fail);
		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		AddCreateChoiceResponse response;

		if (fail) {
			logger.log(failMessage);
			response = new AddCreateChoiceResponse(400, failMessage);
		} else {
			response = new AddCreateChoiceResponse(200, choice);  // success
		}

		return response;
	}
    

}
