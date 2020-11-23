package caml.group.demo.http;

/** To work with AWS must not have final attributes, must have no-arg constructor, and all get/set methods. */
public class AddLogInRequest {
	String username;
	String password;
	int choiceID;

	public AddLogInRequest (String user, String pass, int choiceID) {
		this.username = user;
		this.password = pass;
		this.choiceID = choiceID;
	}

	public AddLogInRequest() { }

	public String getUsername() { return username; }
	public void setUsername(String name) { this.username = name; }
	
	public String getPassword() { return password; }
	public void setPassword(String pass) { this.password = pass; }

	public int getChoiceID() { return choiceID; }
	public void setChoiceID(int choiceID) { this.choiceID = choiceID; }

	public String toString() {
		return "Add (" + username + "," + password + ") to choice #" + choiceID;
	}
}
