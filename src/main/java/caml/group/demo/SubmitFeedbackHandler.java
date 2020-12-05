package caml.group.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import caml.group.demo.db.AlternativeDAO;
import caml.group.demo.db.FeedbackDAO;
import caml.group.demo.http.AddSubmitFeedbackRequest;
import caml.group.demo.http.AddSubmitFeedbackResponse;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.Feedback;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

public class SubmitFeedbackHandler implements RequestHandler<AddSubmitFeedbackRequest,AddSubmitFeedbackResponse> {
	LambdaLogger logger;
	Alternative alternative;
	FeedbackDAO feedbackDAO;

	public void submitFeedback(Feedback feedback) throws Exception {
		logger.log("In submitFeedback in SubmitFeedback Handler");
		feedbackDAO = new FeedbackDAO(logger);
		AlternativeDAO alternativeDAO = new AlternativeDAO(logger);

		int oldNumApprovals;

		boolean userChangedMind = feedbackDAO.feedbackExists(feedback.getAltID(), feedback.getUserID());

		logger.log("Retrieved dao");
		feedbackDAO.addFeedback(feedback.getAltID(), feedback.getUserID(), feedback.getApproved(), feedback.getMessage(),
				feedback.getTimeStamp());

		// update the alternative
		alternative = alternativeDAO.getAlternativeByID(feedback.getAltID());
		if (feedback.getApproved() == 'A') { // approve alternative
			alternative.setTotalApprovals(alternative.getTotalApprovals() + 1);
			if (userChangedMind) {
				alternative.setTotalDisapprovals(alternative.getTotalDisapprovals() - 1);
			}
			alternativeDAO.updateAlternative(alternative, true, true);
		} else if (feedback.getApproved() == 'D') { // disapprove alternative
			alternative.setTotalApprovals(alternative.getTotalDisapprovals() + 1);
			if (userChangedMind) {
				alternative.setTotalApprovals(alternative.getTotalApprovals() - 1);
			}
			alternativeDAO.updateAlternative(alternative, true, false);
		}
		// TODO for iteration 3: update alternative when the feedback is just a message

	}

	@Override
    public AddSubmitFeedbackResponse handleRequest(AddSubmitFeedbackRequest req, Context context) {
    	logger = context.getLogger();
		logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

		boolean fail = false;
		String failMessage = "";
		Timestamp time = Timestamp.from(Instant.now());

		Feedback feedback = new Feedback(req.getAltID(), req.getUserID(), req.getUsername(),
				req.getRating(), req.getFeedback(), time);

		try{
			submitFeedback(feedback);
		}catch (Exception e){
			fail = true;
			failMessage = "Failed to submit feedback";
		}

		AddSubmitFeedbackResponse response;
		if(fail) response = new AddSubmitFeedbackResponse(400, failMessage);
		else response = new AddSubmitFeedbackResponse(200, feedback);
		return response;
    }
}
