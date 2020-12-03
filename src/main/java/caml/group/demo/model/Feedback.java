//Jyalu

package caml.group.demo.model;

import java.sql.Timestamp;


public class Feedback {
	String altID;
	boolean approved;
	String message;
	Timestamp timestamp;
	String userID;

	public Feedback(String altID, boolean approved, String message, Timestamp timestamp, String userID) {
		this.altID = altID;
		this.approved = approved;		
		this.message = message;
		this.timestamp = timestamp;
		this.userID = userID;
	}
	
	public String getAltID() { return altID; }
	public boolean getApproved() { return approved;}
	public String getMessage() { return message;}
	public Timestamp getTimeStamp() { return timestamp;}
	public String getUserID() { return userID;}
	
	public void setAltID(String altIDb) { altID = altIDb; }
	public void setApproved(boolean approvedb) { approved = approvedb; }
	public void setMessage(String messageb) { message = messageb; }
	public void setTimeStamp(Timestamp timestampb) { timestamp = timestampb; }
	public void setUserID(String userIDb) { userID = userIDb; }
	
	
	
}
