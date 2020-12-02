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



	public int getTotalDisapprovals() { 
		int answer = 0;
		for(Rating rate : ratings) {
			if(rate.getIsDisapproval()) { answer++; }
		}
		totalDisapprovals = answer;
		return totalDisapprovals;
	}

	public int getTotalApprovals() { 
		int answer = 0;
		for(Rating rate : ratings) {
			if(rate.getIsApproval()) { answer++; }
		}
		totalApprovals = answer;
		return totalApprovals;
	}

	public void setTotalApprovals(int totalApprovs) { totalApprovals = totalApprovs; }
	public void setTotalDisapprovals(int totalDisapprovs) { totalDisapprovals = totalDisapprovs; }

	public ArrayList<Rating> getRatings() { return ratings;}
	
	public void addRating(Rating rator) { ratings.add(rator); } // fyi rator is noun and rater is verb
	public void removeRating(Rating rator) { ratings.remove(rator); }

	public void changeRating(Rating rator) { 
		int currentID = rator.getUserID();
		for(Rating rater : ratings) {
			if(helpEquals(rater.getUserID(), currentID)) { 
				removeRating(rater); 
			}
		}
		ratings.add(rator); 
	}

	public boolean helpEquals(int a, int b) {
		Integer var1 = Integer.valueOf(a);
		Integer var2 = Integer.valueOf(b);

		if(var2.intValue() == var1.intValue()) {
			return true;
		}
		else {
			return false;
		}
	}
	public ArrayList<Feedback> getFeedback() { return feedback;}


}
