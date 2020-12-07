package caml.group.demo.http;

import caml.group.demo.model.Choice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class AddDeleteChoiceResponse {
    public boolean result;
    public int statusCode;
    public String error;
    public ArrayList<Choice> choices;
    public Timestamp time;

    public AddDeleteChoiceResponse(int statusCode, ArrayList<Choice> choices){
        this.result = true;
        this.statusCode = statusCode;
        this.error = "";
        this.choices = choices;
    }

    public AddDeleteChoiceResponse(int statusCode, String errorMessage){
        this.result = false;
        this.statusCode = statusCode;
        this.error = errorMessage;
    }

    public AddDeleteChoiceResponse(int statusCode, Timestamp time){
        this.result = true;
        this.statusCode = statusCode;
        this.error = String.valueOf(time);
        this.time = time;
    }
}
