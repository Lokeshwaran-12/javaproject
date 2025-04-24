package com.sentiment.controller;

/**
 * Handles user authentication for the sentiment analysis dashboard.
 * This replaces the check_login function in the Python version.
 */
public class AuthController {
    
    // Hardcoded credentials (same as in Python version)
    private static final String VALID_USERNAME = "java";
    private static final String VALID_PASSWORD = "lab";
    
    /**
     * Validates user credentials.
     * 
     * @param username The username to validate
     * @param password The password to validate
     * @return true if credentials are valid, false otherwise
     */
    public boolean login(String username, String password) {
        return VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password);
    }
}
