package caml.group.demo.http;

import caml.group.demo.model.Choice;

public class AddCreateChoiceResponse {
    public boolean result;
    public int statusCode;
    public String error;

    public AddCreateChoiceResponse(int statusCode, Choice choice){
        this.result = choice != null;
        this.statusCode = statusCode;
        this.error = "";
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
