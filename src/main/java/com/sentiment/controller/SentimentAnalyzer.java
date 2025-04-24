package com.sentiment.controller;

import com.sentiment.model.SentimentResult;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Handles sentiment analysis of text using Stanford CoreNLP.
 * This replaces the TextBlob functionality from the Python version.
 */
public class SentimentAnalyzer {
    private final StanfordCoreNLP pipeline;

    /**
     * Initializes the Stanford CoreNLP pipeline with necessary annotators.
     */
    public SentimentAnalyzer() {
        // Set up pipeline properties
        Properties props = new Properties();
        // Set the list of annotators to run
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        // Build pipeline
        pipeline = new StanfordCoreNLP(props);
    }

    /**
     * Analyzes the sentiment of the given text.
     * 
     * @param text The text to analyze
     * @return A SentimentResult object containing polarity, label, and extracted points
     */
    public SentimentResult analyzeSentiment(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new SentimentResult(0.0, "neutral", new ArrayList<>(), new ArrayList<>(), "");
        }

        // Create an empty annotation just with the given text
        Annotation document = new Annotation(text);
        
        // Run all annotators on this text
        pipeline.annotate(document);
        
        // Calculate average sentiment score
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        double totalScore = 0.0;
        
        for (CoreMap sentence : sentences) {
            // Get the sentiment tree and score
            int sentiment = RNNCoreAnnotations.getPredictedClass(
                    sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class));
            // Convert from 0-4 scale to -1 to 1 scale (similar to TextBlob's polarity)
            double sentimentScore = (sentiment - 2) / 2.0;
            totalScore += sentimentScore;
        }
        
        // Calculate average sentiment
        double avgScore = sentences.isEmpty() ? 0.0 : totalScore / sentences.size();
        
        // Determine sentiment label (similar to Python version thresholds)
        String label;
        if (avgScore > 0.1) {
            label = "positive";
        } else if (avgScore < -0.1) {
            label = "negative";
        } else {
            label = "neutral";
        }
        
        // Extract positive and negative points
        List<String> positivePoints = new ArrayList<>();
        List<String> negativePoints = new ArrayList<>();
        
        for (CoreMap sentence : sentences) {
            String sentenceText = sentence.get(CoreAnnotations.TextAnnotation.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(
                    sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class));
            double sentimentScore = (sentiment - 2) / 2.0;
            
            // Using same thresholds as Python version
            if (sentimentScore > 0.2) {
                positivePoints.add(sentenceText);
            } else if (sentimentScore < -0.2) {
                negativePoints.add(sentenceText);
            }
        }
        
        // Create a simple summary (first 200 chars, like Python version)
        String summary = text.length() > 200 ? text.substring(0, 200) : text;
        
        // Round the score to 2 decimal places (like Python version)
        double roundedScore = Math.round(avgScore * 100.0) / 100.0;
        
        return new SentimentResult(roundedScore, label, positivePoints, negativePoints, summary);
    }

    /**
     * Runs batch sentiment analysis on a list of feedback entries.
     * This method would be called from a controller that interacts with the database.
     * 
     * @param feedbackTexts List of feedback text entries to analyze
     * @return List of sentiment results
     */
    public List<SentimentResult> runBatchAnalysis(List<String> feedbackTexts) {
        List<SentimentResult> results = new ArrayList<>();
        
        for (String text : feedbackTexts) {
            results.add(analyzeSentiment(text));
        }
        
        return results;
    }
}
