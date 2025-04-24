package com.sentiment.controller;

import com.sentiment.model.DatabaseManager;
import com.sentiment.model.Feedback;
import com.sentiment.model.SentimentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller for feedback operations.
 * This handles the business logic between the view and model layers.
 */
public class FeedbackController {
    
    private final DatabaseManager dbManager;
    private final SentimentAnalyzer sentimentAnalyzer;
    
    /**
     * Constructor that initializes the controller with database and sentiment analyzer.
     */
    public FeedbackController() {
        this.dbManager = new DatabaseManager();
        this.sentimentAnalyzer = new SentimentAnalyzer();
    }
    
    /**
     * Submits a new feedback entry.
     * 
     * @param studentName The name of the student
     * @param staffName The name of the staff
     * @param courseName The name of the course
     * @param feedbackText The feedback text
     * @param rating The rating (0-5)
     * @return true if submission was successful, false otherwise
     */
    public boolean submitFeedback(String studentName, String staffName, String courseName, 
                                 String feedbackText, double rating) {
        // Validate input
        if (studentName == null || studentName.trim().isEmpty() ||
            staffName == null || staffName.trim().isEmpty() ||
            courseName == null || courseName.trim().isEmpty() ||
            feedbackText == null || feedbackText.trim().isEmpty() ||
            rating < 0 || rating > 5) {
            return false;
        }
        
        // Create feedback object
        Feedback feedback = new Feedback(
            studentName.trim(),
            staffName.trim(),
            courseName.trim(),
            feedbackText.trim(),
            rating
        );
        
        // Save to database
        return dbManager.saveFeedback(feedback);
    }
    
    /**
     * Runs sentiment analysis on all feedback entries.
     * 
     * @return Number of entries updated
     */
    public int runSentimentAnalysis() {
        List<Feedback> feedbacks = dbManager.getAllFeedbacks();
        int updatedCount = 0;
        
        for (Feedback feedback : feedbacks) {
            SentimentResult result = sentimentAnalyzer.analyzeSentiment(feedback.getFeedbackText());
            
            boolean success = dbManager.updateSentimentResults(
                feedback.getId(), 
                result
            );
            
            if (success) {
                updatedCount++;
            }
        }
        
        return updatedCount;
    }
    
    /**
     * Gets feedback entries filtered by staff name.
     * 
     * @param staffName The staff name to filter by
     * @return List of feedback entries for the specified staff
     */
    public List<Feedback> getFeedbackByStaff(String staffName) {
        if (staffName == null || staffName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Feedback> allFeedbacks = dbManager.getAllFeedbacks();
        List<Feedback> filteredFeedbacks = new ArrayList<>();
        
        for (Feedback feedback : allFeedbacks) {
            if (staffName.equals(feedback.getStaffName())) {
                filteredFeedbacks.add(feedback);
            }
        }
        
        return filteredFeedbacks;
    }
    
    /**
     * Gets feedback entries filtered by course name.
     * 
     * @param courseName The course name to filter by
     * @return List of feedback entries for the specified course
     */
    public List<Feedback> getFeedbackByCourse(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Feedback> allFeedbacks = dbManager.getAllFeedbacks();
        List<Feedback> filteredFeedbacks = new ArrayList<>();
        
        for (Feedback feedback : allFeedbacks) {
            if (courseName.equals(feedback.getCourseName())) {
                filteredFeedbacks.add(feedback);
            }
        }
        
        return filteredFeedbacks;
    }
    
    /**
     * Gets all feedback entries.
     * 
     * @return List of all feedback entries
     */
    public List<Feedback> getAllFeedbacks() {
        return dbManager.getAllFeedbacks();
    }
    
    /**
     * Gets sentiment counts.
     * 
     * @return Map of sentiment labels to counts
     */
    public Map<String, Integer> getSentimentCounts() {
        return dbManager.getSentimentCounts();
    }
    
    /**
     * Gets average ratings by staff.
     * 
     * @return Map of staff names to average ratings
     */
    public Map<String, Double> getStaffRatings() {
        return dbManager.getStaffRatings();
    }
    
    /**
     * Gets average ratings by course.
     * 
     * @return Map of course names to average ratings
     */
    public Map<String, Double> getCourseRatings() {
        return dbManager.getCourseRatings();
    }
}
