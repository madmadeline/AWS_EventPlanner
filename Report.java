package caml.group.demo.model;

import java.sql.Timestamp;
//import java.time.ZonedDateTime;

public class Report {
	String choiceID;
	Timestamp timestamp;
	boolean isCompleted;
	

	public Report(String choiceID, Timestamp timestamp, boolean isCompleted) {
		this.choiceID = choiceID;
		this.timestamp = timestamp;
		this.isCompleted = isCompleted;
	}
	
	
	public String getChoiceID() { return choiceID; }
	public Timestamp getTimestamp() { return timestamp; }
	public boolean getIsCompleted() { return isCompleted; }
}
