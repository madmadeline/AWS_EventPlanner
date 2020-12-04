//Jyalu

package caml.group.demo.model;

import java.sql.Timestamp;


public class Feedback {
	final String altID;
	final String userID;
	char approved;  // the approval char is 'a' and the disapproval char is 'd'
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
	
	public String getAltID() { return altID; }
	public char getApproved() { return approved;}
	public String getMessage() { return message;}
	public Timestamp getTimeStamp() { return timestamp;}
	public String getUserID() { return userID;}

	public void setApproved(char approved) { approved = approved; }
	public void setMessage(String message) { message = message; }
	public void setTimeStamp(Timestamp timestamp) { timestamp = timestamp; }
	
	
	public boolean isApproved(char approved) {
		char a = 'a';
		if(sameChar(a, approved)) {
			return true;
		}
		else { return false;}
	}
	
	public boolean isDisapproved(char approved) {
		char d = 'd';
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
