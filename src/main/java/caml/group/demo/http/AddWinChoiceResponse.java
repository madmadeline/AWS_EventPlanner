package caml.group.demo.http;

public class AddWinChoiceResponse {
    public boolean result;
    public int statusCode;
    public String error;
    public String altID;
    public String choiceID;

    public AddWinChoiceResponse(int statusCode, String altID, String choiceID){
        this.result = true;
        this.statusCode = statusCode;
        this.altID = altID;
        this.choiceID = choiceID;
    }

    public AddWinChoiceResponse(int statusCode, String error){
        this.result = false;
        this.statusCode = statusCode;
        this.error = error;
    }
}
