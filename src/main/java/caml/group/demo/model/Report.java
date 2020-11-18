package caml.group.demo.model;

import java.sql.Timestamp;
//import java.time.ZonedDateTime;

public class Report {
	int choiceID;
	Timestamp timestamp;
	boolean isCompleted;
	

	public Report(int choiceID, Timestamp timestamp, boolean isCompleted) {
		this.choiceID = choiceID;
		this.timestamp = timestamp;
		this.isCompleted = isCompleted;
	}
	
	
	public int getChoiceID() { return choiceID; }
	public Timestamp getTimestamp() { return timestamp; }
	public boolean getIsCompleted() { return isCompleted; }
}
