package caml.group.demo.http;

import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import com.amazonaws.services.dynamodbv2.xspec.S;

import java.sql.Timestamp;
import java.util.ArrayList;

public class AddCreateChoiceResponse {
    public boolean result;
    public int statusCode;
    public String error;
    public String choiceID;
    public String description;
    public Timestamp time;
    public int maxTeamSize;
    public ArrayList<Alternative> alts;


    public AddCreateChoiceResponse(int statusCode, Choice choice){
        this.result = choice != null;
        this.statusCode = statusCode;
        this.error = "";
        assert choice != null;
        this.choiceID = choice.getID();
        this.description = choice.getDescription();
        this.time = choice.getTime();
        this.alts = choice.getAlternatives();
        this.maxTeamSize = choice.getMaxTeamSize();
    }

    public AddCreateChoiceResponse(int statusCode, String errorMessage){
        this.result = false;
        this.statusCode = statusCode;
        this.error = errorMessage;
    }

    public String toString(){
        if(statusCode == 200) return "Result(" + result + ")";
        else return "ErrorResult(" + statusCode + ", err=" + error + ")";
    }
}
