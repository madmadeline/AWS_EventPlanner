package caml.group.demo.model;

public class User {
	String id;
	String password; //Optional

	public User(String id) {
		this.id = id;
	}
	
	public User(String id, String password) {
		this.id = id;
		this.password = password;
	}
}
