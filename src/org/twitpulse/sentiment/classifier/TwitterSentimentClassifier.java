package org.twitpulse.sentiment.classifier;

import java.util.List;

import org.twitpulse.sentiment.TwitterSentimentExample;
import org.twitpulse.sentiment.TwitterSentimentPrediction;

/**
 * Interface of twitter sentiment prediction classifiers
 * 
 * @author kevd1337
 * 
 */
public interface TwitterSentimentClassifier {

    /**
     * Trains classifier
     * 
     * @param trainingExamples
     *            training sentiment examples
     */
    public void train(List<TwitterSentimentExample> trainingExamples);

    /**
     * Classifies a batch of sentiment examples (i.e. predicts what sentiment is appropriate)
     * 
     * @param examples
     *            sentiment examples
     * 
     * @return sentiment predictions
     */
    public List<TwitterSentimentPrediction> classify(List<TwitterSentimentExample> examples);
}
