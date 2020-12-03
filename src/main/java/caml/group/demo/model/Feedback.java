//Jyalu

package caml.group.demo.model;

import java.sql.Timestamp;


public class Feedback {
	final String altID;
	final String userID;
	char approved;
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
	
	
	
}
