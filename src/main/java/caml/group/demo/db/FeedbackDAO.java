package caml.group.demo.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import caml.group.demo.model.Feedback;

public class FeedbackDAO {

	LambdaLogger logger;
	java.sql.Connection conn;
//	Model model;
	final String feedbackTbl = "Feedback";
	final String messageTbl = "Message";


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
     * Returns whether or not the feedback already exists.
     * @param altID
     * @param userID
     * @return
     */
    public boolean ratingExists(String altID, String userID) throws Exception {
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
     * Returns whether or not the feedback message already exists.
     * @param altID
     * @param userID
     * @return
     */
    public boolean messageExists(String altID, String userID) throws Exception {
        PreparedStatement ps;

        ps = conn.prepareStatement("SELECT * FROM " + messageTbl + " WHERE altID=? " +
                "AND userID=?;");
        ps.setString(1, altID);
        ps.setString(2, userID);
        try {
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            } else { return false; }
        } catch (Exception e) {
            throw new Exception("database error in message exists??? idk" + e.getMessage());
        }
    }



    /**
     * Returns a Feedback object representing the given row in the Feedback table.
     * @param fb_resultSet the row in the table
     * @return the Feedback object
     * @throws Exception database error idk
     */
    public Feedback generateFeedbackFromFeedbackTable(ResultSet fb_resultSet) throws Exception {
        String altID;
        String userID;
        String username;
        char approved;
        String message;
        Timestamp timestamp;

        UserDAO userDAO;
        PreparedStatement getMessage_ps;

        logger.log("getting feedback from feedback table");

        try {
            // get all the ratings
            userDAO = new UserDAO(logger);
            altID = fb_resultSet.getString("altID");
            userID = fb_resultSet.getString("userID");
            username = userDAO.getUserFromID(userID).getName();
            approved = fb_resultSet.getString("approved").toCharArray()[0];

            // try to get all the messages
            try {
                getMessage_ps = conn.prepareStatement("SELECT * FROM " + messageTbl + " WHERE altID=? " +
                        "AND userID=?;");
                getMessage_ps.setString(1, altID);
                getMessage_ps.setString(2, userID);
                ResultSet msg_rs = getMessage_ps.executeQuery();
                if (msg_rs.next()) {
                    message = msg_rs.getString("message");
                    timestamp = msg_rs.getTimestamp("timeStamp");
                } else {
                    message = "";
                    timestamp = null;
                }
            } catch (Exception e){
                throw new Exception("Can't connect to the Message table " + e.getMessage());
            }

            return new Feedback(altID, userID, username, approved, message, timestamp);
        } catch (Exception e) {
            throw new Exception("Feedback can't be found in the table: " + e.getMessage());
        }
    }


    /**
     * Returns a Feedback object representing the given row in the Message table.
     * @param msg_resultSet the row in the table
     * @return the Feedback object
     * @throws Exception database error idk
     */
    public Feedback generateFeedbackFromMessageTable(ResultSet msg_resultSet) throws Exception {
        String altID;
        String userID;
        String username;
        char approved;
        String message;
        Timestamp timestamp;

        UserDAO userDAO;
        PreparedStatement getFeedback_ps;

        logger.log("getting feedback from message table");

        try {
            // get the message
            userDAO = new UserDAO(logger);
            altID = msg_resultSet.getString("altID");
            userID = msg_resultSet.getString("userID");
            logger.log("user id is " + userID);
            logger.log("username is " + userDAO.getUserFromID(userID).getName());
            username = userDAO.getUserFromID(userID).getName();
            message = msg_resultSet.getString("message");
            timestamp = msg_resultSet.getTimestamp("timeStamp");

            // try to get the rating
            try {
                getFeedback_ps = conn.prepareStatement("SELECT * FROM " + feedbackTbl + " WHERE altID=? " +
                        "AND userID=?;");
                getFeedback_ps.setString(1, altID);
                getFeedback_ps.setString(2, userID);
                ResultSet fb_resultSet = getFeedback_ps.executeQuery();
                if (fb_resultSet.next()) {
                    approved = msg_resultSet.getString("approved").toCharArray()[0];
                } else {
                    approved = 0;
                }
            } catch (Exception e){
                throw new Exception("Can't connect to the Feedback table " + e.getMessage());
            }

            return new Feedback(altID, userID, username, approved, message, timestamp);
        } catch (Exception e) {
            throw new Exception("Feedback can't be found in the table: " + e.getMessage());
        }

    }


