package caml.group.demo.http;

public class AddSubmitFeedbackRequest {
    String altID;
    String userID;
    String username;
    String feedback;
    char rating;

    public String getAltID() {
        return altID;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getUserID() {
        return userID;
    }

    public char getRating(){
        return rating;
    }

    public String getUsername() { return username; }

    public void setAltID(String altID) {
        this.altID = altID;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setRating(char rating) {
        this.rating = rating;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUsername(String username) { this.username = username; }

    public String toString() { return "Add(" + feedback + ")"; }
}
