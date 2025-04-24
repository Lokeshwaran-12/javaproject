package com.sentiment.model;

/**
 * Represents a feedback entry with properties matching database fields.
 */
public class Feedback {
    private int id;
    private String studentName;
    private String staffName;
    private String courseName;
    private String feedbackText;
    private double originalRating;
    private String sentimentLabel;
    private double sentimentScore;
    private String positivePoints;
    private String negativePoints;
    private String summary;

    public Feedback() {
    }

    public Feedback(String studentName, String staffName, String courseName, 
                   String feedbackText, double originalRating) {
        this.studentName = studentName;
        this.staffName = staffName;
        this.courseName = courseName;
        this.feedbackText = feedbackText;
        this.originalRating = originalRating;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public double getOriginalRating() {
        return originalRating;
    }

    public void setOriginalRating(double originalRating) {
        this.originalRating = originalRating;
    }

    public String getSentimentLabel() {
        return sentimentLabel;
    }

    public void setSentimentLabel(String sentimentLabel) {
        this.sentimentLabel = sentimentLabel;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public String getPositivePoints() {
        return positivePoints;
    }

    public void setPositivePoints(String positivePoints) {
        this.positivePoints = positivePoints;
    }

    public String getNegativePoints() {
        return negativePoints;
    }

    public void setNegativePoints(String negativePoints) {
        this.negativePoints = negativePoints;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", studentName='" + studentName + '\'' +
                ", staffName='" + staffName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", feedbackText='" + feedbackText + '\'' +
                ", originalRating=" + originalRating +
                ", sentimentLabel='" + sentimentLabel + '\'' +
                ", sentimentScore=" + sentimentScore +
                ", positivePoints='" + positivePoints + '\'' +
                ", negativePoints='" + negativePoints + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
