package com.sentiment;

import com.sentiment.util.DatabaseInitializer;
import com.sentiment.view.MainView;
import javafx.application.Application;

/**
 * Main class to launch the Sentiment Analysis application.
 */
public class SentimentAnalysisApp {
    public static void main(String[] args) {
        // Initialize database schema
        boolean dbInitialized = DatabaseInitializer.initializeDatabase();
        if (!dbInitialized) {
            System.err.println("Warning: Database initialization failed. Application may not function correctly.");
        }
        
        // Launch the JavaFX application
        Application.launch(MainView.class, args);
    }
}
