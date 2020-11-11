package caml.group.demo.model;


import java.util.ArrayList;
import java.time.ZonedDateTime;

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

	// getters
	public String getID() { return id; }
	public String getPassword() { return password; }


	public void createChoice(String description) {

	}

	public void submitFeedback(String message) {

	}

	public void rate(RatingType r) {

	}

	public void createChoice(int id, String description, ArrayList<Alternative> alternatives) {

		ZonedDateTime time = ZonedDateTime.now();

		Choice cnew = new Choice(id, description, alternatives, time);
	}

	/*public ArrayList<Alternative> addAlternatives(ArrayList<Alternative> alternatives, String destricption, int totalApprovals, int totalDisapprovals, ArrayList<ArrayList<String>> approversAndDisapprovers, ArrayList<Feedback> feedback){

		alternatives.add(new Alternative(destricption, totalApprovals, totalDisapprovals, approversAndDisapprovers, feedback));

		return alternatives;
	}*/



}
