package caml.group.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Alternative {
	String id;
	String description;
	int totalApprovals;
	int totalDisapprovals;
	ArrayList<ArrayList<User>> approversAndDisapprovers; // 0th row approvers 1st row disapprover // changed String to User
	ArrayList<Feedback> feedback;
	

	public Alternative(String id, String description) {
		this.id = id;
		this.description = description;
		this.totalApprovals = 0;
		this.totalDisapprovals = 0;
		this.approversAndDisapprovers = new ArrayList<ArrayList<User>>();
		this.feedback  = new ArrayList<Feedback>();
	}
	
	public String getID() { return id; }
	public String getDescription() { return description;}
	
	public int getTotalApprovals() { return totalApprovals;}
	public void setTotalApprovals(int totalApprovs) { totalApprovals = totalApprovs; }
	
	public int getTotalDisapprovals() { return totalDisapprovals;}
	public void setTotalDisapprovals(int totalDisapprovs) { totalDisapprovals = totalDisapprovs; }
	
	public ArrayList<ArrayList<User>> getApproversAndDisapprovers() { return approversAndDisapprovers;}
	public void addApprover(User username) { approversAndDisapprovers.get(0).add(username); }
	public void removeApprover(User username) { approversAndDisapprovers.get(0).remove(username); }
	public void addDisapprover(User username) { approversAndDisapprovers.get(1).add(username); }
	public void removeDisapprover(User username) { approversAndDisapprovers.get(1).remove(username); }
	
	public ArrayList<Feedback> getFeedback() { return feedback;}
	
	
}
