package caml.group.demo;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.http.AddDeleteChoiceRequest;
import caml.group.demo.http.AddDeleteChoiceResponse;
import caml.group.demo.model.Choice;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DeleteChoiceHandler implements RequestHandler<AddDeleteChoiceRequest, AddDeleteChoiceResponse> {
    LambdaLogger logger;

    public ArrayList<Choice> deleteChoice(Timestamp timestamp) throws Exception {
        ChoiceDAO dao = new ChoiceDAO(logger);
        return dao.deleteChoice(timestamp);
    }

    @Override
    public AddDeleteChoiceResponse handleRequest(AddDeleteChoiceRequest req, Context context){
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
        logger.log(req.toString());

        boolean fail = false;
        String failMessage = "";
        ArrayList<Choice> choices = null;

        Timestamp currentTime = Timestamp.from(Instant.now());
        Long currentTimeMilli = currentTime.getTime();
        Double removalTimeMilli = req.getDays() * (8.64*(Math.pow(10, 7)));
        long deletionTimeMilli = Double.valueOf(currentTimeMilli - removalTimeMilli).longValue();
        Timestamp deletionTime = new Timestamp(deletionTimeMilli);

        try{
            choices = deleteChoice(deletionTime);
        }
        catch (Exception e){
            fail = true;
            failMessage = "Failed to delete choices";
        }

        if (fail) return new AddDeleteChoiceResponse(400, failMessage);
        else return new AddDeleteChoiceResponse(200, choices);


    }
}
