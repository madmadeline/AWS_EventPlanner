package caml.group.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Alternative {
	String description;
	int totalApprovals;
	int totalDisapprovals;
	ArrayList<ArrayList<String>> approversAndDisapprovers = new ArrayList<ArrayList<String>>(); // 0th row approvers 1st row disapprover
	ArrayList<Feedback> feedback = new ArrayList<Feedback>();
	

	public Alternative( String description, int totalApprovals, int totalDisapprovals, ArrayList<ArrayList<String>> approversAndDisapprovers, ArrayList<Feedback> feedback) {
		this.description = description;
		this.totalApprovals = totalApprovals;
		this.totalDisapprovals = totalDisapprovals;
		this.approversAndDisapprovers = approversAndDisapprovers;
		this.feedback = feedback;	
	}
	
	public String getDestricption() { return destricption;}
	
	public int getTotalApprovals() { return totalApprovals;}
	public void setTotalApprovals(int totalApprovs) { 
		totalApprovals = totalApprovs;
		}
	
	public int getTotalDisapprovals() { return totalDisapprovals;}
	public void setTotalDisapprovals(int totalDisapprovs) { 
		totalDisapprovals = totalDisapprovs;
		}
	
	public ArrayList<ArrayList<String>> getApproversAndDisapprovers() { return approversAndDisapprovers;}
	
	public ArrayList<Feedback> getFeedback() { return feedback;}
	
	
}
