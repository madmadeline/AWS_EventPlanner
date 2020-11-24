package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.http.AddCreateChoiceRequest;
import caml.group.demo.http.AddCreateChoiceResponse;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.ArrayList;

public class FindChoiceHandler implements RequestHandler<AddCreateChoiceRequest,AddCreateChoiceResponse> {
    LambdaLogger logger;

    public Choice getChoice(String id) throws Exception {
        Choice choice = null;
        logger.log("Getting dao");
        ChoiceDAO dao = new ChoiceDAO(logger);
        logger.log("Got dao. Getting choice");
        choice = dao.getChoice(id);
        return choice;
    }

    @Override
    public AddCreateChoiceResponse handleRequest(AddCreateChoiceRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
        logger.log(req.toString());

        boolean fail = false;
        String failMessage = "";
        Choice choice = null;
        String id = "";

        try{
            id = req.getChoiceID();
            logger.log(id);
            choice = getChoice(id);
        } catch (Exception e) {
            fail = true;
            failMessage = "Failed to retrieve choice";
        }

        AddCreateChoiceResponse response;
        if(fail) response = new AddCreateChoiceResponse(400, failMessage);
        else response = new AddCreateChoiceResponse(200, choice);

        return response;
    }
}
