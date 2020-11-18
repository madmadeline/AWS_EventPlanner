package caml.group.demo.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import caml.group.demo.model.Admin;
import caml.group.demo.model.Model;
import caml.group.demo.model.User;



/**
 * Note that CAPITALIZATION matters regarding the table name. If you create with 
 * a capital "User" then it must be "User" in the SQL queries.
 *
 */
public class UserDAO { 

	java.sql.Connection conn;
	Model model;
	final String tblName = "User";   // Exact capitalization

    public UserDAO(Model model) {
    	this.model = model;
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }

    // TODO have different error messages for user doesn't exist and incorrect password
    /**
     * Returns a User object representing the entry in the User table with the given
     * username and password.
     * @param name, the given username
     * @param pass, the given password
     * @return the User object
     * @throws Exception
     */
    public User getUser(String name, String pass) throws Exception {
        
        try {
            User user = null; // User object representing the database entry
//            boolean passwordCorrect = true;
            
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName + 
            		" WHERE username=?; AND password=?;");
            ps.setString(1,  name);
            ps.setString(2, pass);
            ResultSet resultSet = ps.executeQuery(); // cursor that points to database row
            
            while (resultSet.next()) {
                user = generateUser(resultSet); // should only loop 1x
            }
            resultSet.close();
            ps.close();
            
            return user;

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in getting user: " + e.getMessage());
        }
    }
    
//    public boolean updateUser(User user) throws Exception {
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
            PreparedStatement ps = conn.prepareStatement("DELETE FROM " + tblName + " WHERE name = ?;");
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
     * @return true if the addition was a success, false otherwise
     * @throws Exception, failed to insert user
     */
    public boolean addUser(User user) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName + " WHERE name = ?;");
            ps.setString(1, user.getID());
            ResultSet resultSet = ps.executeQuery();
            
            // already present?
            while (resultSet.next()) {
                generateUser(resultSet);
                resultSet.close();
                return false;
            }

            ps = conn.prepareStatement("INSERT INTO " + tblName + " (username,password,isAdmin) values(?,?,?);");
            ps.setString(1, user.getID());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getClass() == Admin.class);
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
            String query = "SELECT * FROM " + tblName + ";";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                User u = generateUser(resultSet);
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
     * Generates a User object that represents the given database row.
     * @param resultSet, the cursor to the specified database row
     * @return a User object
     * @throws Exception, failed to get user
     */
    private User generateUser(ResultSet resultSet) throws Exception {
        String name  = resultSet.getString("username");
        String password = resultSet.getString("password");
        boolean isAdmin = resultSet.getBoolean("isAdmin");
        
        if (isAdmin) { return new Admin(name, password, model); }
        return new User (name, password);
    }

}