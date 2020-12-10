package caml.group.demo.db;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import caml.group.demo.model.Feedback;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import caml.group.demo.model.Alternative;


/**
 * Note that CAPITALIZATION matters regarding the table name. If you create with 
 * a capital "User" then it must be "User" in the SQL queries.
 *
 */
public class AlternativeDAO { 
	LambdaLogger logger;
	java.sql.Connection conn;
	final String tblName = "Alternative";   // Exact capitalization
	final String fbTbl = "Feedback";
	final String msgTbl = "Message";

	public AlternativeDAO(LambdaLogger logger) {
		this.logger = logger;
		try  {
			conn = DatabaseUtil.connect();
			logger.log("Connected to database in AlternativeDAO");
		} catch (Exception e) {
			conn = null;
		}
	}


	/**
	 * Returns an Alternative object representing the entry in the Alternative
	 * table with the given Alternative description.
	 * @param desc, the given description
	 * @return the Alternative object
	 * @throws Exception
	 */
	public Alternative getAlternative(String desc) throws Exception {
		PreparedStatement ratings_ps;
		PreparedStatement messages_ps;
		ResultSet ratings_rs;
		ResultSet messages_rs;

		try {
			Alternative alt = null; // User object representing the database entry
			//            boolean passwordCorrect = true;

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName + 
					" WHERE description=?;");
			ps.setString(1,  desc);
			ResultSet resultSet = ps.executeQuery(); // cursor that points to database row

			while (resultSet.next()) {
				ratings_ps = conn.prepareStatement("SELECT * FROM " + fbTbl + "" +
						" WHERE altID=?;");
				ratings_ps.setString(1, resultSet.getString("altID"));
				ratings_rs = ratings_ps.executeQuery();

				// messages result set
				messages_ps = conn.prepareStatement("SELECT * FROM " + msgTbl + "" +
						" WHERE altID=?;");
				messages_ps.setString(1, resultSet.getString("altID"));
				messages_rs = messages_ps.executeQuery();

				alt = generateAlternative(resultSet, ratings_rs, messages_rs); // should only loop 1x
			}
			resultSet.close();
			ps.close();

			if (logger != null) { logger.log("retrieved all alternatives"); }
			return alt;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting user: " + e.getMessage());
		}
	}

	public ArrayList<Alternative> getAllAlternativesByChoiceID(String choiceID) throws Exception {
		ArrayList<Alternative> alts = new ArrayList<Alternative>();
		PreparedStatement ratings_ps;
		PreparedStatement messages_ps;
		ResultSet ratings_rs;
		ResultSet messages_rs;

		logger.log("getting the alts");

		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName +
					" WHERE choiceID=?;");
			ps.setString(1,  choiceID);
			ResultSet resultSet = ps.executeQuery(); // cursor that points to database row


			// for each alternative
			while (resultSet.next()) {
				logger.log("Got alt: " + resultSet.getString("description"));

				// ratings result set
				ratings_ps = conn.prepareStatement("SELECT * FROM " + fbTbl +
						" WHERE altID=?;");
				ratings_ps.setString(1, resultSet.getString("altID"));
				ratings_rs = ratings_ps.executeQuery();

				// messages result set
				messages_ps = conn.prepareStatement("SELECT * FROM " + msgTbl +
						" WHERE altID=?;");
				messages_ps.setString(1, resultSet.getString("altID"));
				messages_rs = messages_ps.executeQuery();
				alts.add(generateAlternative(resultSet, ratings_rs, messages_rs));
			}
			resultSet.close();
			ps.close();

			logger.log("Retrieved all alternatives");
			return alts;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting alternatives: " + e.getMessage());
		}
	}

	/**
	 * Gets an alternative from the database when given an id
	 * @param id, id of the requested alt
	 * @return the requested alt
	 * @throws Exception, how it fails
	 */
	public Alternative getAlternativeByID(String id) throws Exception {
		Alternative alt = null; // User object representing the database entry
		PreparedStatement alt_ps;
		PreparedStatement ratings_ps;
		PreparedStatement messages_ps;
		ResultSet alt_rs;
		ResultSet ratings_rs;
		ResultSet messages_rs;

		logger.log("getting alternative by id");

		alt_ps = conn.prepareStatement("SELECT * FROM " + tblName +
				" WHERE altID=?;");
		alt_ps.setString(1,  id);
		alt_rs = alt_ps.executeQuery();
		logger.log("got alternative result set");

		// ratings result set
		ratings_ps = conn.prepareStatement("SELECT * FROM " + fbTbl + " WHERE altID=?;");
		ratings_ps.setString(1,  id);
		ratings_rs = ratings_ps.executeQuery();
		logger.log("got ratings result set");

		// messages result set
		messages_ps = conn.prepareStatement("SELECT * FROM " + msgTbl + " WHERE altID=?;");
		messages_ps.setString(1, id);
		messages_rs = messages_ps.executeQuery();
		logger.log("got messages result set");


		logger.log("Generating alternative");
		while (alt_rs.next()) {
			// this method adds both the ratings and the messages to the alternative
			alt = generateAlternative(alt_rs, ratings_rs, messages_rs);
		}
		alt_rs.close();
		ratings_rs.close();
		alt_ps.close();
		ratings_ps.close();

		logger.log("Retrieved alternative " + id);

		return alt;
	}

	
	/**
	 * Adds or removes an approval or disapproval to the row corresponding to 
	 * the given Alternative.
	 * @param alt The given Alternative
	 * @return True if the update was successful, false otherwise
	 * @throws Exception
	 */
	public boolean updateAlternative(Alternative alt) throws Exception {
		try {
			String query = "UPDATE " + tblName + " SET numLikes=?, numDislikes=? where altID=?;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, alt.getTotalApprovals());
			ps.setInt(2, alt.getTotalDisapprovals());
			ps.setString(3, alt.getID());
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);
		} catch (Exception e) {
			throw new Exception("Failed to update alternative: " + e.getMessage());
		}
	}


	/**
	 * Deletes all alternatives associated with the given choice ID.
	 * @param choiceID The given choice ID
	 * @return true if the deletion was a success, false otherwise
	 * @throws Exception, failed to delete alternative
	 */
	public boolean deleteAlternatives(String choiceID) throws Exception {
		FeedbackDAO feedbackDAO = new FeedbackDAO(logger);
		ArrayList<Feedback> feedbacks;
		ArrayList<Alternative> alts = getAllAlternativesByChoiceID(choiceID);

		logger.log("Deleting alts for choice " + choiceID);

		// for each alternative in the choice
		for (Alternative alternative : alts) {
			// delete feedback associated with each alternative (rating + message)
			feedbacks = alternative.getFeedback();
			for (Feedback feedback : feedbacks) {
				logger.log("About to delete feedback");
				feedbackDAO.deleteFeedback(feedback);
			}

			// delete the alternative
			try {
				logger.log("Trying to delete the alternative");
				PreparedStatement ps = conn.prepareStatement("DELETE FROM " + tblName +
						" WHERE altID=? AND choiceID=?;");
				ps.setString(1, alternative.getID());
				ps.setString(2, choiceID);
				ps.executeUpdate();
				ps.close();

			} catch (Exception e) {
				throw new Exception("Failed to delete alternatives: " + e.getMessage());
			}
		}
		return true;
	}



	// TESTED
	/**
	 * Adds the given Alternative object to the Alternative table.
	 * @param alt, the given Alternative object
	 * @return true if the addition was a success, false otherwise
	 * @throws Exception, failed to insert alternative
	 */
	public boolean addAlternative(Alternative alt, String choiceID) throws Exception {
		int result = 0;
		FeedbackDAO feedbackDAO = new FeedbackDAO(logger);
		ArrayList<Feedback> feedbacks;

		try {
			/*PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName + " WHERE id = ?;");
			ps.setString(1, alt.getID());
			ResultSet resultSet = ps.executeQuery();

			// already present?
			while (resultSet.next()) {
				generateAlternative(resultSet);
				resultSet.close();
			}*/

			if (alt.getDescription().length() > 60) {
				logger.log("Alt desc too long");
				return false;
			}

			// add feedbacks
			feedbacks = alt.getFeedback();
			for (Feedback feedback : feedbacks) {
				// add rating
				if (feedback.getApproved() == 'A' || feedback.getApproved() == 'D') {
					feedbackDAO.addRating(feedback.getAltID(), feedback.getUserID(), feedback.getApproved(),
							"", Timestamp.from(Instant.now()));
				}
				// add message
				if (!feedback.getMessage().equals("") && !feedback.getMessage().equals(null)) {
					feedbackDAO.addMessage(feedback.getAltID(), feedback.getUserID(), feedback.getMessage(),
							feedback.getTimeStamp());
				}
			}

			// add alternative
			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + tblName +
					" (altID,numLikes,numDislikes,description,choiceID) values(?,?,?,?,?);");
			ps.setString(1, alt.getID());
			ps.setInt(2, alt.getTotalApprovals());
			ps.setInt(3, alt.getTotalDisapprovals());
			ps.setString(4, alt.getDescription());
			ps.setString(5, choiceID);
			try {
				result = ps.executeUpdate();
			} catch (Exception e) {
				logger.log("Duplicate alternative");
				return false;
			}

			logger.log("Finished inserting alternative");
			return result == 1;
		} catch (Exception e) {
			throw new Exception("Failed to insert alternative: " + e.getMessage());
		}
	}

	/**
	 * Returns all Alternatives in the Alternative table as a list of Alternative objects.
	 * @return the list of Alternative objects
	 * @throws Exception, couldn't get all the Alternative
	 */
	public List<Alternative> getAllAlternatives() throws Exception {
		List<Alternative> allAlts = new ArrayList<Alternative>();
		PreparedStatement ratings_ps;
		PreparedStatement messages_ps;
		ResultSet ratings_rs;
		ResultSet messages_rs;
		
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT * FROM " + tblName + ";";
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				ratings_ps = conn.prepareStatement("SELECT * FROM " + fbTbl + "" +
						" WHERE altID=?;");
				ratings_ps.setString(1, resultSet.getString("altID"));
				ratings_rs = ratings_ps.executeQuery();


				messages_ps = conn.prepareStatement("SELECT * FROM " + msgTbl + "" +
						" WHERE altID=?;");
				messages_ps.setString(1, resultSet.getString("altID"));
				messages_rs = messages_ps.executeQuery();

				Alternative a = generateAlternative(resultSet, ratings_rs, messages_rs);
				allAlts.add(a);
			}
			resultSet.close();
			statement.close();
			return allAlts;

		} catch (Exception e) {
			throw new Exception("Failed in getting all alternatives: " + e.getMessage());
		}
	}

	/**
	 * Generates an Alternative object that represents the given database row.
	 * Gets the ratings and also the messages associated with the alternative.
	 * @param alt_rs The cursor to the alternative row in the Alternative table
	 * @param ratings_rs The list of rating rows in Feedback for this alternative
	 * @return an Alternative object
	 * @throws Exception, failed to generate Alternative
	 */
	private Alternative generateAlternative(ResultSet alt_rs, ResultSet ratings_rs, ResultSet messages_rs)
			throws Exception {
		FeedbackDAO feedbackDAO = new FeedbackDAO(logger);
		UserDAO userDAO = new UserDAO(logger);

		logger.log("Generating alternative");

		String id;
		String description;
		int totalApprovals = 0;
		int totalDisapprovals = 0;
		String userID;

		ArrayList<Feedback> feedback = new ArrayList<>();
		ArrayList<String> totalDisapprovalUsers = new ArrayList<>();
		ArrayList<String> totalApprovalUsers = new ArrayList<>();

		id = alt_rs.getString("altID");
		description = alt_rs.getString("description");
		totalApprovals = alt_rs.getInt("numLikes");
		totalDisapprovals = alt_rs.getInt("numDislikes");

		if (ratings_rs.isBeforeFirst()) {
			while (ratings_rs.next()) {
				// this method adds both the rating and the message to the feedback object
				Feedback fb;
				fb = feedbackDAO.generateFeedbackFromFeedbackTable(ratings_rs);
				feedback.add(fb);
				if (ratings_rs.getString("approval").equals("A")) {
					userID = ratings_rs.getString("userID");
					totalApprovalUsers.add(userDAO.getUserFromID(userID).getName());
				} else if (ratings_rs.getString("approval").equals("D")) {
					userID = ratings_rs.getString("userID");
					totalDisapprovalUsers.add(userDAO.getUserFromID(userID).getName());
				}
			}
		}
		if (messages_rs.isBeforeFirst()) {
			while (messages_rs.next()) {
				// this method adds both the rating and the message to the feedback object
				Feedback newFb = feedbackDAO.generateFeedbackFromMessageTable(messages_rs);
				logger.log(newFb.getMessage());
				if (!feedback.contains(newFb)) {
					logger.log("Adding feedback: " + messages_rs.getString("message"));
					feedback.add(newFb);
				}
//					logger.log("Adding feedback: " + messages_rs.getString("message"));
//					feedback.add(feedbackDAO.generateFeedbackFromMessageTable(messages_rs));
			}
		}


		return new Alternative(id, description, totalApprovals, totalDisapprovals,
				feedback, totalApprovalUsers, totalDisapprovalUsers);
	}

}