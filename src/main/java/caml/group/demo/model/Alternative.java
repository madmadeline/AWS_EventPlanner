package caml.group.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Alternative {
	String id;
	String description;
	int totalApprovals;
	int totalDisapprovals; 
	ArrayList<Rating> ratings; // ArrayList of Rating. Rating holds 2 values User object and RatingType object - approve/disapprove
	ArrayList<Feedback> feedback;
	

	public Alternative(String id, String description) {
		this.id = id;
		this.description = description;
		this.totalApprovals = 0;
		this.totalDisapprovals = 0;
		this.ratings = new ArrayList<Rating>();
		this.feedback  = new ArrayList<Feedback>();
	}
	
	public String getID() { return id; }
	public String getDescription() { return description;}
	
	public int getTotalApprovals() { return totalApprovals;}
	public void setTotalApprovals(int totalApprovs) { totalApprovals = totalApprovs; }
	
	public int getTotalDisapprovals() { return totalDisapprovals;}
	public void setTotalDisapprovals(int totalDisapprovs) { totalDisapprovals = totalDisapprovs; }
	
	public ArrayList<Rating> getRatings() { return ratings;}
	public void addRator(Rating rator) { ratings.add(rator); } // fyi rator is noun and rater is verb
	public void removeRator(Rating rator) { ratings.remove(rator); }
	
	
	public ArrayList<Feedback> getFeedback() { return feedback;}
	
	
}
