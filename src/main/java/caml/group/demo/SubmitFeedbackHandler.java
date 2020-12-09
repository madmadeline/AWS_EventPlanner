package caml.group.demo;

import java.sql.Timestamp;
import java.time.Instant;

import caml.group.demo.db.AlternativeDAO;
import caml.group.demo.db.FeedbackDAO;
import caml.group.demo.http.AddSubmitFeedbackRequest;
import caml.group.demo.http.AddSubmitFeedbackResponse;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import caml.group.demo.model.Feedback;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SubmitFeedbackHandler implements RequestHandler<AddSubmitFeedbackRequest,AddSubmitFeedbackResponse> {
	LambdaLogger logger;
	Alternative alternative;
	FeedbackDAO feedbackDAO;

	public boolean submitFeedback(Feedback feedback) throws Exception {
		char oldApproval;

		logger.log("In submitFeedback in SubmitFeedback Handler");

		feedbackDAO = new FeedbackDAO(logger);
		AlternativeDAO alternativeDAO = new AlternativeDAO(logger);
		logger.log("Retrieved daos");

		// get old alternative
//		System.out.println("alt id " + feedback.getAltID());
		alternative = alternativeDAO.getAlternativeByID(feedback.getAltID());
		logger.log("Alternative: " + alternative.getDescription());

		// store old approval
		oldApproval = feedbackDAO.getRating(feedback.getAltID(), feedback.getUserID());
		System.out.println("Old approval = " + oldApproval);

		// approve alternative
		if (feedback.getApproved() == 'A' && oldApproval != 'A') {
			logger.log("Adding approval");
			alternative.setTotalApprovals(alternative.getTotalApprovals() + 1);
			feedbackDAO.addRating(feedback.getAltID(), feedback.getUserID(), feedback.getApproved(), feedback.getMessage(),
					feedback.getTimeStamp());
			if (oldApproval == 'D') {
				logger.log("Getting rid of old disapproval");
				alternative.setTotalDisapprovals(alternative.getTotalDisapprovals() - 1);
			}
			alternativeDAO.updateAlternative(alternative);
			return true;
		}

		// disapprove alternative
		else if (feedback.getApproved() == 'D' && oldApproval != 'D') {
			logger.log("Adding disapproval");
			alternative.setTotalDisapprovals(alternative.getTotalDisapprovals() + 1);
			feedbackDAO.addRating(feedback.getAltID(), feedback.getUserID(), feedback.getApproved(), feedback.getMessage(),
					feedback.getTimeStamp());
			if (oldApproval == 'A') {
				logger.log("Getting rid of old approval");
				alternative.setTotalApprovals(alternative.getTotalApprovals() - 1);
			}
			alternativeDAO.updateAlternative(alternative);
			return true;
		}

		// clear rating
		else if (feedbackDAO.ratingExists(feedback.getAltID(), feedback.getUserID())) {
			logger.log("Clearing alternative");

			// delete disapproval
			if (feedback.getApproved() == 'D') {
				alternative.setTotalDisapprovals(alternative.getTotalDisapprovals() - 1);
				feedbackDAO.clearRating(feedback.getAltID(), feedback.getUserID());
			}

			// delete approval
			else {
				alternative.setTotalApprovals(alternative.getTotalApprovals() - 1);
				feedbackDAO.clearRating(feedback.getAltID(), feedback.getUserID());
			}
			return false;
		}
		else {
			feedbackDAO.addRating(feedback.getAltID(), feedback.getUserID(), feedback.getApproved(), feedback.getMessage(),
					feedback.getTimeStamp());
			return true;
		}
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
			if (!submitFeedback(feedback)) {
				fail = true;
				failMessage = "Duplicate approval";
			}
			logger.log("Submitted feedback");
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
