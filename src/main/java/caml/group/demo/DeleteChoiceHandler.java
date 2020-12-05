package caml.group.demo;

import caml.group.demo.http.AddDeleteChoiceRequest;
import caml.group.demo.http.AddDeleteChoiceResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class DeleteChoiceHandler implements RequestHandler<AddDeleteChoiceRequest, AddDeleteChoiceResponse> {
    LambdaLogger logger;

    @Override
    public AddDeleteChoiceResponse handleRequest(AddDeleteChoiceRequest req, Context context){
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
        logger.log(req.toString());

        boolean fail = false;
        String failMessage = "";


        return null;
    }
}
