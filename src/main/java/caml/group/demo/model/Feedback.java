//Jyalu

package caml.group.demo.model;

import java.time.ZonedDateTime;

public class Feedback {
	String message;
	ZonedDateTime timestamp;
	String userID;

	public Feedback(String message, ZonedDateTime timestamp, String userID) {
		this.message = message;
		this.timestamp = timestamp;
		this.userID = userID;
	}
	
	
}
