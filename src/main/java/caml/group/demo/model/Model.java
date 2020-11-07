// Jyalu

package caml.group.demo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;


// TODO stack for making choices
// TODO stack for making users

public class Model  implements Iterable<Choice>{
	ArrayList<Choice> choices; // TODO use SQL/RDS instead
	Admin admin;
	User currentUser;
	Stack<Choice> choicesToCreate;
	Stack<User> usersToRegister;
	

	public Model(ArrayList<Choice> choices, Admin admin) {
		this.choices = choices;
		this.admin = admin;
		this.currentUser = null;
		choicesToCreate = new Stack<Choice>();
		usersToRegister = new Stack<User>();
	}
	
	
	public ArrayList<Choice> getChoices() { return choices;}
	
	// user functions
		// register user
		// create choice
		// join choice
		// rate alternative
		// submit feedback
		
		
	// admin functions
		// create report
		// delete reports
	
	@Override
	public Iterator<Choice> iterator() {
		return choices.iterator();
	}
	

}
