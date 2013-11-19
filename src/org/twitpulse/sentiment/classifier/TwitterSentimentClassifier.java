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
     * Classifies single sentiment example (i.e. predicts what sentiment is appropriate)
     * 
     * @param example
     *            sentiment example
     * @return sentiment prediction
     */
    public TwitterSentimentPrediction classify(TwitterSentimentExample example);

    /**
     * Classifies a batch of sentiment examples
     * 
     * @param example
     *            sentiment examples
     * @return sentiment predictions
     */
    public List<TwitterSentimentPrediction> batchClassify(List<TwitterSentimentExample> example);
}
