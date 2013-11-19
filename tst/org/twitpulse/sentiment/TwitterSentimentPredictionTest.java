package org.twitpulse.sentiment;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for {@link TwitterSentimentPrediction}
 * 
 * @author kevd1337
 * 
 */
public class TwitterSentimentPredictionTest {

    @Test
    public void testConstructorPersistsData() {
        Long anyTweetId = 42l;
        String anyTweet = "What's the meaning of life, the universe, and everything?";
        Integer positiveSentiment = 4;
        Integer negativeSentiment = 0;
        Integer neutralSentiment = 2;
        Double anyConfidence = 0.5;

        TwitterSentimentPrediction actualPositivePrediction = new TwitterSentimentPrediction(anyTweetId, anyTweet,
                positiveSentiment, anyConfidence);
        assertEquals(anyTweetId, actualPositivePrediction.getTweetId());
        assertEquals(anyTweet, actualPositivePrediction.getTweet());
        assertEquals(positiveSentiment, actualPositivePrediction.getSentiment());
        assertEquals(anyConfidence, actualPositivePrediction.getConfidence());
        assertEquals(TwitterSentimentPredictionType.POSITIVE, actualPositivePrediction.getPredictionType());

        TwitterSentimentPrediction actualNegativePrediction = new TwitterSentimentPrediction(anyTweetId, anyTweet,
                negativeSentiment, anyConfidence);
        assertEquals(anyTweetId, actualNegativePrediction.getTweetId());
        assertEquals(anyTweet, actualNegativePrediction.getTweet());
        assertEquals(negativeSentiment, actualNegativePrediction.getSentiment());
        assertEquals(anyConfidence, actualNegativePrediction.getConfidence());
        assertEquals(TwitterSentimentPredictionType.NEGATIVE, actualNegativePrediction.getPredictionType());

        TwitterSentimentPrediction actualNeutralPrediction = new TwitterSentimentPrediction(anyTweetId, anyTweet,
                neutralSentiment, anyConfidence);
        assertEquals(anyTweetId, actualNeutralPrediction.getTweetId());
        assertEquals(anyTweet, actualNeutralPrediction.getTweet());
        assertEquals(neutralSentiment, actualNeutralPrediction.getSentiment());
        assertEquals(anyConfidence, actualNeutralPrediction.getConfidence());
        assertEquals(TwitterSentimentPredictionType.NEUTRAL, actualNeutralPrediction.getPredictionType());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsExceptionWhenSentimentIsNull() {
        Long anyTweetId = 123l;
        String anyTweet = "whatever";
        Double anyConfidence = 0.5;

        new TwitterSentimentPrediction(anyTweetId, anyTweet, null, anyConfidence);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrowsExceptionWhenSentimentIsBelowAcceptableRange() {
        Long anyTweetId = 123l;
        String anyTweet = "whatever";
        Integer anySentimentValueBelowLowerRange = -1;
        Double anyConfidence = 0.5;

        new TwitterSentimentPrediction(anyTweetId, anyTweet, anySentimentValueBelowLowerRange, anyConfidence);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrowsExceptionWhenSentimentIsAboveAcceptableRange() {
        Long anyTweetId = 123l;
        String anyTweet = "whatever";
        Integer anySentimentValueAboveUpperrRange = 5;
        Double anyConfidence = 0.5;
        new TwitterSentimentPrediction(anyTweetId, anyTweet, anySentimentValueAboveUpperrRange, anyConfidence);
    }
}
