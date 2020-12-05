//Jyalu

package caml.group.demo.model;

import java.sql.Timestamp;


public class Feedback {
	final String altID;
	final String userID;
	char approved;  // the approval char is 'A' and the disapproval char is 'D'
	String message;
	Timestamp timestamp;

	// general constructor
	public Feedback(String altID, String userID, char approved, String message, Timestamp timestamp) {
		this.altID = altID;
		this.approved = approved;
		this.message = message;
		this.timestamp = timestamp;
		this.userID = userID;
	}

	// constructor for approving and disapproving
	public Feedback(String altID, String userID, char approved) {
		this.altID = altID;
		this.approved = approved;
		this.message = null;
		this.timestamp = null;
		this.userID = userID;
	}

	// constructor for leaving message on alternative
	public Feedback(String altID, String userID, String message, Timestamp timestamp) {
		this.altID = altID;
		this.approved = 0;
		this.message = message;
		this.timestamp = timestamp;
		this.userID = userID;
	}
	
	public String getAltID() { return altID; }
	public char getApproved() { return approved;}
	public String getMessage() { return message;}
	public Timestamp getTimeStamp() { return timestamp;}
	public String getUserID() { return userID;}

	public void setApproved(char approved) { approved = approved; }
	public void setMessage(String message) { message = message; }
	public void setTimeStamp(Timestamp timestamp) { timestamp = timestamp; }
	
	public boolean isApproved(char approved) {
		char a = 'A';
		if(sameChar(a, approved)) {
			return true;
		}
		else { return false;}
	}
	
	public boolean isDisapproved(char approved) {
		char d = 'D';
		if(sameChar(d, approved)) {
			return true;
		}
		else { return false;}
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
	
}
