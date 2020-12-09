package caml.group.demo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.sql.Timestamp;

//import java.time.ZonedDateTime;

public class Choice {
	final String id;
	final String description;
	Timestamp time;
	Alternative winner;
	String winnerName;
	final ArrayList<Alternative> alternatives;// = new ArrayList<Alternative>();
	ArrayList<User> users;
	final int maxTeamSize;


	public Choice(String id, String description, ArrayList<Alternative> alternatives, Timestamp time, int teamSize) {
		this.id = id;
		this.description = description;
		this.alternatives = alternatives;
		this.time = time;
		this.winner = null;
		this.users = new ArrayList<>();
		this.maxTeamSize = teamSize;
	}
	

	public Choice(String id, String description, Timestamp time, int teamSize) {
		this.id = id;
		this.description = description;
		this.alternatives = null;
		this.time = time;
		this.winner = null;
		this.users = new ArrayList<>();
		this.maxTeamSize = teamSize;
	}
	
	public String getID() { return this.id; }
	public int getMaxTeamSize() { return this.maxTeamSize; };
	public ArrayList<Alternative> getAlternatives() {
		return alternatives;
	}

	public boolean getChoiceIDbyAltID(String altID) {
		//String ans = null;
		boolean ans = false;
		for(Alternative alt: alternatives) {
			if(altID.equals(alt.getID())) {
				//ans = this.id;
				ans = true;
			}
		}
		return ans;
	}
	
//	public void setMaxTeamSize(int teamSize){ this.maxTeamSize = teamSize; }

	public String getDescription(){
		return description;
	}
	public Timestamp getTime(){
		return time;
	}
	public Alternative getWinner() { return this.winner; }
	public void setWinner(Alternative winner) { this.winner = winner; }
	public ArrayList<User> getUsers() { return users; }

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public void setWinnerName(String winnerName) {
		this.winnerName = winnerName;
	}

	public String getWinnerName() {
		return winnerName;
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
			if (user.getID().equals(id)) { return user; }
		}
		return null;
	}
	
	
	
	
	// iterators
	public Iterator<User> userIterator() { return users.iterator(); }
	public Iterator<Alternative> altsIterator() { return alternatives.iterator(); }
}

