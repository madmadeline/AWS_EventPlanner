package caml.group.demo.http;

import caml.group.demo.model.Feedback;

public class AddSubmitFeedbackMessageResponse {
    public boolean result;
    public int statusCode;
    public String error;
    public String altID;
    public String userID;
    public String username;
    public String message;

    public AddSubmitFeedbackMessageResponse(int statusCode, Feedback feedback){
        this.result = feedback != null;
        this.statusCode = statusCode;
        this.error = "";
        assert feedback != null;
        this.altID = feedback.getAltID();
        this.userID = feedback.getUserID();
        this.username = feedback.getUsername();
        this.message = feedback.getMessage();
    }

    public AddSubmitFeedbackMessageResponse(int statusCode, String errorMessage){
        this.result = false;
        this.statusCode = statusCode;
        this.error = errorMessage;
        this.message = null;
    }
}
