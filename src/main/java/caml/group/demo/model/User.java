package caml.group.demo.model;

public class User {
	String id;
	String password; //Optional
	/**
	 * Constructor for User
	 * @param id, the given username
	 * @param password, the given password (can be "")
	 */
	public User(String id, String password) {
		this.id = id;
		this.password = password;
	}
	
	// getters
	public String getID() { return id; }
	public String getPassword() { return password; }
	
	
	public void createChoice(String description) {
		
	}
	
	public void submitFeedback(String message) {
		
	}
	
	public void rate(RatingType r) {
		
	}
	
	
}
