package caml.group.demo.model;

import java.time.ZonedDateTime;

public class Report {
	int choiceID;
	ZonedDateTime timestamp;
	boolean isCompleted;
	

	public Report(int choiceID, ZonedDateTime timestamp, boolean isCompleted) {
		this.choiceID = choiceID;
		this.timestamp = timestamp;
		this.isCompleted = isCompleted;
	}
	
	
	public int getChoiceID() { return choiceID; }
	public ZonedDateTime getTimestamp() { return timestamp; }
	public boolean getIsCompleted() { return isCompleted; }
}
