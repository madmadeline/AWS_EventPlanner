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


    public Feedback loadOrInsertFeedback(String altID, char approved, String message, Timestamp timeStamp, String userID) throws SQLException {
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
                feedback = new Feedback(altID, userID, approved, message, timeStamp);
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


    private Feedback generateFeedback(ResultSet resultSet) throws SQLException {
        String altID;
        char approved;
        String message;
        Timestamp timestamp;
        String userID;

        try {
            altID = resultSet.getString("altID");
            approved = resultSet.getString("approved").toCharArray()[0];
            message = resultSet.getString("message");
            timestamp = resultSet.getTimestamp("timeStamp");
            userID = resultSet.getString("userID");
//            logger.log("Row username: " + username + "\n");
            return new Feedback(altID, userID, approved, message, timestamp);
        } catch (SQLException e) {
            // result set is null, user doesn't exist
            throw new SQLException("Feedback can't be found in the table: " + e.getMessage());
        }
    }


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



    public boolean addFeedback(String altID, String userID, char approved, String message, Timestamp timeStamp) throws SQLException {
        PreparedStatement ps;
        int result;
        try {
            // already present?
            logger.log("Checking if already present");
            ps = conn.prepareStatement("SELECT * FROM " + feedbackTbl + " WHERE " + feedbackTbl
                    + ".altID=? AND " + feedbackTbl + ".userID=?;");
            ps.setString(1, altID);
            ps.setString(2, userID);
            ResultSet resultSet = ps.executeQuery();

            // the feedback is already present in the table
            if (resultSet.isBeforeFirst()) {
                logger.log("Already present");
               // update the feedback row
                ps = conn.prepareStatement("UPDATE " + feedbackTbl + " SET " +
                        " message=?, timeStamp=?, approved=? WHERE altID=? AND userID=?;");
                ps.setString(1, message);
                ps.setTimestamp(2, timeStamp);
                ps.setString(3, "" + approved);
                ps.setString(4, altID);
                ps.setString(5, userID);

                result = ps.executeUpdate();
                ps.close();
                logger.log("Updated table");
                return result == 1;
            }

            // add to the Feedback table
            logger.log("Adding new stuff");
            ps = conn.prepareStatement("INSERT INTO " + feedbackTbl +
                    " (altID,userID,message,timeStamp,approved) values(?,?,?,?,?);");
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


    public List<Feedback> getAllFeedback(String altID) throws Exception {
        
        List<Feedback> allFeedback = new ArrayList<>();
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement("SELECT * FROM " + feedbackTbl + " WHERE altID=?;");
            ps.setString(1, altID);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Feedback feedback = new Feedback(altID, resultSet.getString("userID"),
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

	
}
