package caml.group.demo.http;

import java.sql.Timestamp;

public class AddCreateChoiceRequest {
    String choiceID;
    String choiceDescription;
    String altID;
    String altDescription;
    Timestamp dateOfCreation;
    int numLikes = 0;
    int numDislikes = 0;

    public String getChoiceID() { return choiceID; }
    public void setChoiceID(String id) { this.choiceID = id; }

    public String getChoiceDescription() { return choiceDescription; }
    public void setChoiceDescription(String description) { this.choiceDescription = description; }

    public String getAltID() { return altID; }
    public void setAltID(String id) { this.altID = id; }

    public String getAltDescription() { return altDescription; }
    public void setAltDescription(String description) { this.altDescription = description; }

    public Timestamp getDateOfCreation() { return dateOfCreation; }
    public void setDateOfCreation(Timestamp timestamp) { this.dateOfCreation = timestamp; }

    public String toString() { return "Add(" + choiceID + ", " + dateOfCreation + ")"; }
}
