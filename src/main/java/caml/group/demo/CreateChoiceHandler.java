package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.http.AddCreateChoiceRequest;
import caml.group.demo.http.AddCreateChoiceResponse;
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
	
	
	public void createChoice(Choice choice) throws Exception {
		logger.log("In createChoice in CreateChoiceHandler");
		ChoiceDAO dao = new ChoiceDAO(logger);
		logger.log("Retrieved Dao in CreateChoiceHandler");
		dao.addChoice(choice);
	}

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

		Alternative alt1 = new Alternative(req.getAlt1ID(), req.getAlt1Description());
		Alternative alt2 = new Alternative(req.getAlt2ID(), req.getAlt2Description());
		Alternative alt3 = new Alternative(req.getAlt3ID(), req.getAlt3Description());
		Alternative alt4 = new Alternative(req.getAlt4ID(), req.getAlt4Description());
		Alternative alt5 = new Alternative(req.getAlt5ID(), req.getAlt5Description());
		ArrayList<Alternative> alts = new ArrayList<>();
		if(req.getAlt1ID() != null) alts.add(alt1);
		if(req.getAlt2ID() != null) alts.add(alt2);
		if(req.getAlt3ID() != null) alts.add(alt3);
		if(req.getAlt4ID() != null) alts.add(alt4);
		if(req.getAlt5ID() != null) alts.add(alt5);

		Choice choice = new Choice(randString, req.getChoiceDescription(), alts,
				time);
		if(alts.size() >= 2){
			try {
				createChoice(choice);
			} catch (Exception e) {
				failMessage = "Failed to create choice";
				fail = true;
			}
		}
		else{
			failMessage = "Failed. Not enough alternatives";
			fail = true;
		}

		AddCreateChoiceResponse response;
		if(fail) response = new AddCreateChoiceResponse(400, failMessage);
		else response = new AddCreateChoiceResponse(200, choice);

		return response;
	}
    

}
