# URK23CS5070 - LOKESHWARAN S

# Java Sentiment Analysis Application

This application is a Java conversion of a Python sentiment analysis project. It provides a GUI interface for submitting feedback and analyzing sentiment.

## Features

- Feedback submission form for staff and course evaluations
- Sentiment analysis of feedback using Stanford CoreNLP
- Authentication system for accessing analysis dashboard
- Multiple views for analyzing sentiment data:
  - Staff analysis
  - Course analysis
  - Overall ratings with charts
  - Table view of all feedback

## Technical Details

- **Language**: Java 17
- **GUI Framework**: JavaFX 17
- **Sentiment Analysis**: Stanford CoreNLP
- **Database**: MySQL with JDBC
- **Build Tool**: Maven

## Project Structure

The application follows the Model-View-Controller (MVC) pattern:

- **Model**: Data models and database operations
- **View**: JavaFX UI components
- **Controller**: Business logic and controllers

## Setup Instructions

### Prerequisites

- Java Development Kit (JDK) 17 or later
- Maven
- MySQL Server

### Database Setup

1. Create a MySQL database named `sentiment_feedback`
2. Create a user with access to the database
3. Update database connection settings in `DatabaseUtil.java` if needed

### Building the Application

```bash
mvn clean package
```

### Running the Application

```bash
java -jar target/sentiment-analysis-1.0-SNAPSHOT.jar
```

## Usage Guide

### Submitting Feedback

1. Select the "Submit Feedback" tab
2. Choose feedback type (Staff or Course)
3. Fill in the required fields
4. Rate on a scale of 0-5
5. Write your feedback
6. Click "Submit Feedback"

### Viewing Sentiment Analysis

1. Select the "Sentiment Analysis" tab
2. Log in with username "angelina jeba" and password "HOD"
3. Click "Run Sentiment Analysis" to process feedback
4. Choose a view option:
   - Staff Analysis: View feedback by staff member
   - Course Analysis: View feedback by course
   - Overall Ratings: View charts and average ratings
   - View Table: See all feedback in tabular format

## Color Scheme

- Primary: Blue (#3498db) - Headers and buttons
- Secondary: Green (#2ecc71) - Success messages
- Accent: Red (#e74c3c) - Error messages
- Neutral: Light Gray (#ecf0f1) - Backgrounds
- Text: Dark Blue (#2c3e50) - Main text
- Highlight: Orange (#f39c12) - Highlights and warnings
