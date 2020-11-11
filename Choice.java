package caml.group.demo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.time.ZonedDateTime;

public class Choice {
	final int id;
	final String description;
	final ArrayList<Alternative> alternatives;// = new ArrayList<Alternative>();
	ArrayList<User> users;
	ZonedDateTime time;
	Alternative winner;

	public Choice(int id, String description, ArrayList<Alternative> alternatives, ZonedDateTime time) {
		this.id = id;
		this.description = description;
		this.alternatives = alternatives;
		this.time = time;
		this.winner = null;
	}
	
	public int getID() { return id;}
	
	public String getDescription() { return description;}
	
	public ArrayList<Alternative> getAlternatives() { return alternatives;}
	
	public ArrayList<User> getUsers() { return this.users; }
	
	public ZonedDateTime getDate() { return time;}
	
	public Alternative getWinner() { return winner;}
	public void setWinner(Alternative win) { 
		winner = win;
		}
	
public void addUser(User user) { users.add(user); }
	
	
	/**
	 * Returns the user with the given id if they are
	 * registered in this choice.
	 * @param id, the given user id
	 * @return the user if they are registered, null otherwise
	 */
	public User getUser(String id) {
		for (User user : users) {
			if (user.getID() == id) { return user; }
		}
		return null;
	}
	
	
	
	
	// iterators
	public Iterator<User> userIterator() { return users.iterator(); }
	public Iterator<Alternative> altsIterator() { return alternatives.iterator(); }
}
