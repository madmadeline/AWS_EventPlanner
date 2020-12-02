package caml.group.demo;

import java.util.ArrayList;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import caml.group.demo.db.ChoiceDAO;
import caml.group.demo.http.AddReportRequest;
import caml.group.demo.http.AddReportResponse;
import caml.group.demo.model.Choice;

public class ReportHandler implements RequestHandler<AddReportRequest,AddReportResponse> {
	LambdaLogger logger;
	
    public ArrayList<Choice> getReport() throws Exception {
        ArrayList<Choice> choices = null;
        logger.log("Getting dao");
        ChoiceDAO dao = new ChoiceDAO(logger);
        logger.log("Got dao. Getting choice");
        choices = dao.getReport();
        return choices;
    }
	
	
	@Override
	public AddReportResponse handleRequest(AddReportRequest req, Context context) {
		logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
        logger.log(req.toString());

        boolean fail = false;
        String failMessage = "";
        ArrayList<Choice> choices = null;

        try{
            choices = getReport();
            for (int i = 0; i < choices.size(); i++) {
            	//print the choice
            	logger.log(choices.get(i).getDescription());
            }
        } catch (Exception e) {
            fail = true;
            failMessage = "Failed to generate report";
        }

        AddReportResponse response;
        if(fail) response = new AddReportResponse(400, failMessage);
        else response = new AddReportResponse(choices, 200);

        return response;
	}

}
