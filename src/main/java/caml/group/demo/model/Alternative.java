package caml.group.demo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class Alternative {
	String id;
	String description;
	int totalApprovals;
	int totalDisapprovals; 
	ArrayList<Feedback> feedback;


	public Alternative(String id, String description) {
		this.id = id;
		this.description = description;
		this.totalApprovals = 0;
		this.totalDisapprovals = 0;
		this.feedback  = new ArrayList<Feedback>();
	}

	public String getID() { return id; }
	public String getDescription() { return description;}



	public int getTotalDisapprovals() { 
		int answer = 0;
		for(Feedback rate : feedback) {
			if(rate.isDisapproved(rate.getApproved())) { answer++; }
		}
		totalDisapprovals = answer;
		return totalDisapprovals;
	}

	public int getTotalApprovals() { 
		int answer = 0;
		for(Feedback rate : feedback) {
			if(rate.isApproved(rate.getApproved())) { answer++; }
		}
		totalApprovals = answer;
		return totalApprovals;
	}

	public void setTotalApprovals(int totalApprovs) { totalApprovals = totalApprovs; }
	public void setTotalDisapprovals(int totalDisapprovs) { totalDisapprovals = totalDisapprovs; }


	public boolean sameChar(char a, char b) {
		int compare = Character.compare(a, b);
		if(compare == 0) {
			return true;
		}
		else {
			return false;
		}
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

	// iterators
	public Iterator<Feedback> feedbackIterator() { return feedback.iterator(); }

}
