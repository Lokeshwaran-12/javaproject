package com.sentiment.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for database operations.
 * This replaces the db.py file from the Python version.
 */
public class DatabaseUtil {
    // Database connection parameters
	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/sentiment_feedback";
	private static final String USER = "root";
	private static final String PASSWORD = "shahiil@2005";
// Should be configured properly in production

    /**
     * Gets a connection to the database.
     * 
     * @return A Connection object
     * @throws SQLException If a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);
            
            // Open a connection
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    /**
     * Closes the database connection safely.
     * 
     * @param connection The connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
