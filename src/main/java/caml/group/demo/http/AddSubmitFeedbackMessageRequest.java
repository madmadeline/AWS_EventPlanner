package caml.group.demo.http;

import java.sql.Timestamp;

public class AddSubmitFeedbackMessageRequest {
    String altID;
    String userID;
    String username;
    String message;
    Timestamp timeStamp;

    public String getAltID() {
        return altID;
    }
    public String getUserID() {
        return userID;
    }
    public String getUsername() {
        return username;
    }
    public String getMessage(){
        return message;
    }
    public Timestamp getTimeStamp() { return timeStamp; }

    public void setAltID(String altID) {
        this.altID = altID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setUsername(String username) { this.username = username; }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setTimeStamp(Timestamp time) {
        this.timeStamp = time;
    }

    public String toString() { return username + " is adding (" + message + ") " +
            "to alternative " + altID; }
}
