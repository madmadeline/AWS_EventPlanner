package caml.group.demo.model;

public class User {
	final String id;
	final String name;
	String password; //Optional


	/**
	 * Constructor for User
	 * @param name the given username
	 * @param password the given password (can be "")
	 */
	public User(String id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}
	
	// getters
	public String getID() { return id; }
	public String getName() { return name; }
	public String getPassword() { return password; }
	
	
	public void createChoice(String description) {
		
	}
	
	public void submitFeedback(String message) {
		
	}
	
	public void rate(RatingType r) {
		
	}
	
	
}
