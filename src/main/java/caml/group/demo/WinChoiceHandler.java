package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.http.AddWinChoiceRequest;
import caml.group.demo.http.AddWinChoiceResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class WinChoiceHandler implements RequestHandler<AddWinChoiceRequest, AddWinChoiceResponse> {
    LambdaLogger logger;

    public void winChoice(String altID, String choiceID) throws Exception {
        logger.log("Creating dao");
        ChoiceDAO dao = new ChoiceDAO(logger);
        logger.log("Adding winning alt");
        dao.addWinner(altID, choiceID);
    }

    @Override
    public AddWinChoiceResponse handleRequest(AddWinChoiceRequest req, Context context){
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
        logger.log(req.toString());

        boolean fail = false;
        String failMessage = "";

        try{
            winChoice(req.getAltID(), req.getChoiceID());
        }
        catch (Exception e){
            fail = true;
            failMessage = "Failed to set winner";
        }

        if(fail) return new AddWinChoiceResponse(400, failMessage);
        else return new AddWinChoiceResponse(200, req.getAltID(), req.getChoiceID());
    }
}
