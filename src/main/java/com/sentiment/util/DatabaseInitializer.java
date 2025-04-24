package com.sentiment.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for database schema creation and initialization.
 * This helps with testing the application by ensuring the database schema exists.
 */
public class DatabaseInitializer {
    
    /**
     * Creates the necessary database tables if they don't exist.
     * 
     * @return true if successful, false otherwise
     */
    public static boolean initializeDatabase() {
        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.createStatement();
            
            // Create feedbacks table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS feedbacks (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_name VARCHAR(100) NOT NULL, " +
                    "staff_name VARCHAR(100) NOT NULL, " +
                    "course_name VARCHAR(100) NOT NULL, " +
                    "feedback_text TEXT NOT NULL, " +
                    "original_rating DOUBLE NOT NULL, " +
                    "sentiment_label VARCHAR(20), " +
                    "sentiment_score DOUBLE, " +
                    "positive_points TEXT, " +
                    "negative_points TEXT, " +
                    "summary TEXT, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            
            stmt.executeUpdate(createTableSQL);
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            DatabaseUtil.closeConnection(conn);
        }
    }
}
