package caml.group.demo.http;

import caml.group.demo.db.UserDAO;
import caml.group.demo.model.Feedback;

public class AddSubmitFeedbackResponse {
    public boolean result;
    public int statusCode;
    public String error;
    public String altID;
    public String userID;
    public String feedback;
    public String username;
    public char rating;

    public AddSubmitFeedbackResponse(int statusCode, Feedback feedback){
        this.result = feedback != null;
        this.statusCode = statusCode;
        this.error = "";
        assert feedback != null;
        this.altID = feedback.getAltID();
        this.userID = feedback.getUserID();
        this.username = feedback.getUsername();
        this.feedback = feedback.getMessage();
        this.rating = feedback.getApproved();
    }

    public AddSubmitFeedbackResponse(int statusCode, String errorMessage){
        this.result = false;
        this.statusCode = statusCode;
        this.error = errorMessage;
        this.rating = 'F';
    }
}
