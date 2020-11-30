package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.http.AddCreateChoiceRequest;
import caml.group.demo.http.AddCreateChoiceResponse;
import caml.group.demo.http.AddFindChoiceRequest;
import caml.group.demo.http.AddFindChoiceResponse;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.ArrayList;

public class FindChoiceHandler implements RequestHandler<AddFindChoiceRequest,AddFindChoiceResponse> {
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
    public AddFindChoiceResponse handleRequest(AddFindChoiceRequest req, Context context) {
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

        AddFindChoiceResponse response;
        if(fail) response = new AddFindChoiceResponse(400, failMessage);
        else response = new AddFindChoiceResponse(choice, 200);

        return response;
    }
}
