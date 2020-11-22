package caml.group.demo.db;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {

	// DB user names and passwords (as well as the db endpoint) should never be stored directly in code.
	//
	// https://docs.aws.amazon.com/lambda/latest/dg/env_variables.html
	//
	// The above link shows how to ensure Lambda function has access to environment as well as local
	public final static String jdbcTag = "jdbc:mysql://";
	public final static String rdsMySqlDatabasePort = "3306";
	public final static String multiQueries = "?allowMultiQueries=true";
	   
	// Make sure matches Schema created from MySQL WorkBench
	public final static String dbName = "project";            
	
	// pooled across all usages.
	static Connection conn;
 
	/**
	 * Singleton access to DB connection to share resources effectively across multiple accesses.
	 */
	protected static Connection connect() throws Exception {
		if (conn != null) { return conn; }
		
		// this is resistant to any SQL-injection attack.
		String schemaName = dbName;
		
		// These three environment variables must be set!
		String dbUsername = System.getenv("dbUsername");
		if (dbUsername == null) {
			System.err.println("Environment variable dbUsername is not set!");
		}
		String dbPassword = System.getenv("dbPassword");
		if (dbPassword == null) {
			System.err.println("Environment variable dbPassword is not set!");
		}
		String rdsMySqlDatabaseUrl = System.getenv("rdsMySqlDatabaseUrl");
		if (rdsMySqlDatabaseUrl == null) {
			System.err.println("Environment variable rdsMySqlDatabaseUrl is not set!");
		}
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e){
			System.err.println("Failed to find class com.mysql.cj.jdbc.Driver");
		}
		
		try {
			conn = DriverManager.getConnection(
					jdbcTag + rdsMySqlDatabaseUrl + ":" + rdsMySqlDatabasePort + "/" + schemaName + multiQueries,
					"admin",
					"Theo1020");
			return conn;
		} catch (Exception ex) {
			System.err.println("DB-ERRORraed:" + schemaName + "," + dbUsername + "," + dbPassword + "," + rdsMySqlDatabaseUrl);
			throw new Exception("Failed in database connection");
		}
	}
}
