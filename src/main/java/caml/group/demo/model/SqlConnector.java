package caml.group.demo.model;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/*
Class for is just a place for me to dump my java to sql code until
we can come up with a better spot for it. This class most likely
will not be used in the final deliverable, so don't try to keep it
 */
public class SqlConnector {
    private static Connection conn;
    Statement stmt = null;

    /*
    Method for creating a connection to the database. Call at the start of the program
     */
    public static void Connection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://database-3733.cgkor7xozyye.us-east-1.rds.amazonaws.com:3306/";
        String userName = "admin";
        String password = "Theo1020";
        String dbName = "project";
        conn = DriverManager.getConnection(url + dbName, userName, password);

    }

    /*
    Closes the connection to the database. Call at the end of the program
     */
    public void CloseConnection() throws SQLException {
        conn.close();
    }

    /*
    Method for printing out the entire user table
     */
    public List<List<User>> FindAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        List<User> admins = new ArrayList<>();
        List<List<User>> returnStmt = new ArrayList<>();
        stmt = conn.createStatement();
        String str = "Select * From User";
        ResultSet rs = stmt.executeQuery(str);

        while(rs.next()){
            if(rs.getBoolean("isAdmin")){
                // Create Admin
                User admin = new User(rs.getString("username"), rs.getString("password"));
                admins.add(admin);
            }
            else{
                // Create user
                User user = new User(rs.getString("username"), rs.getString("password"));
                users.add(user);
            }
        }
        returnStmt.add(users);
        returnStmt.add(admins);
        rs.close();
        stmt.close();
        return returnStmt;
    }

    /**
    Method for printing out any table with any config
    @param tables, the list of tables to be printed out
     @param columns, the list of columns to be printed out
     */
    public List<List<String>> PrintAnyTable(List<String> tables, List<String> columns) throws SQLException {
        stmt = conn.createStatement();
        StringBuilder str = new StringBuilder("Select ");
        List<List<String>> resultStrings = new ArrayList<>();
        str.append(columns.get(0));
        for(int i = 1; i < columns.size(); i++){
            str.append(", ").append(columns.get(i));
        }
        str.append(" From ").append(tables.get(0));
        for(int i = 1; i < tables.size(); i++){
            str.append(" join ").append(tables.get(i));
        }
        ResultSet rs = stmt.executeQuery(String.valueOf(str));
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnNumber = rsmd.getColumnCount();
        while(rs.next()){
            List<String> outputStr = new ArrayList<>();
            outputStr.add(rs.getString(1));
            for(int i = 2; i <= columnNumber; i++){
                outputStr.add(rs.getString(i));
            }
            resultStrings.add(outputStr);
        }
        rs.close();
        stmt.close();
        return resultStrings;
    }

    /**
    Method for inserting into User table
    @param userName: username of user
    @param passWord: password of user
    @param isAdmin: is the user an admin
     */
    public void InsertIntoUser(String userName, String passWord, boolean isAdmin) throws SQLException {
        stmt = conn.createStatement();
        String str = "Insert into User (username, password, isAdmin) values (" + userName + ", " + passWord + ", "
                + isAdmin + ")";
        stmt.executeUpdate(str);
        stmt.close();
    }

    /**
     * Method for inserting into review table
     * @param reviewer
     * @param alternativeID
     * @throws SQLException
     */
    public void InsertIntoReview(String reviewer, String alternativeID) throws SQLException {
        stmt = conn.createStatement();
        String str = "Insert into Review (reviewer, altID) values (" + reviewer + ", " + alternativeID + ")";
        stmt.executeUpdate(str);
        stmt.close();
    }

    /**
     * Method for inserting into report table
     * @param id
     * @param reporter
     * @param completedDate
     * @param isComplete
     * @throws SQLException
     */
    public void InsertIntoReport(String id, String reporter, Date completedDate, boolean isComplete) throws SQLException {
        stmt = conn.createStatement();
        String str = "Insert into Report (id, reporter, dateOfCompletion, isComplete) values ("
                + id + ", " + reporter + ", " + completedDate + ", " + isComplete + ")";
        stmt.executeUpdate(str);
        stmt.close();
    }

    /**
     * Method for inserting into feedback table
     * @param altID
     * @param userID
     * @param message
     * @param timeStamp
     * @throws SQLException
     */
    public void InsertIntoFeedback(String altID, String userID, String message, Date timeStamp) throws SQLException {
        stmt = conn.createStatement();
        String str = "Insert into Feedback (altID, userID, message, timeStamp) values (" + altID + ", " + userID
                + ", " + message + ", " + timeStamp +")";
        stmt.executeUpdate(str);
        stmt.close();
    }

    /**
     * Method for inserting into choicealtmatch table
     * @param choiceID
     * @param altID
     * @throws SQLException
     */
    public void InsertIntoChoiceAltMatch(String choiceID, String altID) throws SQLException{
        stmt = conn.createStatement();
        String str = "Insert into ChoiceAltMatch (choiceID, altID) values (" + choiceID + ", " + altID + ")";
        stmt.executeUpdate(str);
        stmt.close();
    }

    /**
     * Method for inserting into choice table
     * @param id
     * @param description
     * @param DoC
     * @param winningAlt
     * @throws SQLException
     */
    public void InsertIntoChoice(String id, String description, Date DoC, String winningAlt) throws SQLException {
        stmt = conn.createStatement();
        String str = "Insert into Choice (id, description, dateOfCreation, winningAlt) values ("
                + id + ", " + description + ", " + DoC + ", " + winningAlt + ")";
        stmt.executeUpdate(str);
        stmt.close();
    }

    /**
     * Method for inserting into alternative table
     * @param id
     * @param numLikes
     * @param numDislikes
     * @param description
     * @throws SQLException
     */
    public void InsertIntoAlternative(String id, int numLikes, int numDislikes, String description) throws SQLException {
        stmt = conn.createStatement();
        String str = "Insert into Alternative (id, numLikes, numDislikes, description) values ("
                + id + ", " + numLikes + ", " + numDislikes + ", " + description + ")";
        stmt.executeUpdate(str);
        stmt.close();
    }
}
