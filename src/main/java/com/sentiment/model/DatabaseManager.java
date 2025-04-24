package com.sentiment.model;

import com.sentiment.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all database operations for the sentiment analysis application.
 * This replaces the database operations in the Python version.
 */
public class DatabaseManager {
    
    /**
     * Saves a new feedback entry to the database.
     * 
     * @param feedback The feedback object to save
     * @return true if successful, false otherwise
     */
    public boolean saveFeedback(Feedback feedback) {
        String sql = "INSERT INTO feedbacks (student_name, staff_name, course_name, feedback_text, original_rating) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, feedback.getStudentName());
            pstmt.setString(2, feedback.getStaffName());
            pstmt.setString(3, feedback.getCourseName());
            pstmt.setString(4, feedback.getFeedbackText());
            pstmt.setDouble(5, feedback.getOriginalRating());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saving feedback: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    /**
     * Retrieves all feedback entries from the database.
     * 
     * @return List of Feedback objects
     */
    public List<Feedback> getAllFeedbacks() {
        String sql = "SELECT * FROM feedbacks";
        List<Feedback> feedbacks = new ArrayList<>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setId(rs.getInt("id"));
                feedback.setStudentName(rs.getString("student_name"));
                feedback.setStaffName(rs.getString("staff_name"));
                feedback.setCourseName(rs.getString("course_name"));
                feedback.setFeedbackText(rs.getString("feedback_text"));
                feedback.setOriginalRating(rs.getDouble("original_rating"));
                feedback.setSentimentLabel(rs.getString("sentiment_label"));
                feedback.setSentimentScore(rs.getDouble("sentiment_score"));
                feedback.setPositivePoints(rs.getString("positive_points"));
                feedback.setNegativePoints(rs.getString("negative_points"));
                feedback.setSummary(rs.getString("summary"));
                
                feedbacks.add(feedback);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving feedbacks: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
            DatabaseUtil.closeConnection(conn);
        }
        
        return feedbacks;
    }
    
    /**
     * Updates the sentiment analysis results for a feedback entry.
     * 
     * @param feedbackId The ID of the feedback to update
     * @param result The sentiment analysis result
     * @return true if successful, false otherwise
     */
    public boolean updateSentimentResults(int feedbackId, SentimentResult result) {
        String sql = "UPDATE feedbacks SET " +
                     "sentiment_label = ?, " +
                     "sentiment_score = ?, " +
                     "positive_points = ?, " +
                     "negative_points = ?, " +
                     "summary = ? " +
                     "WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, result.getLabel());
            pstmt.setDouble(2, result.getPolarity());
            pstmt.setString(3, String.join("\n", result.getPositivePoints()));
            pstmt.setString(4, String.join("\n", result.getNegativePoints()));
            pstmt.setString(5, result.getSummary());
            pstmt.setInt(6, feedbackId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating sentiment results: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    /**
     * Gets the average ratings grouped by staff name.
     * 
     * @return Map of staff names to their average ratings
     */
    public Map<String, Double> getStaffRatings() {
        String sql = "SELECT staff_name, AVG(original_rating) as avg_rating FROM feedbacks GROUP BY staff_name";
        Map<String, Double> staffRatings = new HashMap<>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String staffName = rs.getString("staff_name");
                double avgRating = Math.round(rs.getDouble("avg_rating") * 100.0) / 100.0; // Round to 2 decimal places
                staffRatings.put(staffName, avgRating);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving staff ratings: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
            DatabaseUtil.closeConnection(conn);
        }
        
        return staffRatings;
    }
    
    /**
     * Gets the average ratings grouped by course name.
     * 
     * @return Map of course names to their average ratings
     */
    public Map<String, Double> getCourseRatings() {
        String sql = "SELECT course_name, AVG(original_rating) as avg_rating FROM feedbacks GROUP BY course_name";
        Map<String, Double> courseRatings = new HashMap<>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String courseName = rs.getString("course_name");
                double avgRating = Math.round(rs.getDouble("avg_rating") * 100.0) / 100.0; // Round to 2 decimal places
                courseRatings.put(courseName, avgRating);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving course ratings: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
            DatabaseUtil.closeConnection(conn);
        }
        
        return courseRatings;
    }
    
    /**
     * Gets the count of each sentiment label.
     * 
     * @return Map of sentiment labels to their counts
     */
    public Map<String, Integer> getSentimentCounts() {
        String sql = "SELECT sentiment_label, COUNT(*) as count FROM feedbacks GROUP BY sentiment_label";
        Map<String, Integer> sentimentCounts = new HashMap<>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String label = rs.getString("sentiment_label");
                int count = rs.getInt("count");
                sentimentCounts.put(label, count);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving sentiment counts: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
            DatabaseUtil.closeConnection(conn);
        }
        
        return sentimentCounts;
    }
}
