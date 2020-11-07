// TODO have admin extend user?

package caml.group.demo.model;

import java.time.ZonedDateTime;
import java.time.LocalDateTime;

public class Admin {
	private final String userID;
	private final String password;
	

	public Admin() {
		this.userID = "Admin";
		this.password = "s0methingRandom?idk";
	}
	
	
	public boolean userPassCorrect(String user, String pass) {
		return (user.equals(userID) && pass.equals(password));
	}
	
	
	public Report createReport(int choiceID) {
		boolean isCompleted = false;
		// if choice has a winning alt, isCompleted = true
		
		return new Report(choiceID, ZonedDateTime.now(), isCompleted);
	}
	
	
	// TODO: either have user choose specific time (e.g. wed nov 14 @ 4PM EST) or choose N days (e.g. 2.5)
	public void deleteReports(double days) {
		// convert double to LocalDateTime type
		
		
		// for all choices in the system
			// convert choice timestamp to local time
			// is timestamp before given time?
				// true: delete 
		
	}
	
	
}
