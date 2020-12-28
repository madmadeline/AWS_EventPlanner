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
	ArrayList<String> totalDisapprovalUsers;
	ArrayList<String> totalApprovalUsers;
	int totalRatings;

	public Alternative(String id, String description) {
		this.id = id;
		this.description = description;
		this.totalApprovals = 0;
		this.totalDisapprovals = 0;
		this.feedback  = new ArrayList<Feedback>();
	}

//	public Alternative(String id, String description, int totalApprovals, int totalDisapprovals) {
//		this.id = id;
//		this.description = description;
//		this.totalApprovals = totalApprovals;
//		this.totalDisapprovals = totalDisapprovals;
//		this.feedback  = new ArrayList<Feedback>();
//		this.totalRatings = totalApprovals + totalDisapprovals;
//	}

	public Alternative(String id, String description, int totalApprovals, int totalDisapprovals,
					   ArrayList<Feedback> feedback, ArrayList<String> totalApprovalUsers,
					   ArrayList<String> totalDisapprovalUsers) {
		this.id = id;
		this.description = description;
		this.totalApprovals = totalApprovals;
		this.totalDisapprovals = totalDisapprovals;
		this.feedback  = feedback;
		this.totalRatings = totalApprovals + totalDisapprovals;
		this.totalApprovalUsers = totalApprovalUsers;
		this.totalDisapprovalUsers = totalDisapprovalUsers;
	}

	public String getID() { return id; }
	public String getDescription() { return description;}

	public int getTotalRatings() {
		int answer = 0;
		answer = getTotalDisapprovals() + getTotalApprovals();
		
		return answer;
	}

	public int getTotalDisapprovals() {
		return totalDisapprovals;
	}

	public int getTotalApprovals() {
		return totalApprovals;
	}

	public ArrayList<String> getTotalDisapprovalUsers() { 
		return totalDisapprovalUsers;
	}

	public ArrayList<String> getTotalApprovalUsers() { 
		return totalApprovalUsers;
	}
	
	public void setTotalApprovals(int totalApprovs) {
		this.totalApprovals = totalApprovs;
		this.totalRatings = totalApprovals + totalDisapprovals;
	}
	public void setTotalDisapprovals(int totalDisapprovs) {
		this.totalDisapprovals = totalDisapprovs;
		this.totalRatings = totalApprovals + totalDisapprovals;
	}


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

	public void setTotalApprovalUsers(ArrayList<String> totalApprovalUsers) {
		this.totalApprovalUsers = totalApprovalUsers;
	}

	public void setTotalDisapprovalUsers(ArrayList<String> totalDisapprovalUsers){
		this.totalDisapprovalUsers = totalDisapprovalUsers;
	}

	public void addFeedback(Feedback feedback) { this.feedback.add(feedback); }
	public ArrayList<Feedback> getFeedback() { return feedback;}

	// iterators
	public Iterator<Feedback> feedbackIterator() { return feedback.iterator(); }

}
