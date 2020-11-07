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
}
