package caml.group.demo.http;

import java.util.ArrayList;

import caml.group.demo.model.Choice;

public class AddReportResponse {
    public boolean result;
    public int statusCode;
    public String error;
	public ArrayList<Choice> choices;
	
    public AddReportResponse(ArrayList<Choice> choices, int statusCode){
        this.result = true;
        this.choices = choices;
        this.statusCode = 200;
    }

    public AddReportResponse(int statusCode, String errorMessage){
        this.result = false;
        this.statusCode = statusCode;
        this.error = errorMessage;
    }

}
