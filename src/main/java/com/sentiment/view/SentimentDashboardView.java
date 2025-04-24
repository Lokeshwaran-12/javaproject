package com.sentiment.view;

import com.sentiment.controller.AuthController;
import com.sentiment.controller.SentimentAnalyzer;
import com.sentiment.model.DatabaseManager;
import com.sentiment.model.Feedback;
import com.sentiment.model.SentimentResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Map;

/**
 * View for the sentiment analysis dashboard.
 * This replaces the sentiment_interface function in the Python version.
 */
public class SentimentDashboardView extends VBox {
    
    private final AuthController authController = new AuthController();
    private final DatabaseManager dbManager = new DatabaseManager();
    private final SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
    
    private VBox loginBox;
    private VBox dashboardBox;
    private VBox analysisResultsBox;
    
    private TextField usernameField;
    private PasswordField passwordField;
    
    /**
     * Constructor that sets up the sentiment dashboard UI.
     */
    public SentimentDashboardView() {
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: " + MainView.NEUTRAL_COLOR + ";");
        
        setupUI();
    }
    
    /**
     * Sets up the UI components for the sentiment dashboard.
     */
    private void setupUI() {
        // Title
        Text title = new Text("Sentiment Analysis Dashboard");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setStyle("-fx-fill: " + MainView.PRIMARY_COLOR + ";");
        
        // Create login box
        setupLoginBox();
        
        // Create dashboard box (initially hidden)
        setupDashboardBox();
        
        // Add components to main view
        this.getChildren().addAll(
            title,
            new Separator(),
            loginBox
        );
    }
    
