package caml.group.demo.http;

public class AddSubmitFeedbackRequest {
    String altID;
    String userID;
    String feedback;
    boolean rating;

    public String getAltID() {
        return altID;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getUserID() {
        return userID;
    }

    public boolean getRating(){
        return rating;
    }

    public void setAltID(String altID) {
        this.altID = altID;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setRating(boolean rating) {
        this.rating = rating;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String toString() { return "Add(" + feedback + ")"; }
}
