package com.sentiment.model;

import java.util.List;

/**
 * Represents the result of sentiment analysis on a text.
 */
public class SentimentResult {
    private double polarity;
    private String label;
    private List<String> positivePoints;
    private List<String> negativePoints;
    private String summary;

    public SentimentResult() {
    }

    public SentimentResult(double polarity, String label, List<String> positivePoints, 
                          List<String> negativePoints, String summary) {
        this.polarity = polarity;
        this.label = label;
        this.positivePoints = positivePoints;
        this.negativePoints = negativePoints;
        this.summary = summary;
    }

    public double getPolarity() {
        return polarity;
    }

    public void setPolarity(double polarity) {
        this.polarity = polarity;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getPositivePoints() {
        return positivePoints;
    }

    public void setPositivePoints(List<String> positivePoints) {
        this.positivePoints = positivePoints;
    }

    public List<String> getNegativePoints() {
        return negativePoints;
    }

    public void setNegativePoints(List<String> negativePoints) {
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
        return "SentimentResult{" +
                "polarity=" + polarity +
                ", label='" + label + '\'' +
                ", positivePoints=" + positivePoints +
                ", negativePoints=" + negativePoints +
                ", summary='" + summary + '\'' +
                '}';
    }
}
