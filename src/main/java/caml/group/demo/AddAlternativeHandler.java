package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.http.AddCreateChoiceRequest;
import caml.group.demo.http.AddCreateChoiceResponse;
import caml.group.demo.model.Choice;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AddAlternativeHandler implements RequestHandler<AddCreateChoiceRequest,AddCreateChoiceResponse> {
    LambdaLogger logger;


    public void createChoice(Choice choice) throws Exception {
        logger.log("In createChoice in CreateChoiceHandler");
        ChoiceDAO dao = new ChoiceDAO(logger);
        logger.log("Retrieved Dao in CreateChoiceHandler");
        dao.addChoice(choice);
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

        try{
            createChoice(choice);
        } catch (Exception e) {
            failMessage = "Failed to create choice";
            fail = true;
        }

        AddCreateChoiceResponse response;
        if(fail) response = new AddCreateChoiceResponse(400, failMessage);
        else response = new AddCreateChoiceResponse(200, choice);

        return response;
    }
}
