package caml.group.demo.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import caml.group.demo.model.Admin;
//import caml.group.demo.model.Model;
import caml.group.demo.model.User;



/**
 * Note that CAPITALIZATION matters regarding the table name. If you create with 
 * a capital "User" then it must be "User" in the SQL queries.
 *
 */
public class UserDAO { 
	LambdaLogger logger;
	java.sql.Connection conn;
//	Model model;
	final String usrTbl = "User";
    final String choiceUsrTbl = "ChoiceUserMatch";

    public UserDAO(LambdaLogger logger) {
    	this.logger = logger;
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }

    // TODO have different error messages for user doesn't exist and incorrect password
    /**
     * Returns a User object representing the entry in the User table with the given
     * username and password. If the User isn't in the table, add a new User with the
     * given username and password, and return it.
     * @param name, the given username
     * @param pass, the given password
     * @return the User object
     * @throws Exception if the user could not be found or inserted in the table
     */
    public User getUser(String name, String pass, int choiceID) throws Exception {
        User user = null; // User object representing the database entry
        PreparedStatement ps;
        ResultSet resultSet;

        // check if user is already registered in the choice
        try {
            ps = conn.prepareStatement("SELECT * FROM " + usrTbl + " INNER JOIN " + choiceUsrTbl
                    + "on " + usrTbl + ".username=" + choiceUsrTbl + ".username WHERE username=?");
            ps.setString(1,  name);
            resultSet = ps.executeQuery(); // cursor that points to database row

            // user isn't registered --> register user
            if (!resultSet.isBeforeFirst()) {
                user = new User(name, pass);
                addUser(user, choiceID);
                logger.log("Registered new user\n");

                resultSet.close();
                ps.close();
                return user;
            }

            // user is registered --> get the user
            while (resultSet.next()) {
                user = rowToUserObject(resultSet, pass); // should only loop 1x
                logger.log("Retrieved user from the " + usrTbl + " table\n");
            }

            resultSet.close();
            ps.close();
            return user;

        } catch (Exception e) {
            throw new Exception("Failed to get user\n" + e.getMessage());
        }
    }
    
//    public boolean updateUser(User user) throws` Exception {
//        try {
//        	String query = "UPDATE " + tblName + " SET password=? WHERE username=?;";
//        	PreparedStatement ps = conn.prepareStatement(query);
//            ps.setString(1, user.getPassword());
//            ps.setString(2, user.getID());
//            int numAffected = ps.executeUpdate();
//            ps.close();
//            
//            return (numAffected == 1);
//        } catch (Exception e) {
//            throw new Exception("Failed to update report: " + e.getMessage());
//        }
//    }
    
    public boolean deleteUser(User user) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM " + usrTbl
                    + " WHERE name = ?;");
            ps.setString(1, user.getID());
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("Failed to delete user: " + e.getMessage());
        }
    }

    /**
     * Adds the given User object to the User database.
     * @param user, the given User object
     * @return true if the User was added, false otherwise
     * @throws Exception, failed to insert user
     */
    public boolean addUser(User user, int choiceID) throws Exception {
        try {
            // already present?
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + usrTbl
                    + " WHERE name = ?");
            ps.setString(1, user.getID());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.isBeforeFirst()) {
               logger.log("User can't be added because they are already in the table\n");
               return false;
            }

            // add to User table
            ps = conn.prepareStatement("INSERT INTO " + usrTbl +
                    " (username,password,isAdmin) values(?,?,?);");
            ps.setString(1, user.getID());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getClass() == Admin.class);
            ps.execute();

            // add to ChoiceUserMatch table
            ps = conn.prepareStatement("INSERT INTO " + choiceUsrTbl +
                    " (username,choiceID) values(?,?);");
            ps.setString(1, user.getID());
            ps.setInt(2, choiceID);
            ps.execute();

            return true;

        } catch (Exception e) {
            throw new Exception("Failed to insert user: " + e.getMessage());
        }
    }

    /**
     * Returns all Users in the User table as a list of User objects.
     * @return the list of User objects
     * @throws Exception, couldn't get all the Users
     */
    public List<User> getAllUsers() throws Exception {
        
        List<User> allConstants = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM " + usrTbl + ";";
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
    
    /**
     * Generates a User object that represents the given database row if the
     * password is correct. Else, return null.
     * @param resultSet, the cursor to the specified database row
     * @param password, the specified password
     * @return a User object
     * @throws Exception, user doesn't exist in the database
     */
    private User rowToUserObject(ResultSet resultSet, String password) throws Exception {
        String username;
        String correctPassword;

        try {
            username = resultSet.getString("username");
            logger.log("row username: " + username + "\n");
        } catch (Exception e) {
            // result set is null, user doesn't exist
            throw new Exception("User does not exist (rowToUserObject): " + e.getMessage());
        }

        correctPassword = resultSet.getString("password");
        logger.log("row password: " + correctPassword + "\n");
        if (correctPassword.equals(password)) { return new User (username, password); }
        return null;
    }

}