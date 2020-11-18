package caml.group.demo.http;

/** To work with AWS must not have final attributes, must have no-arg constructor, and all get/set methods. */
public class AddLogInRequest {
	String username;
	String password;

	public String getUsername() { return username; }
	public void setUsername(String name) { this.username = name; }
	
	public String getPassword() { return password; }
	public void setPassword(String pass) { this.password = pass; }

	public String toString() {
		return "Add(" + username + "," + password + ")";
	}
	
	public AddLogInRequest (String user, String pass) {
		this.username = user;
		this.password = pass;
	}
	
	public AddLogInRequest() {
	}
}
