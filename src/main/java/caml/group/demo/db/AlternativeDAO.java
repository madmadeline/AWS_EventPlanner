package caml.group.demo.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import caml.group.demo.model.Admin;
import caml.group.demo.model.Alternative;
import caml.group.demo.model.Model;
import caml.group.demo.model.User;



/**
 * Note that CAPITALIZATION matters regarding the table name. If you create with 
 * a capital "User" then it must be "User" in the SQL queries.
 *
 */
public class AlternativeDAO { 
	LambdaLogger logger;
	java.sql.Connection conn;
	Model model;
	final String tblName = "Alternative";   // Exact capitalization

	public AlternativeDAO(LambdaLogger logger) {
		this.logger = logger;
		try  {
			conn = DatabaseUtil.connect();
			logger.log("Connected in alt dao");
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

		try {
			Alternative alt = null; // User object representing the database entry
			//            boolean passwordCorrect = true;

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName + 
					" WHERE description=?;");
			ps.setString(1,  desc);
			ResultSet resultSet = ps.executeQuery(); // cursor that points to database row

			while (resultSet.next()) {
				alt = generateAlternative(resultSet); // should only loop 1x
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

	/**
	 * Gets an alternative from the database when given an id
	 * @param id, id of the requested alt
	 * @return the requested alt
	 * @throws Exception, how it fails
	 */
	public Alternative getAlternativeByID(String id) throws Exception {
		Alternative alt = null; // User object representing the database entry
		//            boolean passwordCorrect = true;

		PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName +
				" WHERE id=?;");
		ps.setString(1,  id);
		ResultSet resultSet = ps.executeQuery(); // cursor that points to database row
		logger.log("Generating alts");
		while (resultSet.next()) {
			alt = generateAlternative(resultSet); // should only loop 1x
		}
		resultSet.close();
		ps.close();

		if (logger != null) { logger.log("retrieved alternative " + id); }
		return alt;
	}

	
	/**
	 * Adds or removes an approval or disapproval to the row corresponding to 
	 * the given Alternative.
	 * @param alt, the given Alternative
	 * @param isApproval, the type of rating
	 * @param isAdd, the type of update (addition or removal)
	 * @return true if the update was successful, false otherwise
	 * @throws Exception
	 */
	public boolean updateAlternative(Alternative alt, boolean isApproval, boolean isAdd) throws Exception {
		String column = "numLikes";
		int newNum = alt.getTotalApprovals();
		
		if (!isApproval) {
			column = "numDislikes";
			newNum = alt.getTotalDisapprovals();
		}
		
		if (isAdd) { newNum++; }
		else { newNum--; }
		
		
		try {
			String query = "UPDATE " + tblName + " SET "+ column + "=? WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, newNum);
			ps.setString(2, alt.getID());
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);
		} catch (Exception e) {
			throw new Exception("Failed to update alternative: " + e.getMessage());
		}
	}

	
	/**
	 * Deletes the specified alternative from the Alternative table.
	 * @param alt, the given Alternative object
	 * @return true if the deletion was a success, false otherwise
	 * @throws Exception, failed to delete alternative
	 */
	public boolean deleteAlternative(Alternative alt) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM " + tblName + " WHERE id = ?;");
			ps.setString(1, alt.getID());
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);

		} catch (Exception e) {
			throw new Exception("Failed to delete alternative: " + e.getMessage());
		}
	}

	/**
	 * Adds the given Alternative object to the Alternative table.
	 * @param alt, the given Alternative object
	 * @return true if the addition was a success, false otherwise
	 * @throws Exception, failed to insert alternative
	 */
	public void addAlternative(Alternative alt) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName + " WHERE id = ?;");
			ps.setString(1, alt.getID());
			ResultSet resultSet = ps.executeQuery();

			// already present?
			while (resultSet.next()) {
				generateAlternative(resultSet);
				resultSet.close();
			}

			ps = conn.prepareStatement("INSERT INTO " + tblName + 
					" (id,numLikes,numDislikes,description) values(?,?,?,?);");
			ps.setString(1, alt.getID());
			ps.setInt(2, alt.getTotalApprovals());
			ps.setInt(3, alt.getTotalDisapprovals());
			ps.setString(4, alt.getDescription());
			ps.execute();

		} catch (Exception e) {
			throw new Exception("Failed to insert user: " + e.getMessage());
		}
	}

	/**
	 * Returns all Alternatives in the Alternative table as a list of Alternative objects.
	 * @return the list of Alternative objects
	 * @throws Exception, couldn't get all the Alternative
	 */
	public List<Alternative> getAllAlternatives() throws Exception {
		List<Alternative> allAlts = new ArrayList<Alternative>();
		
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT * FROM " + tblName + ";";
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				Alternative a = generateAlternative(resultSet);
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
	 * @param resultSet, the cursor to the specified database row
	 * @return an Alternative object
	 * @throws Exception, failed to get user
	 */
	private Alternative generateAlternative(ResultSet resultSet) throws Exception {
		String id  = resultSet.getString("id");
		String desc = resultSet.getString("description");

		return new Alternative (id, desc);
	}

}