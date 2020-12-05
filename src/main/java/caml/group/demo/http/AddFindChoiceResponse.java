package caml.group.demo.http;

import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;

import java.sql.Timestamp;
import java.util.ArrayList;

public class AddFindChoiceResponse {
    public boolean result;
    public int statusCode;
    public String error;
    public String choiceID;
    public String description;
    public int maxTeamSize;
    public Timestamp time;
    public ArrayList<Alternative> alternatives;
    //public Choice choice;

    public AddFindChoiceResponse(Choice choice, int statusCode){
        this.result = true;
        this.choiceID = choice.getID();
        this.description = choice.getDescription();
        this.alternatives = choice.getAlternatives();
        this.time = choice.getTime();
        this.statusCode = statusCode;
        this.maxTeamSize = choice.getMaxTeamSize();
    }

    public AddFindChoiceResponse(int statusCode, String errorMessage){
        this.result = false;
        this.statusCode = statusCode;
        this.error = errorMessage;
    }

    public String toString() {
        if (statusCode / 100 == 2) {  // too cute?
//            return "ChoiceID " + choiceID + " Description " + description;
            return "Response: Result (" + result + ")\n";
        } else {
            return "Response: ErrorResult(" + statusCode + ", err=" + error + ")\n";
        }
    }
}
