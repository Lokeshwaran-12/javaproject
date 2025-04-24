package com.sentiment.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main application class that sets up the JavaFX GUI.
 * This replaces the Streamlit interface from the Python version.
 */
public class MainView extends Application {
    
    // Color scheme constants
    public static final String PRIMARY_COLOR = "#3498db";    // Blue
    public static final String SECONDARY_COLOR = "#2ecc71";  // Green
    public static final String ACCENT_COLOR = "#e74c3c";     // Red
    public static final String NEUTRAL_COLOR = "#ecf0f1";    // Light Gray
    public static final String TEXT_COLOR = "#2c3e50";       // Dark Blue
    public static final String HIGHLIGHT_COLOR = "#f39c12";  // Orange
    
    @Override
    public void start(Stage primaryStage) {
        // Create main layout container
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + NEUTRAL_COLOR + ";");
        
        // Create tab pane for navigation (similar to sidebar in Streamlit)
        TabPane tabPane = new TabPane();
        
        // Create tabs for different sections
        Tab feedbackTab = new Tab("Submit Feedback");
        feedbackTab.setClosable(false);
        feedbackTab.setContent(new FeedbackFormView());
        
        Tab sentimentTab = new Tab("Sentiment Analysis");
        sentimentTab.setClosable(false);
        sentimentTab.setContent(new SentimentDashboardView());
        
        // Add tabs to tab pane
        tabPane.getTabs().addAll(feedbackTab, sentimentTab);
        
        // Set tab pane as center content
        root.setCenter(tabPane);
        root.setPadding(new Insets(10));
        
        // Create scene and set it on the stage
        Scene scene = new Scene(root, 900, 700);
        
        // Add CSS styling
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        
        // Set up the stage
        primaryStage.setTitle("Sentiment Analysis System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Main method to launch the application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
