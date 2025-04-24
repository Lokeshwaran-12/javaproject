# Java Architecture Design for Sentiment Analysis Application

## Overview
This document outlines the architecture for converting the Python-based sentiment analysis application to Java with a GUI interface. The architecture follows the Model-View-Controller (MVC) pattern to ensure separation of concerns and maintainability.

## Package Structure
```
com.sentiment
├── model/           # Data models and database operations
│   ├── Feedback.java
│   ├── SentimentResult.java
│   └── DatabaseManager.java
├── controller/      # Business logic and controllers
│   ├── SentimentAnalyzer.java
│   ├── FeedbackController.java
│   └── AuthController.java
├── view/            # GUI components
│   ├── MainView.java
│   ├── FeedbackFormView.java
│   ├── SentimentDashboardView.java
│   ├── LoginView.java
│   └── components/  # Reusable UI components
└── util/            # Utility classes
    ├── DatabaseUtil.java
    └── TextAnalysisUtil.java
```

## Component Mapping (Python to Java)

### Database Connection
- **Python**: `db.py` with MySQL connector
- **Java**: `DatabaseManager.java` using JDBC for MySQL

### Sentiment Analysis
- **Python**: `sentiment.py` using TextBlob
- **Java**: `SentimentAnalyzer.java` using Stanford CoreNLP

### User Interface
- **Python**: Streamlit components in `app.py`
- **Java**: JavaFX views in the `view` package

## Class Descriptions

### Model Layer

#### `Feedback.java`
- Represents a feedback entry with properties matching database fields
- Properties: id, studentName, staffName, courseName, feedbackText, originalRating, sentimentLabel, sentimentScore, positivePoints, negativePoints, summary

#### `SentimentResult.java`
- Represents the result of sentiment analysis
- Properties: polarity, label, positivePoints, negativePoints, summary

#### `DatabaseManager.java`
- Handles all database operations
- Methods:
  - `getConnection()`: Establishes database connection
  - `saveFeedback()`: Inserts new feedback
  - `getAllFeedbacks()`: Retrieves all feedback entries
  - `updateSentimentResults()`: Updates sentiment analysis results
  - `getStaffRatings()`: Gets average ratings by staff
  - `getCourseRatings()`: Gets average ratings by course

### Controller Layer

#### `SentimentAnalyzer.java`
- Performs sentiment analysis on feedback text
- Methods:
  - `analyzeSentiment()`: Analyzes sentiment of text
  - `extractPoints()`: Extracts positive and negative points
  - `runBatchAnalysis()`: Processes all feedback entries

#### `FeedbackController.java`
- Manages feedback submission and retrieval
- Methods:
  - `submitFeedback()`: Validates and saves feedback
  - `getFeedbackSummary()`: Gets summary statistics
  - `getFeedbackByStaff()`: Filters feedback by staff
  - `getFeedbackByCourse()`: Filters feedback by course

#### `AuthController.java`
- Handles user authentication
- Methods:
  - `login()`: Validates user credentials

### View Layer

#### `MainView.java`
- Main application window with navigation
- Components: Navigation sidebar, content area

#### `FeedbackFormView.java`
- Form for submitting feedback
- Components: Input fields, radio buttons, slider, submit button

#### `SentimentDashboardView.java`
- Dashboard for viewing sentiment analysis results
- Components: Analysis views, charts, tables

#### `LoginView.java`
- Login screen for authentication
- Components: Username and password fields, login button

### Utility Layer

#### `DatabaseUtil.java`
- Database utility functions
- Methods: Connection pooling, query helpers

#### `TextAnalysisUtil.java`
- Text processing utilities
- Methods: Text normalization, sentence splitting

## Technology Stack

### Core Technologies
- **Java**: JDK 17 or later
- **GUI Framework**: JavaFX 17
- **Database**: MySQL with JDBC connector
- **Sentiment Analysis**: Stanford CoreNLP
- **Build Tool**: Maven or Gradle

### External Libraries
- **Stanford CoreNLP**: For sentiment analysis
- **MySQL Connector/J**: For database connectivity
- **JFoenix**: For modern JavaFX components (optional)
- **ControlsFX**: For additional JavaFX controls (optional)
- **Apache Commons**: For utility functions

## Color Scheme
- Primary: #3498db (Blue) - Headers, primary buttons
- Secondary: #2ecc71 (Green) - Success messages, positive sentiment
- Accent: #e74c3c (Red) - Error messages, negative sentiment
- Neutral: #ecf0f1 (Light Gray) - Background
- Text: #2c3e50 (Dark Blue) - Main text
- Highlight: #f39c12 (Orange) - Highlights, warnings

## Implementation Strategy
1. Set up project structure and dependencies
2. Implement database connection and models
3. Develop sentiment analysis engine
4. Create basic GUI components
5. Implement controllers and business logic
6. Connect all components
7. Apply styling and finalize UI
8. Test and refine
