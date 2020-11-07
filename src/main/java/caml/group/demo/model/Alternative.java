package caml.group.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Alternative {
	String destricption;
	int totalApprovals;
	int totalDisapprovals;
	ArrayList<ArrayList<String>> approversAndDisapprovers = new ArrayList<ArrayList<String>>(); // 0th row approvers 1st row disapprover
	ArrayList<Feedback> feedback = new ArrayList<Feedback>();
	

	public Alternative( String destricption, int totalApprovals, int totalDisapprovals, ArrayList<ArrayList<String>> approversAndDisapprovers, ArrayList<Feedback> feedback) {
		this.destricption = destricption;
		this.totalApprovals = totalApprovals;
		this.totalDisapprovals = totalDisapprovals;
		this.approversAndDisapprovers = approversAndDisapprovers;
		this.feedback = feedback;	
	}
	
}
