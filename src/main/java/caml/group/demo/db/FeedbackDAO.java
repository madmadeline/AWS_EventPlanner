package caml.group.demo.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import caml.group.demo.model.User;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import caml.group.demo.model.Feedback;

public class FeedbackDAO {

	LambdaLogger logger;
	java.sql.Connection conn;
//	Model model;
	final String feedbackTbl = "Feedback";


    public FeedbackDAO(LambdaLogger logger) {
    	this.logger = logger;
    	try  {
//            logger.log("Connecting in UserDAO.java");
    		conn = DatabaseUtil.connect();
//            logger.log("Connection Succeeded in UserDAO.java");
    	} catch (Exception e) {
    	    logger.log("Failed to connect to Feedback table\n");
    		conn = null;
    	}
    }


    /**
     * Returns a User object representing the entry in the User table with the given
     * username and password. If the User isn't in the table, add a new User with the
     * given username and password, and return it.
     * @return the User object
     * @throws SQLException if the user could not be found or inserted in the table
     */
    public Feedback loadOrInsertFeedback(String altID, char approved, String message, Timestamp timeStamp, String userID, String username) throws Exception {
        Feedback feedback = null; // User object representing the database entry
        PreparedStatement ps;
        ResultSet resultSet;

        // check if message is over character limit
        if (message.length() >= 500) {
            logger.log("The message is too long");
            return null;
        }
//


        // check if user has already submitted some feedback
        try {
            ps = conn.prepareStatement("SELECT * FROM " + feedbackTbl + " WHERE " + feedbackTbl
                    + ".userID=?;");
            ps.setString(1,  userID);
            resultSet = ps.executeQuery(); // cursor that points to database row

            // user feedback isn't in the table --> insert new feedback
            if (!resultSet.isBeforeFirst()) {
//                System.out.println("user's feedback isn't in the table");
                feedback = new Feedback(altID, userID, username, approved, message, timeStamp);
//                System.out.println("got new feedback");
                try {
                    addFeedback(altID, userID, approved, message, timeStamp);
                } catch (SQLException e) {
//                    System.out.println("Tried to add feedback");
                    throw new SQLException("Couldn't add feedback" + e.getMessage());
                }
                resultSet.close();
                ps.close();
                return feedback;
            }

            // user feedback is in the table --> load the feedback
            while (resultSet.next()) {
//                System.out.println("user is in the table");
                feedback = generateFeedback(resultSet); // should only loop 1x
                if (feedback != null) { logger.log("Retrieved feedback from the " + feedbackTbl + " table\n"); }
            }

            resultSet.close();
            ps.close();
            return feedback;

        } catch (SQLException e) {
            throw new SQLException("Failed to get user\n" + e.getMessage());
        }
    }


    /**
     * Returns a Feedback object representing the given row in the Feedback table.
     * @param resultSet the row in the table
     * @return the Feedback object
     * @throws SQLException database error idk
     */
    private Feedback generateFeedback(ResultSet resultSet) throws Exception {
        String altID;
        char approved;
        String message;
        Timestamp timestamp;
        String userID;
        UserDAO userDAO;

        try {
            userDAO = new UserDAO(logger);
            altID = resultSet.getString("altID");
            approved = resultSet.getString("approved").toCharArray()[0];
            message = resultSet.getString("message");
            timestamp = resultSet.getTimestamp("timeStamp");
            userID = resultSet.getString("userID");
//            logger.log("Row username: " + username + "\n");
            return new Feedback(altID, userID, userDAO.getUsernameFromID(userID),
                    approved, message, timestamp);
        } catch (SQLException e) {
            // result set is null, user doesn't exist
            throw new SQLException("Feedback can't be found in the table: " + e.getMessage());
        }
    }


    /**
     * Deletes the specified user from the User table.
     * @return true if the User was deleted, false otherwise
     * @throws Exception the User can't be found in the table
     */
    public boolean deleteFeedback(Feedback fb) throws Exception {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM " + feedbackTbl
                + " WHERE altID=? AND userID=?;");
        ps.setString(1, fb.getAltID());
        ps.setString(2, fb.getUserID());

