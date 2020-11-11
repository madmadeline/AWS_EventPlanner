package caml.group.demo.model;
import java.sql.*;
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
    Method for inserting into User table
    userName: username of user
    passWord: password of user
    isAdmin: is the user an admin
     */
    public void InsertIntoUser(String userName, String passWord, boolean isAdmin) throws SQLException {
        stmt = conn.createStatement();
        String str = "Insert into User (username, password, isAdmin) values (" + userName + ", " + passWord + ", "
                + isAdmin + ")";
        stmt.executeUpdate(str);
        stmt.close();
    }

    /*
    Method for inserting into Review table
     */
    public void InsertIntoReview(String reviewer, String alternativeID) throws SQLException {
        stmt = conn.createStatement();
        String str = "Insert into Review (reviewer, altID) values (" + reviewer + ", " + alternativeID + ")";
        stmt.executeUpdate(str);
        stmt.close();
    }

    public void InsertIntoReport(String id, String reporter, Date completedDate, boolean isComplete) throws SQLException {
        stmt = conn.createStatement();
        String str = "Insert into Report (id, reporter, dateOfCompletion, isComplete) values ("
                + id + ", " + reporter + ", " + completedDate + ", " + isComplete + ")";
        stmt.executeUpdate(str);
        stmt.close();
    }
}
