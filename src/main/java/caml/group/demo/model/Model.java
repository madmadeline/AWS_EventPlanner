// Jyalu

package caml.group.demo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;


public class Model  implements Iterable<Choice>{
	ArrayList<Choice> choices; // TODO use SQL/RDS instead
	Admin admin;
	User currentUser;
	Choice currentChoice;
	Stack<Choice> choicesToCreate; // TODO use SQL/RDS instead
	Stack<User> usersToRegister; // TODO use SQL/RDS instead
	ArrayList<Report> reports;
	

	public Model(String adminName, String adminPass) {
		this.choices = new ArrayList<Choice>();
		this.admin = new Admin(adminName, adminPass, this);
		this.currentUser = null;
		this.currentChoice = null;
		choicesToCreate = new Stack<Choice>();
		usersToRegister = new Stack<User>();
	}
	
	// getters and setters
	public ArrayList<Choice> getChoices() { return this.choices; }
	public Admin getAdmin() { return this.admin; }
	public User getCurrentUser() { return this.currentUser; }
	public Choice getCurrentChoice() { return this.currentChoice; }
	
	
	public void addChoiceToSystem() {
		
	}
	
	
	
	/**
	 * If no choice is specified and the login credentials match the
	 * ones for the admin, the admin is logged in. If a choice is
	 * selected and the user exists, the user is logged in. If a choice
	 * is selected and the user doesn't exist, the user is registered.
	 * @param name, the given username
	 * @param pass, the given password
	 * @return true if the log in was successful, false otherwise
	 */
	public boolean logIn(String name, String pass) {
		// no choice is selected
		if (currentChoice == null) {
			// admin logged in
			if (admin.userPassCorrect(name, pass)) { 
				currentUser = admin; 
				return true;
			} 
			// invalid login
			else { currentUser = null; return false; } 
		}
		
		// choice is selected
		else {
			User thisUser = currentChoice.getUser(name);
			
			// user is registered in choice
			if (thisUser != null) { 
				// user logged in
				if (thisUser.getPassword() == pass) { 
					currentUser = thisUser; 
					return true;
				}
				// wrong password
				else { currentUser = null; return false; }
			}
			
			// user doesn't exist --> make new user
			else {
				registerUser(name, pass, currentChoice);
				// TODO confirm registration??
				return true;
			}
		}
	}
	
	
	public void registerUser(String name, String pass, Choice choice) {
		// TODO fix method to use stacks so two users don't accidentally
		// register with the same user name
		// TODO use RDS/SQL instead
		choice.addUser(new User(name, pass));
	}
	
	
	@Override
	public Iterator<Choice> iterator() {
		return choices.iterator();
	}
	

}