        try {
            int numDeleted = ps.executeUpdate();
            if (numDeleted != 1) {
                logger.log("Feedback can't be deleted because it isn't in the table\n");
                ps.close();
                return false;
            }

            logger.log("Successfully deleted the feedback from the table");
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to delete feedback: " + e.getMessage());
        }
    }



    /**
     * Adds the given User object to the User database.
     * @return true if the User was added, false otherwise
     * @throws Exception, failed to insert user
     */
    public boolean addFeedback(String altID, String userID, char approved, String message, Timestamp timeStamp) throws Exception {
        PreparedStatement ps;
        int result;

        try {
            // already present?
            // TODO use the feedbackExists() instead
            ps = conn.prepareStatement("SELECT * FROM " + feedbackTbl + " WHERE " + feedbackTbl
                    + ".altID=? AND " + feedbackTbl + ".userID=?;");
            ps.setString(1, altID);
            ps.setString(2, userID);
            ResultSet resultSet = ps.executeQuery();

            // the feedback is already present in the table
            if (resultSet.isBeforeFirst()) {
                logger.log("Updating feedback row");
               // update the feedback row
                ps = conn.prepareStatement("UPDATE " + feedbackTbl + " SET " +
                        " message=?, timeStamp=?, approved=? WHERE altID=? AND userID=? AND" +
                        "approved!=?;");
                ps.setString(1, message);
                ps.setTimestamp(2, timeStamp);
                ps.setString(3, "" + approved);
                ps.setString(4, altID);
                ps.setString(5, userID);
                ps.setString(6, "" + approved);
                try {
                    result = ps.executeUpdate();
                } catch (Exception e) {
                    throw new Exception("User is trying to approve/disapprove the same " +
                            "alternative twice " + e.getMessage());
                }

                ps.close();

                return result == 1;
            }

            // add to the Feedback table
            logger.log("Adding to the Feedback table");
            ps = conn.prepareStatement("INSERT INTO " + feedbackTbl +
                    " (altID,userID,message,timeStamp,approved) values(?,?,?,?,?);"); // ps is closed ignore error
            ps.setString(1, altID);
            ps.setString(2, userID);
            ps.setString(3, message);
            ps.setTimestamp(4, timeStamp);
            ps.setString(5, "" + approved);
            result = ps.executeUpdate();
            logger.log("Added the feedback in the Feedback table\n");

            ps.close();
            return result == 1;

        } catch (SQLException e) {
            throw new SQLException("Failed to insert user: " + e.getMessage());
        }
    }


    /**
     * Returns all feedback in the Feedback table with the given alternative ID.
     * @return the list of Feedback objects
     * @throws Exception, couldn't get all the Feedback
     */
    public List<Feedback> getAllFeedback(String altID) throws Exception {
        UserDAO userDAO = new UserDAO(logger);
        List<Feedback> allFeedback = new ArrayList<>();
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement("SELECT * FROM " + feedbackTbl + " WHERE altID=?;");
            ps.setString(1, altID);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String userID = resultSet.getString("userID");
                Feedback feedback = new Feedback(altID, userID,
                        userDAO.getUsernameFromID(userID),
                        resultSet.getString("approved").toCharArray()[0],
                        resultSet.getString("message"),
                        resultSet.getTimestamp("timeStamp"));
                allFeedback.add(feedback);
            }
            resultSet.close();
            return allFeedback;

        } catch (Exception e) {
            throw new Exception("Failed in getting all feedback: " + e.getMessage());
        }
    }


    /**
     * Returns whether or not the feedback already exists.
     * @param altID
     * @param userID
     * @return
     */
    public boolean feedbackExists(String altID, String userID) throws Exception {
        PreparedStatement ps;

        ps = conn.prepareStatement("SELECT * FROM " + feedbackTbl + " WHERE " + feedbackTbl
                + ".altID=? AND " + feedbackTbl + ".userID=?;");
        ps.setString(1, altID);
        ps.setString(2, userID);
        try {
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            } else { return false; }
        } catch (Exception e) {
            throw new Exception("database error in feedback exists??? idk" + e.getMessage());
        }
    }


    /**
     * Checks to see if the feedback with the given alt ID and user ID already
     * has an approval in it.
     * @param altID The given alt ID
     * @param userID The given user ID
     * @return The approval
     *      'A' if approval,
     *      'D' if disapproval,
     *      '0' if null or feedback doesn't exist)
     * @throws Exception
     */
    public char getApproval(String altID, String userID) throws Exception {
        logger.log("Getting approval");
        PreparedStatement ps;

        ps = conn.prepareStatement("SELECT * FROM " + feedbackTbl + " WHERE " +
                "altID=? AND userID=? AND approved IS NOT NULL;");
        ps.setString(1, altID);
        ps.setString(2, userID);

        if (feedbackExists(altID, userID)) {
            try {
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    logger.log("Feedback does have approval");
                    return resultSet.getString("approved").toCharArray()[0];
                } else {
                    logger.log("Feedback doesn't have approval");
                    return '0';
                }
            } catch (Exception e) {
                throw new Exception("Database error " + e.getMessage());
            }
        }
        return '0';
    }
}