    /**
     * Deletes the given feedback from the database (including both
     * rating and message).
     * @return True if the feedback was deleted, false otherwise
     * @throws Exception The feedback can't be found in the tables
     */
    public boolean deleteFeedback(Feedback fb) throws Exception {
        String altID = fb.getAltID();
        String userID = fb.getUserID();
        int ratingResult = 0;
        int messageResult = 0;

        logger.log("Deleting feedback " + fb.getMessage() + " for alt " + fb.getAltID());

//        ps = conn.prepareStatement("DELETE FROM " + feedbackTbl
//                + " WHERE altID=? AND userID=?;");
//        ps.setString(1, altID);
//        ps.setString(2, userID);

        // delete rating if it exists
        if (ratingExists(altID, userID)) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM " + feedbackTbl
                    + " WHERE altID=? AND userID=?;");
            ps.setString(1, altID);
            ps.setString(2, userID);

            try {
                ratingResult = ps.executeUpdate();
                logger.log("Successfully deleted the rating from the table");
                ps.close();
            } catch (Exception e) {
                throw new Exception("Failed to delete feedback rating: " + e.getMessage());
            }
        }

        // delete message if it exists
        if (messageExists(altID, userID)) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM " + messageTbl
                    + " WHERE altID=? AND userID=?;");
            ps.setString(1, altID);
            ps.setString(2, userID);

            try {
                messageResult = ps.executeUpdate();
                logger.log("Successfully deleted the message from the table");
                ps.close();
            } catch (Exception e) {
                throw new Exception("Failed to delete feedback message: " + e.getMessage());
            }
        }

        if (ratingResult == 1 || messageResult == 1) {
            logger.log("Successfully deleted the feedback from the table");
            return true;
        }

        logger.log("Feedback can't be deleted because it isn't in the table\n");
        return false;
    }



    /**
     * Adds the given approval to the database for the given alternative ID
     * and user ID.
     * @return true if the User was added, false otherwise
     * @throws Exception, failed to insert user
     */
    public boolean addRating(String altID, String userID, char approved, String message, Timestamp timeStamp)
            throws Exception {
        PreparedStatement ps;
        int result;

        try {
            // approval is already present
            if (ratingExists(altID, userID)) {
                logger.log("Updating feedback row");
                // update the feedback row
                ps = conn.prepareStatement("UPDATE " + feedbackTbl + " SET " +
                        " message=?, timeStamp=?, approved=? WHERE altID=? AND userID=? AND" +
                        " approved IS NOT NULL;");
                ps.setString(1, message);
                ps.setTimestamp(2, timeStamp);
                ps.setString(3, "" + approved);
                ps.setString(4, altID);
                ps.setString(5, userID);
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

            return result == 1;

        } catch (SQLException e) {
            throw new SQLException("Failed to insert feedback: " + e.getMessage());
        }
    }


    /**
     * Adds the given feedback message to the database for the given alternative
     * ID and user ID.
     * @return True if the message was added, false otherwise
     * @throws Exception Failed to insert or update message
     */
    public boolean addMessage(String altID, String userID, String message, Timestamp timeStamp) throws Exception {
        PreparedStatement ps;
        int result;

        logger.log("Adding message");

        try {
            // feedback message already exists --> update Message table
            if (ratingExists(altID, userID)) {
                logger.log("Updating Message row");
                ps = conn.prepareStatement("UPDATE " + messageTbl + " SET " +
                        " message=?, timeStamp=? WHERE altID=? AND userID=?;");
                ps.setString(1, message);
                ps.setTimestamp(2, timeStamp);
                ps.setString(3, altID);
                ps.setString(4, userID);
                result = ps.executeUpdate();

                ps.close();
                return result == 1;
            }

            // feedback message doesn't exist --> add to the Message table
            logger.log("Adding to the Message table");
            ps = conn.prepareStatement("INSERT INTO " + messageTbl +
                    " (altID,userID,message,timeStamp) values(?,?,?,?);");
            ps.setString(1, altID);
            ps.setString(2, userID);
            ps.setString(3, message);
            ps.setTimestamp(4, timeStamp);
            result = ps.executeUpdate();
            logger.log("Added the message in the Message table\n");

            return result == 1;

        } catch (SQLException e) {
            throw new SQLException("Failed to insert message: " + e.getMessage());
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
    public char getRating(String altID, String userID) throws Exception {
        logger.log("Getting approval");
        PreparedStatement ps;

        ps = conn.prepareStatement("SELECT * FROM " + feedbackTbl + " WHERE " +
                "altID=? AND userID=? AND approved IS NOT NULL;");
        ps.setString(1, altID);
        ps.setString(2, userID);

        if (ratingExists(altID, userID)) {
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
