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
    private final Double negativeConfidence;
    private final Double positiveConfidence;
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
     * @param negativeConfidence
     *            confidence from predictor that sentiment is negative (if available / applicable for predictor)
     * @param positiveConfidence
     *            confidence from predictor that sentiment is positive (if available / applicable for predictor)
     */
    public TwitterSentimentPrediction(Long tweetId, String tweet, Integer sentiment, Double negativeConfidence,
            Double positiveConfidence) {
        Validate.notNull(sentiment, "Sentiment must be specified.");
        Validate.isTrue(sentiment >= 0 && sentiment <= 4,
                "Invalid sentiment provided, value must be between 0 and 4 inclusive");

        this.tweetId = tweetId;
        this.tweet = tweet;
        this.sentiment = sentiment;
        this.negativeConfidence = negativeConfidence;
        this.positiveConfidence = positiveConfidence;

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
     * @return the negativeConfidence
     */
    public Double getNegativeConfidence() {
        return negativeConfidence;
    }

    /**
     * @return the positiveConfidence
     */
    public Double getPositiveConfidence() {
        return positiveConfidence;
    }

    /**
     * @return the predictionType
     */
    public TwitterSentimentPredictionType getPredictionType() {
        return predictionType;
    }
}
