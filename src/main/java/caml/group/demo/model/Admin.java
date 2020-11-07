// Jyalu

package caml.group.demo.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Admin extends User{
	private final String userID;
	private final String password;
	Model model;
	

	public Admin(Model model) {
		this.userID = "Admin";
		this.password = "s0methingRandom?idk";
		this.model = model;
	}
	
	
	public boolean userPassCorrect(String user, String pass) {
		return (user.equals(userID) && pass.equals(password));
	}
	
	
	public Report createReport(Choice choice) {
		boolean isCompleted = false;
		int choiceID = 0;
		
//		int choiceID = choice.getID();
		
		// if choice has a winning alt, isCompleted = true
		
		return new Report(choiceID, ZonedDateTime.now(), isCompleted);
	}
	
	

	public void deleteReports(double days) {
		long numDays;
		long numHours;
		long numMinutes;
		
		LocalDateTime limit = LocalDateTime.now();
		ArrayList<Choice> choices = model.choices;	// TODO use RDS/SQL for choices
	
		// convert double to LocalDateTime type
		numDays = (long) days;
		limit.minusDays(numDays);
		
		days = (days - numDays) * 24;
		numHours = (long) days;
		limit.minusHours(numHours);
		
		days = (days - numHours) * 1440;
		numMinutes = (long) days;
		limit.minusMinutes(numMinutes);
		
		
		// delete old choices
		for (Choice choice : choices) {
			// TODO replace this line with next line after brandon uploads his stuff
			LocalDateTime choiceTime = LocalDateTime.now(); 
//			LocalDateTime choiceTime = choice.timestamp.toLocalDateTime();
			
			if (choiceTime.isBefore(limit)) {
				choices.remove(choice);
			}
		}
	}
}
