package com.sentiment.util;

/**
 * Utility class for text analysis operations.
 */
public class TextAnalysisUtil {
    
    /**
     * Normalizes text by removing extra whitespace and trimming.
     * 
     * @param text The text to normalize
     * @return Normalized text
     */
    public static String normalizeText(String text) {
        if (text == null) {
            return "";
        }
        return text.trim().replaceAll("\\s+", " ");
    }
    
    /**
     * Splits text into sentences.
     * This is a simple implementation; Stanford CoreNLP provides more sophisticated sentence splitting.
     * 
     * @param text The text to split
     * @return Array of sentences
     */
    public static String[] splitSentences(String text) {
        if (text == null || text.isEmpty()) {
            return new String[0];
        }
        
        // Simple sentence splitting by common sentence terminators
        return text.split("[.!?]+\\s*");
    }
    
    /**
     * Truncates text to specified length with ellipsis if needed.
     * 
     * @param text The text to truncate
     * @param maxLength Maximum length
     * @return Truncated text
     */
    public static String truncateText(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        
        return text.substring(0, maxLength) + "...";
    }
}
