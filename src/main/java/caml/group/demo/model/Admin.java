// Jyalu, Brandon

package caml.group.demo.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import caml.group.demo.model.Model;
import org.joda.time.DateTime;

import java.time.LocalDateTime;

public class Admin{
	Model model;
	
	public Admin(Model model) {

		this.model = model;
	}
	
	public void setModel(Model model) { this.model = model; }
	
	
	
	
	
	public Report createReport(Choice choice) {
		boolean isCompleted = false;
		int choiceID;
		
		choiceID = choice.getID();
		isCompleted = (choice.getWinner() != null);
		
		return new Report(choiceID, Timestamp.from(Instant.now()), isCompleted);
	}
	
	

	/**
	 * Deletes the number of reports older than the given number of days.
	 * @param days, the given number of days
	 * @return the number of reports that were deleted
	 */
	public int deleteReports(double days) {
		long numDays;
		long numHours;
		long numMinutes;
		int numReportsDeleted = 0;
		
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
			LocalDateTime choiceTime = choice.time.toLocalDateTime();
			
			if (choiceTime.isBefore(limit)) {
				choices.remove(choice);
				numReportsDeleted++;
			}
		}
		return numReportsDeleted;
	}
}
