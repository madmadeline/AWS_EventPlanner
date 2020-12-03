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


    public Feedback loadOrInsertFeedback(String altID, boolean approved, String message, Timestamp timeStamp, String userID) throws SQLException {
        Feedback feedback = null; // User object representing the database entry
        PreparedStatement ps;
        ResultSet resultSet;

        // check if message is over character limit
        if (message.length() >= 500) {
            logger.log("The message is too long");
            return null;
        }
//


        // check if user has already voted in the table
        try {
            ps = conn.prepareStatement("SELECT * FROM " + feedbackTbl + " WHERE " + feedbackTbl
                    + ".userID=? AND " + feedbackTbl + ".approved=?;");
            ps.setString(1,  userID);
            ps.setBoolean(2,  approved);
            resultSet = ps.executeQuery(); // cursor that points to database row

            // user feedback isn't in the table --> insert user
            if (!resultSet.isBeforeFirst()) {
//                System.out.println("user's feedback isn't in the table");
                feedback = new Feedback(altID, approved, message, timeStamp, userID);
//                System.out.println("got new feedback");
                try {
                    addFeedback(altID, approved, message, timeStamp, userID);
                } catch (SQLException e) {
//                    System.out.println("Tried to add feedback");
                    throw new SQLException("Couldn't add feedback" + e.getMessage());
                }
                resultSet.close();
                ps.close();
                return feedback;
            }

            // user is in the table --> load the user
            while (resultSet.next()) {
//                System.out.println("user is in the table");
                feedback = rowToUserObject(resultSet, pass); // should only loop 1x
                if (user != null) { logger.log("Retrieved user from the " + feedbackTbl + " table\n"); }
                else { logger.log("Incorrect password\n"); }
            }

            resultSet.close();
            ps.close();
            return user;

        } catch (SQLException e) {
            throw new SQLException("Failed to get user\n" + e.getMessage());
        }
    }


    /**
     * Generates a User object that represents the given database row if the
     * password is correct. Else, return null.
     * @param resultSet The cursor to the specified database row
     * @param userID The specified password
     * @return a User object
     * @throws SQLException, user doesn't exist in the database
     */
    private User rowToUserObject(ResultSet resultSet, String userID) throws SQLException {
        String uID;
        String username;
        String correctPassword;

        try {
            username = resultSet.getString("username");
            uID = resultSet.getString("userID");
//            logger.log("Row username: " + username + "\n");
        } catch (SQLException e) {
            // result set is null, user doesn't exist
            throw new SQLException("User can't be found in the table: " + e.getMessage());
        }

        correctPassword = resultSet.getString("password");

        if (correctPassword.equals(userID)) { return new User (uID, username, userID); }
        return null;
    }


    /**
     * Gets a unique number ID that's not already in the User table.
     * @return the user ID
     */
    private String generateUserID() {
        String randString;

        while(true){
            Random rand = new Random();
            int randInt = rand.nextInt(9000) + 1000;
            randString = String.valueOf(randInt);
            try {
                if(!userIDExists(randString)) break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        System.out.println("generated a user ID");
        return randString;
    }


    // TESTED
    /**
     * Returns whether or not the given user ID is already in the User table.
     * @param uID The given user ID
     * @return true if the table has the id, false otherwise
     */
    public boolean userIDExists(String uID) throws SQLException {
        PreparedStatement ps;
        ResultSet rs;

        try {
//            System.out.println("in userIDExists " + uID);
            ps = conn.prepareStatement("SELECT * FROM " + feedbackTbl + " WHERE " + feedbackTbl
                    + ".userID=?;");
            ps.setString(1, uID);
            rs = ps.executeQuery(); // cursor that points to database row
            return (rs.isBeforeFirst());
        } catch (Exception e) {
            throw new SQLException("Failed to view User table: " + e.getMessage());
        }

    }


    // TESTED
    /**
     * Adds the given User object to the User database.
     * @param user The given User object (valid)
     * @param choiceID The given choice ID (valid)
     * @return true if the User was added, false otherwise
     * @throws SQLException, failed to insert user
     */
    public boolean addApprovedFeedback(String altID, boolean approved, String message, Timestamp timeStamp, String userID) throws SQLException {
        PreparedStatement ps;

        // check if a username was actually given
        if (user.getName().equals("") || user.getName() == null) {
            logger.log("No username was specified\n");
            return false;
        }

        // check if the choiceID is valid
        ChoiceDAO cdao = new ChoiceDAO(logger);
        if (cdao.getChoice(""+choiceID) == null) {
            return false;
        }


        try {
            // already present?
            ps = conn.prepareStatement("SELECT * FROM " + feedbackTbl + " WHERE " + feedbackTbl
                    + ".username=? AND " + feedbackTbl + ".choiceID=?;");
            ps.setString(1, user.getName());
            ps.setString(2, "" + choiceID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.isBeforeFirst()) {
               logger.log("User can't be added because they are already in the table\n");
               ps.close();
               return false;
            }

            // add to User table
            // ps = conn.prepareStatement("UPDATE " + feedbackTbl +  // change INSERT INTO to UPDATE to update/mutate/change data in the SQL table
            ps = conn.prepareStatement("INSERT INTO " + feedbackTbl +
                    " (userID,username,password,choiceID) values(?,?,?,?);");
            ps.setString(1, "" + user.getID());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.setString(4, "" + choiceID);
            ps.update(4, "" + choiceID);
            ps.execute();
            logger.log("Inserted the user into the User table\n");

            ps.close();
            return true;

        } catch (SQLException e) {
            throw new SQLException("Failed to insert user: " + e.getMessage());
        }
    }


    /**
     * Returns all Users in the User table as a list of User objects.
     * @return the list of User objects
     * @throws Exception, couldn't get all the Users
     */
    public List<Feedback> getAllFeedback() throws Exception {
        
        List<Feedback> allFeedback = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM " + feedbackTbl + ";";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                User u = rowToUserObject(resultSet, resultSet.getString("username"));
                allConstants.add(u);
            }
            resultSet.close();
            statement.close();
            return allConstants;

        } catch (Exception e) {
            throw new Exception("Failed in getting users: " + e.getMessage());
        }
    }

	
}
