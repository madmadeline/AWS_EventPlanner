package caml.group.demo.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import caml.group.demo.model.User;

import javax.xml.transform.Result;

/**
 * For accessing the User table in RDS.
 * List of functions:
 *      loadOrInsertUser(String name, String pass, int choiceID) --> User
 *      deleteUser(User user) --> boolean
 *      addUser(User user, int choiceID) --> boolean
 *      getAllUsers() --> List<User>
 *      rowToUserObject(ResultSet resultSet, String password) --> User
 * @author Group Caml
 */
public class UserDAO { 
	LambdaLogger logger;
	java.sql.Connection conn;
//	Model model;
	final String usrTbl = "User";


    public UserDAO(LambdaLogger logger) {
    	this.logger = logger;
    	try  {
//            logger.log("Connecting in UserDAO.java");
    		conn = DatabaseUtil.connect();
//            logger.log("Connection Succeeded in UserDAO.java");
    	} catch (Exception e) {
    	    logger.log("Failed to connect to User table\n");
    		conn = null;
    	}
    }


    /**
     * Returns a User object representing the entry in the User table with the given
     * username and password. If the User isn't in the table, add a new User with the
     * given username and password, and return it.
     * @param name The given username
     * @param pass The given password
     * @return the User object
     * @throws Exception if the user could not be found or inserted in the table
     */
    public User loadOrInsertUser(String name, String pass, int choiceID) throws SQLException {
        User user = null; // User object representing the database entry
        PreparedStatement ps;
        ResultSet resultSet;
//        System.out.println("inside loadOrInsertUser");
        // check if user is already registered in the choice
        try {
            ps = conn.prepareStatement("SELECT * FROM " + usrTbl + " WHERE " + usrTbl
                    + ".username=? AND " + usrTbl + ".choiceID=?;");
            ps.setString(1,  name);
            ps.setString(2, "" + choiceID);
            resultSet = ps.executeQuery(); // cursor that points to database row

//            System.out.println("result set " + resultSet);
            // user isn't registered --> register user
            if (!resultSet.isBeforeFirst()) {
                user = new User(generateUserID(), name, pass);
                try {
                    addUser(user, choiceID);
                } catch (SQLException e) {
                    throw new SQLException("Couldn't add user" + e.getMessage());
                }
                resultSet.close();
                ps.close();
                return user;
            }

            // user is registered --> get the user
            while (resultSet.next()) {
                user = rowToUserObject(resultSet, pass); // should only loop 1x
                if (user != null) { logger.log("Retrieved user from the " + usrTbl + " table\n"); }
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
     * Returns whether or not the given user ID is already in the User table.
     * @param uID The given user ID
     * @return true if the table has the id, false otherwise
     */
    public boolean userIDExists(String uID) throws SQLException {
        PreparedStatement ps;
        ResultSet rs;

        try {
//            System.out.println("in userIDExists");
            ps = conn.prepareStatement("SELECT * FROM " + usrTbl + " WHERE " + usrTbl
                    + ".userID=?;");
            ps.setString(1, uID);
            rs = ps.executeQuery(); // cursor that points to database row
//            System.out.println("after userIDExists");
            return (!rs.isBeforeFirst()); // TODO make sure this works
        } catch (Exception e) {
            throw new SQLException("Failed to view User table: " + e.getMessage());
        }

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
                if(userIDExists(randString)) break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        System.out.println("generated a user ID");
        return randString;
    }


    /**
     * Deletes the specified user from the User table.
     * @param user The given User
     * @return true if the User was deleted, false otherwise
     * @throws Exception the User can't be found in the table
     */
    public boolean deleteUser(User user) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM " + usrTbl
                    + " WHERE name = ?;");
            ps.setString(1, user.getName());
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("Failed to delete user: " + e.getMessage());
        }
    }


    /**
     * Adds the given User object to the User database.
     * @param user The given User object
     * @return true if the User was added, false otherwise
     * @throws Exception, failed to insert user
     */
    public boolean addUser(User user, int choiceID) throws SQLException {
        PreparedStatement ps;

        try {
            // already present?
            ps = conn.prepareStatement("SELECT * FROM " + usrTbl + " WHERE " + usrTbl
                    + ".username=? AND " + usrTbl + ".choiceID=?;");
            ps.setString(1, user.getName());
            ps.setString(2, "" + choiceID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.isBeforeFirst()) {
               logger.log("User can't be added because they are already in the table\n");
               ps.close();
               return false;
            }

            // add to User table
            ps = conn.prepareStatement("INSERT INTO " + usrTbl +
                    " (userID,username,password,choiceID) values(?,?,?,?);");
            ps.setString(1, "" + user.getID());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.setString(4, "" + choiceID);
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
     * @param resultSet The cursor to the specified database row
     * @param password The specified password
     * @return a User object
     * @throws Exception, user doesn't exist in the database
     */
    private User rowToUserObject(ResultSet resultSet, String password) throws SQLException {
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
        // TODO account for case in which password isn't a string, it's NULL
//        logger.log("Row password: " + correctPassword + "\n");
//        logger.log("Specified password: " + password + "\n");

        if (correctPassword.equals(password)) { return new User (uID, username, password); }
        return null;
    }

}