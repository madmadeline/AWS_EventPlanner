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
    public ArrayList<Alternative> alts;

    public AddFindChoiceResponse(Choice choice, int statusCode){
        this.result = true;
        this.choiceID = choice.getID();
        this.description = choice.getDescription();
        this.alts = choice.getAlternatives();
        this.time = choice.getTime();
        this.statusCode = 200;
        this.maxTeamSize = choice.getMaxTeamSize();
    }

    public AddFindChoiceResponse(int statusCode, String errorMessage){
        this.result = false;
        this.statusCode = statusCode;
        this.error = errorMessage;
    }
}
