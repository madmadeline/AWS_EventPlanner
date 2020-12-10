package caml.group.demo.http;

public class AddWinChoiceRequest {
    String altID;
    String choiceID;

    public void setAltID(String altID) {
        this.altID = altID;
    }

    public void setChoiceID(String choiceID) {
        this.choiceID = choiceID;
    }

    public String getAltID() {
        return altID;
    }

    public String getChoiceID() {
        return choiceID;
    }

    public String toString() { return "Add(" + altID + ")"; }
}
