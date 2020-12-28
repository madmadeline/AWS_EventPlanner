package caml.group.demo;

import java.sql.Timestamp;
import java.time.Instant;

import caml.group.demo.db.AlternativeDAO;
import caml.group.demo.db.FeedbackDAO;
import caml.group.demo.http.AddSubmitFeedbackMessageRequest;
import caml.group.demo.http.AddSubmitFeedbackMessageResponse;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.Feedback;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SubmitFeedbackMessageHandler implements RequestHandler<AddSubmitFeedbackMessageRequest,
        AddSubmitFeedbackMessageResponse> {
    LambdaLogger logger;
    Alternative alternative;
    FeedbackDAO feedbackDAO;
    AlternativeDAO alternativeDAO;

    /**
     * Adds the given feedback to the Message table.
     * @param feedback The given feedback
     * @return True if the feedback was added, false otherwise
     * @throws Exception There was an error in adding the feedback
     */
    public boolean submitFeedbackMessage(Feedback feedback) throws Exception {
        logger.log("In submitFeedbackMessage in SubmitFeedbackMessageHandler");

        // did the user actually specify a message?
        if (feedback.getMessage() == null || feedback.getMessage() == "") {
            return false;
        }

        // connect to the database, retrieve the DAOs
        feedbackDAO = new FeedbackDAO(logger);
        alternativeDAO = new AlternativeDAO(logger);
//        logger.log("Retrieved dao");

        // get old alternative
//        logger.log("")
//        alternative = alternativeDAO.getAlternativeByID(feedback.getAltID());

        // this method inserts into or updates the Message table
        return feedbackDAO.addMessage(feedback.getAltID(), feedback.getUserID(),
                    feedback.getMessage(), feedback.getTimeStamp());
    }

    @Override
    public AddSubmitFeedbackMessageResponse handleRequest(AddSubmitFeedbackMessageRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
        logger.log(req.toString());

        boolean fail = false;
        String failMessage = "";
        Timestamp time = Timestamp.from(Instant.now());
        AddSubmitFeedbackMessageResponse response;

        // check if empty string
        if (req.getMessage().equals("")) {
            failMessage = "Message is an empty string";
            response = new AddSubmitFeedbackMessageResponse(400, failMessage);
            return response;
        }

        // check if message is too long
        if (req.getMessage().length() > 500) {
            failMessage = "The message is too long";
            response = new AddSubmitFeedbackMessageResponse(400, failMessage);
            return response;
        }

        Feedback feedback = new Feedback(req.getAltID(), req.getUserID(), req.getUsername(), (char)0,
                req.getMessage(), time);

        try {
            submitFeedbackMessage(feedback);
        } catch (Exception e) {
            fail = true;
            failMessage = "Failed to submit feedback message";
        }

        if(fail) response = new AddSubmitFeedbackMessageResponse(400, failMessage);
        else response = new AddSubmitFeedbackMessageResponse(200, feedback);
        return response;
    }
}
