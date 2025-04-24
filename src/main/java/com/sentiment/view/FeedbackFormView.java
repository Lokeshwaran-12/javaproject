package com.sentiment.view;

import com.sentiment.model.DatabaseManager;
import com.sentiment.model.Feedback;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * View for the feedback submission form.
 * This replaces the feedback_form function in the Python version.
 */
public class FeedbackFormView extends VBox {
    
    private final ToggleGroup feedbackTypeGroup = new ToggleGroup();
    private final ComboBox<String> staffComboBox = new ComboBox<>();
    private final ComboBox<String> courseComboBox = new ComboBox<>();
    private final TextField staffTextField = new TextField();
    private final TextField courseTextField = new TextField();
    private final TextField studentNameField = new TextField();
    private final Slider ratingSlider = new Slider(0, 5, 3);
    private final TextArea feedbackTextArea = new TextArea();
    private final DatabaseManager dbManager = new DatabaseManager();
    
    /**
     * Constructor that sets up the feedback form UI.
     */
    public FeedbackFormView() {
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: " + MainView.NEUTRAL_COLOR + ";");
        
        setupUI();
    }
    
    /**
     * Sets up the UI components for the feedback form.
     */
    private void setupUI() {
        // Title
        Text title = new Text("Student Feedback Form");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setStyle("-fx-fill: " + MainView.PRIMARY_COLOR + ";");
        
        // Feedback type selection
        RadioButton staffRadio = new RadioButton("Staff");
        RadioButton courseRadio = new RadioButton("Course");
        staffRadio.setToggleGroup(feedbackTypeGroup);
        courseRadio.setToggleGroup(feedbackTypeGroup);
        staffRadio.setSelected(true);
        
        HBox radioBox = new HBox(20, new Label("Feedback For:"), staffRadio, courseRadio);
        radioBox.setAlignment(Pos.CENTER_LEFT);
        
        // Initialize dropdown options
        staffComboBox.getItems().addAll("Prof. Meena", "Dr. Kumar", "Mrs. Latha");
        courseComboBox.getItems().addAll("AI", "DBMS", "DSA");
        
        // Grid for form fields
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(10));
        
        // Staff/Course selection based on feedback type
        Label staffCourseLabel = new Label("Select Staff Name:");
        formGrid.add(staffCourseLabel, 0, 0);
        formGrid.add(staffComboBox, 1, 0);
        formGrid.add(new Label("Course Name:"), 0, 1);
        formGrid.add(courseTextField, 1, 1);
        
        // Student name
        formGrid.add(new Label("Your Name:"), 0, 2);
        formGrid.add(studentNameField, 1, 2);
        
        // Rating slider
        formGrid.add(new Label("Rate (0 - 5):"), 0, 3);
        ratingSlider.setShowTickLabels(true);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setMajorTickUnit(1);
        ratingSlider.setMinorTickCount(1);
        ratingSlider.setBlockIncrement(0.5);
        ratingSlider.setSnapToTicks(true);
        
        Label ratingValueLabel = new Label("3.0");
        ratingSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            // Round to nearest 0.5
            double roundedValue = Math.round(newVal.doubleValue() * 2) / 2.0;
            ratingSlider.setValue(roundedValue);
            ratingValueLabel.setText(String.format("%.1f", roundedValue));
        });
        
        HBox sliderBox = new HBox(10, ratingSlider, ratingValueLabel);
        formGrid.add(sliderBox, 1, 3);
        
        // Feedback text
        formGrid.add(new Label("Write Feedback Summary:"), 0, 4);
        feedbackTextArea.setPrefRowCount(5);
        formGrid.add(feedbackTextArea, 1, 4);
        
        // Submit button
        Button submitButton = new Button("Submit Feedback");
        submitButton.setStyle("-fx-background-color: " + MainView.PRIMARY_COLOR + "; -fx-text-fill: white;");
        submitButton.setPrefWidth(150);
        
        // Toggle visibility based on feedback type selection
        staffRadio.setOnAction(e -> {
            staffCourseLabel.setText("Select Staff Name:");
            formGrid.getChildren().remove(staffTextField);
            formGrid.getChildren().remove(courseComboBox);
            
            if (!formGrid.getChildren().contains(staffComboBox)) {
                formGrid.add(staffComboBox, 1, 0);
            }
            if (formGrid.getChildren().contains(courseTextField)) {
                formGrid.getChildren().remove(courseTextField);
            }
            formGrid.add(courseTextField, 1, 1);
        });
        
        courseRadio.setOnAction(e -> {
            staffCourseLabel.setText("Select Course Name:");
            formGrid.getChildren().remove(staffComboBox);
            formGrid.getChildren().remove(courseTextField);
            
            if (!formGrid.getChildren().contains(courseComboBox)) {
                formGrid.add(courseComboBox, 1, 0);
            }
            if (formGrid.getChildren().contains(staffTextField)) {
                formGrid.getChildren().remove(staffTextField);
            }
            formGrid.add(staffTextField, 1, 1);
        });
        
        // Submit button action
        submitButton.setOnAction(e -> submitFeedback());
        
        // Add all components to the view
        this.getChildren().addAll(
            title,
            new Separator(),
            radioBox,
            formGrid,
            submitButton
        );
    }
    
    /**
     * Handles the feedback submission process.
     */
    private void submitFeedback() {
        try {
            // Get selected feedback type
            RadioButton selectedRadioButton = (RadioButton) feedbackTypeGroup.getSelectedToggle();
            boolean isStaffFeedback = "Staff".equals(selectedRadioButton.getText());
            
            // Get form values
            String staffName = isStaffFeedback ? 
                staffComboBox.getValue() : 
                staffTextField.getText();
                
            String courseName = isStaffFeedback ? 
                courseTextField.getText() : 
                courseComboBox.getValue();
                
            String studentName = studentNameField.getText();
            double rating = ratingSlider.getValue();
            String feedbackText = feedbackTextArea.getText();
            
            // Validate input
            if (staffName == null || staffName.trim().isEmpty() ||
                courseName == null || courseName.trim().isEmpty() ||
                studentName == null || studentName.trim().isEmpty() ||
                feedbackText == null || feedbackText.trim().isEmpty()) {
                
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill all fields.");
                return;
            }
            
            // Create feedback object
            Feedback feedback = new Feedback(
                studentName,
                staffName,
                courseName,
                feedbackText,
                rating
            );
            
            // Save to database
            boolean success = dbManager.saveFeedback(feedback);
            
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Feedback submitted successfully!");
                clearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit feedback. Please try again.");
            }
            
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + ex.getMessage());
        }
    }
    
    /**
     * Clears the form fields after successful submission.
     */
    private void clearForm() {
        studentNameField.clear();
        staffTextField.clear();
        courseTextField.clear();
        feedbackTextArea.clear();
        ratingSlider.setValue(3.0);
        staffComboBox.getSelectionModel().clearSelection();
        courseComboBox.getSelectionModel().clearSelection();
    }
    
    /**
     * Shows an alert dialog with the specified type, title, and message.
     * 
     * @param type The alert type
     * @param title The alert title
     * @param message The alert message
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