    /**
     * Sets up the login box for authentication.
     */
    private void setupLoginBox() {
        loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20));
        
        Text loginPrompt = new Text("Please login to view sentiment analysis");
        loginPrompt.setFont(Font.font("System", FontWeight.NORMAL, 16));
        
        GridPane loginGrid = new GridPane();
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        loginGrid.setPadding(new Insets(20));
        loginGrid.setAlignment(Pos.CENTER);
        
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        
        loginGrid.add(new Label("Username:"), 0, 0);
        loginGrid.add(usernameField, 1, 0);
        loginGrid.add(new Label("Password:"), 0, 1);
        loginGrid.add(passwordField, 1, 1);
        
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: " + MainView.PRIMARY_COLOR + "; -fx-text-fill: white;");
        loginButton.setPrefWidth(100);
        loginButton.setOnAction(e -> handleLogin());
        
        loginBox.getChildren().addAll(loginPrompt, loginGrid, loginButton);
    }
    
    /**
     * Sets up the dashboard box for displaying sentiment analysis results.
     */
    private void setupDashboardBox() {
        dashboardBox = new VBox(15);
        dashboardBox.setAlignment(Pos.TOP_CENTER);
        dashboardBox.setPadding(new Insets(10));
        
        // Success message
        Label successLabel = new Label("Login Successful ✅");
        successLabel.setStyle("-fx-text-fill: " + MainView.SECONDARY_COLOR + "; -fx-font-weight: bold;");
        
        // Run analysis button
        Button runAnalysisButton = new Button("Run Sentiment Analysis");
        runAnalysisButton.setStyle("-fx-background-color: " + MainView.PRIMARY_COLOR + "; -fx-text-fill: white;");
        runAnalysisButton.setPrefWidth(200);
        runAnalysisButton.setOnAction(e -> runSentimentAnalysis());
        
        // View options
        ToggleGroup viewGroup = new ToggleGroup();
        RadioButton staffViewRadio = new RadioButton("Staff Analysis");
        RadioButton courseViewRadio = new RadioButton("Course Analysis");
        RadioButton overallViewRadio = new RadioButton("Overall Ratings");
        RadioButton tableViewRadio = new RadioButton("View Table");
        
        staffViewRadio.setToggleGroup(viewGroup);
        courseViewRadio.setToggleGroup(viewGroup);
        overallViewRadio.setToggleGroup(viewGroup);
        tableViewRadio.setToggleGroup(viewGroup);
        overallViewRadio.setSelected(true);
        
        HBox viewOptions = new HBox(20, 
            new Label("Select View:"), 
            staffViewRadio, 
            courseViewRadio, 
            overallViewRadio, 
            tableViewRadio
        );
        viewOptions.setAlignment(Pos.CENTER_LEFT);
        
        // Analysis results box
        analysisResultsBox = new VBox(10);
        analysisResultsBox.setPadding(new Insets(10));
        analysisResultsBox.setBorder(new Border(new BorderStroke(
            Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT
        )));
        
        // Set up view change listeners
        staffViewRadio.setOnAction(e -> showStaffAnalysis());
        courseViewRadio.setOnAction(e -> showCourseAnalysis());
        overallViewRadio.setOnAction(e -> showOverallRatings());
        tableViewRadio.setOnAction(e -> showTableView());
        
        // Add components to dashboard
        dashboardBox.getChildren().addAll(
            successLabel,
            runAnalysisButton,
            new Separator(),
            viewOptions,
            analysisResultsBox
        );
        
        // Initially show overall ratings
        showOverallRatings();
    }
    
    /**
     * Handles the login process.
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (authController.login(username, password)) {
            // Remove login box and show dashboard
            this.getChildren().remove(loginBox);
            this.getChildren().add(dashboardBox);
        } else {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password. Please try again.");
            alert.showAndWait();
        }
    }
    
    /**
     * Runs sentiment analysis on all feedback entries.
     */
    private void runSentimentAnalysis() {
        try {
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
            
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Analysis Complete");
            alert.setHeaderText(null);
            alert.setContentText("✅ Sentiment analysis completed for " + updatedCount + " entries.");
            alert.showAndWait();
            
            // Refresh current view
            RadioButton selectedRadio = (RadioButton) ((ToggleGroup) ((RadioButton) dashboardBox.lookup("RadioButton")).getToggleGroup()).getSelectedToggle();
            if (selectedRadio != null) {
                selectedRadio.fire();
            }
            
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred during sentiment analysis: " + ex.getMessage());
            alert.showAndWait();
        }
    }
    
    /**
     * Shows staff analysis view.
     */
    private void showStaffAnalysis() {
        analysisResultsBox.getChildren().clear();
        
        ComboBox<String> staffComboBox = new ComboBox<>();
        List<Feedback> feedbacks = dbManager.getAllFeedbacks();
        
        // Populate staff names
        for (Feedback feedback : feedbacks) {
            String staffName = feedback.getStaffName();
            if (staffName != null && !staffName.isEmpty() && !staffComboBox.getItems().contains(staffName)) {
                staffComboBox.getItems().add(staffName);
            }
        }
        
        if (!staffComboBox.getItems().isEmpty()) {
            staffComboBox.getSelectionModel().selectFirst();
        }
        
        Button viewButton = new Button("View Analysis");
        viewButton.setStyle("-fx-background-color: " + MainView.PRIMARY_COLOR + "; -fx-text-fill: white;");
        
        HBox selectionBox = new HBox(10, new Label("Choose Staff:"), staffComboBox, viewButton);
        selectionBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox resultsBox = new VBox(10);
        
        viewButton.setOnAction(e -> {
            String selectedStaff = staffComboBox.getValue();
            if (selectedStaff != null) {
                resultsBox.getChildren().clear();
                
                for (Feedback feedback : feedbacks) {
                    if (selectedStaff.equals(feedback.getStaffName())) {
                        VBox entryBox = createFeedbackEntryBox(feedback);
                        resultsBox.getChildren().add(entryBox);
                    }
                }
            }
        });
        
        ScrollPane scrollPane = new ScrollPane(resultsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        
        analysisResultsBox.getChildren().addAll(selectionBox, scrollPane);
    }
    
    /**
     * Shows course analysis view.
     */
    private void showCourseAnalysis() {
        analysisResultsBox.getChildren().clear();
        
        ComboBox<String> courseComboBox = new ComboBox<>();
        List<Feedback> feedbacks = dbManager.getAllFeedbacks();
        
        // Populate course names
        for (Feedback feedback : feedbacks) {
            String courseName = feedback.getCourseName();
            if (courseName != null && !courseName.isEmpty() && !courseComboBox.getItems().contains(courseName)) {
                courseComboBox.getItems().add(courseName);
            }
        }
        
        if (!courseComboBox.getItems().isEmpty()) {
            courseComboBox.getSelectionModel().selectFirst();
        }
        
        Button viewButton = new Button("View Analysis");
        viewButton.setStyle("-fx-background-color: " + MainView.PRIMARY_COLOR + "; -fx-text-fill: white;");
        
        HBox selectionBox = new HBox(10, new Label("Choose Course:"), courseComboBox, viewButton);
        selectionBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox resultsBox = new VBox(10);
        
        viewButton.setOnAction(e -> {
            String selectedCourse = courseComboBox.getValue();
            if (selectedCourse != null) {
                resultsBox.getChildren().clear();
                
                for (Feedback feedback : feedbacks) {
                    if (selectedCourse.equals(feedback.getCourseName())) {
                        VBox entryBox = createFeedbackEntryBox(feedback);
                        resultsBox.getChildren().add(entryBox);
                    }
                }
            }
        });
        
        ScrollPane scrollPane = new ScrollPane(resultsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        
        analysisResultsBox.getChildren().addAll(selectionBox, scrollPane);
    }
    
    /**
     * Shows overall ratings view with charts.
     */
    private void showOverallRatings() {
        analysisResultsBox.getChildren().clear();
        
        // Get data
        Map<String, Integer> sentimentCounts = dbManager.getSentimentCounts();
        Map<String, Double> staffRatings = dbManager.getStaffRatings();
        Map<String, Double> courseRatings = dbManager.getCourseRatings();
        
        // Create sentiment distribution chart
        PieChart sentimentChart = new PieChart();
        sentimentChart.setTitle("Sentiment Distribution");
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : sentimentCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(
                entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1), 
                entry.getValue()
            ));
        }
        sentimentChart.setData(pieChartData);
        
        // Create staff ratings table
        VBox staffRatingsBox = new VBox(5);
        Text staffTitle = new Text("Average Ratings for Staff");
        staffTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        GridPane staffGrid = new GridPane();
        staffGrid.setHgap(10);
        staffGrid.setVgap(5);
        staffGrid.add(new Label("Staff Name"), 0, 0);
        staffGrid.add(new Label("Average Rating"), 1, 0);
        
        int rowIndex = 1;
        for (Map.Entry<String, Double> entry : staffRatings.entrySet()) {
            staffGrid.add(new Label(entry.getKey()), 0, rowIndex);
            staffGrid.add(new Label(String.format("%.2f", entry.getValue())), 1, rowIndex);
            rowIndex++;
        }
        
        staffRatingsBox.getChildren().addAll(staffTitle, staffGrid);
        
        // Create course ratings table
        VBox courseRatingsBox = new VBox(5);
        Text courseTitle = new Text("Average Ratings for Courses");
        courseTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        GridPane courseGrid = new GridPane();
        courseGrid.setHgap(10);
        courseGrid.setVgap(5);
        courseGrid.add(new Label("Course Name"), 0, 0);
        courseGrid.add(new Label("Average Rating"), 1, 0);
        
        rowIndex = 1;
        for (Map.Entry<String, Double> entry : courseRatings.entrySet()) {
            courseGrid.add(new Label(entry.getKey()), 0, rowIndex);
            courseGrid.add(new Label(String.format("%.2f", entry.getValue())), 1, rowIndex);
            rowIndex++;
        }
        
        courseRatingsBox.getChildren().addAll(courseTitle, courseGrid);
        
        // Add components to results box
        HBox chartsBox = new HBox(20, sentimentChart);
        chartsBox.setPrefHeight(300);
        chartsBox.setAlignment(Pos.CENTER);
        
        HBox tablesBox = new HBox(50, staffRatingsBox, courseRatingsBox);
        tablesBox.setPadding(new Insets(20, 0, 0, 0));
        tablesBox.setAlignment(Pos.CENTER);
        
        analysisResultsBox.getChildren().addAll(chartsBox, tablesBox);
    }
    
    /**
     * Shows table view of all feedback entries.
     */
    private void showTableView() {
        analysisResultsBox.getChildren().clear();
        
        // Create table view
        TableView<Feedback> tableView = new TableView<>();
        
        // Define columns
        TableColumn<Feedback, String> studentCol = new TableColumn<>("Student");
        studentCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getStudentName()));
        
        TableColumn<Feedback, String> staffCol = new TableColumn<>("Staff");
        staffCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getStaffName()));
        
        TableColumn<Feedback, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getCourseName()));
        
        TableColumn<Feedback, String> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.1f", cellData.getValue().getOriginalRating())));
        
        TableColumn<Feedback, String> sentimentCol = new TableColumn<>("Sentiment");
        sentimentCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getSentimentLabel() != null ? 
                cellData.getValue().getSentimentLabel().substring(0, 1).toUpperCase() + 
                cellData.getValue().getSentimentLabel().substring(1) : 
                "Unknown"));
        
        TableColumn<Feedback, String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.2f", cellData.getValue().getSentimentScore())));
        
        // Add columns to table
        tableView.getColumns().addAll(studentCol, staffCol, courseCol, ratingCol, sentimentCol, scoreCol);
        
        // Get data
        List<Feedback> feedbacks = dbManager.getAllFeedbacks();
        tableView.getItems().addAll(feedbacks);
        
        // Set table properties
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Add table to results box
        analysisResultsBox.getChildren().add(tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
    }
    
    /**
     * Creates a box displaying a single feedback entry with sentiment analysis results.
     * 
     * @param feedback The feedback entry to display
     * @return A VBox containing the formatted feedback entry
     */
    private VBox createFeedbackEntryBox(Feedback feedback) {
        VBox entryBox = new VBox(5);
        entryBox.setPadding(new Insets(10));
        entryBox.setBorder(new Border(new BorderStroke(
            Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT
        )));
        
        // Staff and course info
        Label staffLabel = new Label("👨‍🏫 Staff: " + feedback.getStaffName());
        Label courseLabel = new Label("📚 Course: " + feedback.getCourseName());
        
        // Sentiment info
        String sentimentText = feedback.getSentimentLabel() != null ? 
            feedback.getSentimentLabel().substring(0, 1).toUpperCase() + 
            feedback.getSentimentLabel().substring(1) : 
            "Unknown";
            
        Label sentimentLabel = new Label("🧠 Overall Sentiment: " + sentimentText + 
            " (" + String.format("%.2f", feedback.getSentimentScore()) + ")");
        
        // Positive and negative points
        Label positiveTitle = new Label("✅ Good Points Mentioned by Students:");
        Label positivePoints = new Label(feedback.getPositivePoints() != null && !feedback.getPositivePoints().isEmpty() ? 
            feedback.getPositivePoints() : "(none)");
        positivePoints.setWrapText(true);
        
        Label negativeTitle = new Label("❌ Bad Points Mentioned by Students:");
        Label negativePoints = new Label(feedback.getNegativePoints() != null && !feedback.getNegativePoints().isEmpty() ? 
            feedback.getNegativePoints() : "(none)");
        negativePoints.setWrapText(true);
        
        // Summary
        Label summaryTitle = new Label("📝 Summary:");
        Label summary = new Label(feedback.getSummary() != null ? feedback.getSummary() : "(No summary)");
        summary.setWrapText(true);
        
        // Add all components to entry box
        entryBox.getChildren().addAll(
            staffLabel,
            courseLabel,
            sentimentLabel,
            positiveTitle,
            positivePoints,
            negativeTitle,
            negativePoints,
            summaryTitle,
            summary,
            new Separator()
        );
        
        return entryBox;
    }
}
