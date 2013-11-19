package org.twitpulse.sentiment;

import org.apache.commons.lang3.Validate;

/**
 * Information on a single twitter sentiment prediction
 * 
 * @author kevd1337
 * 
 */
public class TwitterSentimentPrediction {
    private final Long tweetId;
    private final String tweet;
    private final Integer sentiment;
    private final Double confidence;
    Double anyNegativeConfidence = 0.5;
    Double anyPositiveConfidence = 0.5;

    private final TwitterSentimentPredictionType predictionType;

    /**
     * Constructor
     * 
     * @param tweetId
     *            id associated with the given tweet
     * @param tweet
     *            text of a twitter user's status update (i.e tweet)
     * @param sentiment
     *            final predicted sentiment of the given tweet, where 0 signifies negative sentiment, 2 signifies
     *            neutral, and 4 signifies positive sentiment.
     * @param confidence
     *            confidence from predictor (if available / applicable for predictor)
     **/
    public TwitterSentimentPrediction(Long tweetId, String tweet, Integer sentiment, Double confidence) {
        Validate.notNull(sentiment, "Sentiment must be specified.");
        Validate.isTrue(sentiment >= 0 && sentiment <= 4,
                "Invalid sentiment provided, value must be between 0 and 4 inclusive");

        this.tweetId = tweetId;
        this.tweet = tweet;
        this.sentiment = sentiment;
        this.confidence = confidence;

        if (sentiment == 2) {
            this.predictionType = TwitterSentimentPredictionType.NEUTRAL;
        } else if (sentiment > 2) {
            this.predictionType = TwitterSentimentPredictionType.POSITIVE;
        } else {
            this.predictionType = TwitterSentimentPredictionType.NEGATIVE;
        }
    }

    /**
     * @return the tweetId
     */
    public Long getTweetId() {
        return tweetId;
    }

    /**
     * @return the tweet
     */
    public String getTweet() {
        return tweet;
    }

    /**
     * @return the sentiment
     */
    public Integer getSentiment() {
        return sentiment;
    }

    /**
     * @return the confidence
     */
    public Double getConfidence() {
        return confidence;
    }

    /**
     * @return the predictionType
     */
    public TwitterSentimentPredictionType getPredictionType() {
        return predictionType;
    }
}
