package caml.group.demo.http;

/** To work with AWS must not have final attributes, must have no-arg constructor, and all get/set methods. */
public class AddLogInRequest {
	String username;
	String password;
	String choiceID;

	public AddLogInRequest (String user, String pass, String choiceID) {
		// username is stored as "username_choiceID" in the User table
		this.username = user;
		this.password = pass;
		this.choiceID = choiceID;
	}

	public AddLogInRequest() { }

	public String getUsername() { return username; }
	public void setUsername(String name) { this.username = name; }
	
	public String getPassword() { return password; }
	public void setPassword(String pass) { this.password = pass; }

	public String getChoiceID() { return choiceID; }
	public void setChoiceID(String choiceID) { this.choiceID = choiceID; }

	public String toString() {
		return "Request: Add (" + username + "," + password + ") to Choice #" + choiceID + "\n";
	}
}
