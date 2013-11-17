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
        Double anyNegativeConfidence = 0.5;
        Double anyPositiveConfidence = 0.5;

        TwitterSentimentPrediction actualPositivePrediction = new TwitterSentimentPrediction(anyTweetId, anyTweet,
                positiveSentiment, anyNegativeConfidence, anyPositiveConfidence);
        assertEquals(anyTweetId, actualPositivePrediction.getTweetId());
        assertEquals(anyTweet, actualPositivePrediction.getTweet());
        assertEquals(positiveSentiment, actualPositivePrediction.getSentiment());
        assertEquals(anyNegativeConfidence, actualPositivePrediction.getNegativeConfidence());
        assertEquals(anyPositiveConfidence, actualPositivePrediction.getPositiveConfidence());
        assertEquals(TwitterSentimentPredictionType.POSITIVE, actualPositivePrediction.getPredictionType());

        TwitterSentimentPrediction actualNegativePrediction = new TwitterSentimentPrediction(anyTweetId, anyTweet,
                negativeSentiment, anyNegativeConfidence, anyPositiveConfidence);
        assertEquals(anyTweetId, actualNegativePrediction.getTweetId());
        assertEquals(anyTweet, actualNegativePrediction.getTweet());
        assertEquals(negativeSentiment, actualNegativePrediction.getSentiment());
        assertEquals(anyNegativeConfidence, actualNegativePrediction.getNegativeConfidence());
        assertEquals(anyPositiveConfidence, actualNegativePrediction.getPositiveConfidence());
        assertEquals(TwitterSentimentPredictionType.NEGATIVE, actualNegativePrediction.getPredictionType());

        TwitterSentimentPrediction actualNeutralPrediction = new TwitterSentimentPrediction(anyTweetId, anyTweet,
                neutralSentiment, anyNegativeConfidence, anyPositiveConfidence);
        assertEquals(anyTweetId, actualNeutralPrediction.getTweetId());
        assertEquals(anyTweet, actualNeutralPrediction.getTweet());
        assertEquals(neutralSentiment, actualNeutralPrediction.getSentiment());
        assertEquals(anyNegativeConfidence, actualNeutralPrediction.getNegativeConfidence());
        assertEquals(anyPositiveConfidence, actualNeutralPrediction.getPositiveConfidence());
        assertEquals(TwitterSentimentPredictionType.NEUTRAL, actualNeutralPrediction.getPredictionType());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsExceptionWhenSentimentIsNull() {
        Long anyTweetId = 123l;
        String anyTweet = "whatever";
        Double anyNegativeConfidence = 0.5;
        Double anyPositiveConfidence = 0.5;

        new TwitterSentimentPrediction(anyTweetId, anyTweet, null, anyNegativeConfidence, anyPositiveConfidence);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrowsExceptionWhenSentimentIsBelowAcceptableRange() {
        Long anyTweetId = 123l;
        String anyTweet = "whatever";
        Integer anySentimentValueBelowLowerRange = -1;
        Double anyNegativeConfidence = 0.5;
        Double anyPositiveConfidence = 0.5;

        new TwitterSentimentPrediction(anyTweetId, anyTweet, anySentimentValueBelowLowerRange, anyNegativeConfidence,
                anyPositiveConfidence);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrowsExceptionWhenSentimentIsAboveAcceptableRange() {
        Long anyTweetId = 123l;
        String anyTweet = "whatever";
        Integer anySentimentValueAboveUpperrRange = 5;
        Double anyNegativeConfidence = 0.5;
        Double anyPositiveConfidence = 0.5;

        new TwitterSentimentPrediction(anyTweetId, anyTweet, anySentimentValueAboveUpperrRange, anyNegativeConfidence,
                anyPositiveConfidence);
    }
}
