package caml.group.demo.http;

import caml.group.demo.model.Choice;
import caml.group.demo.model.User;

/** Arbitrary decision to make this a String and not a native double. */
public class AddLogInResponse {
	public boolean result;  // true: logged in
	public String username;
	public String password;
	public String userID;
	public Choice choice;
	public int statusCode;  // HTTP status code.
	public String error;
	
	public AddLogInResponse (User user, Choice choice, int statusCode) { // 200
		this.result = user != null;
//		System.out.println("this changed");
		this.username = user.getName();
		this.password = user.getPassword();
		this.userID = user.getID();
		this.choice = choice;
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public AddLogInResponse (int statusCode, String errorMessage) { // 400
		this.result = false; // doesn't matter since error
		this.username = "";
		this.password = "";
		this.userID = "";
		this.choice = null;
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "Response: Result (" + result + ")\n";
		} else {
			return "Response: ErrorResult(" + statusCode + ", err=" + error + ")\n";
		}
	}
}
