package caml.group.demo.http;

import caml.group.demo.model.User;

/** Arbitrary decision to make this a String and not a native double. */
public class AddLogInResponse {
	public boolean result;  // true: logged in
	public int statusCode;  // HTTP status code.
	public String error;
	
	public AddLogInResponse (User user, int statusCode) {
		this.result = user != null;
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public AddLogInResponse (int statusCode, String errorMessage) {
		this.result = false; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "Result(" + result + ")";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
