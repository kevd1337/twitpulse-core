package org.twitpulse.sentiment;

import static org.junit.Assert.*;

import java.util.Date;

import org.easymock.EasyMock;
import org.junit.Test;

import twitter4j.Status;
import twitter4j.User;

/**
 * Tests for {@link TwitterSentimentExample}
 * 
 * @author kevd1337
 * 
 */
public class TwitterSentimentExampleTest {

    @Test
    public void testFullySpecifiedConstructorPersistsData() {
        String anyTweet = "How you like them apples?";
        Integer anySentiment = 4;
        Long anyTweetId = 42l;
        Date anyCreationDate = new Date();
        String anyQueryTerm = "apples";
        String anyUser = "MattDamon";

        TwitterSentimentExample actualSentimentExample = new TwitterSentimentExample(anyTweet, anySentiment,
                anyTweetId, anyCreationDate, anyQueryTerm, anyUser);

        assertEquals(anyTweet, actualSentimentExample.getTweet());
        assertEquals(anySentiment, actualSentimentExample.getSentiment());
        assertEquals(anyTweetId, actualSentimentExample.getTweetId());
        assertEquals(anyCreationDate, actualSentimentExample.getCreationDate());
        assertEquals(anyQueryTerm, actualSentimentExample.getQueryTerm());
        assertEquals(anyUser, actualSentimentExample.getUser());
    }

    @Test
    public void testStatusStringConstructorPersistsData() {
        String anyTweet = "Am I Bipolar? I'm #biwinning.";
        Long anyTweetId = 777l;
        Date anyCreationDate = new Date();
        String anyQueryTerm = "winning";
        String anyUser = "CharlieSheen";

        User mockUser = EasyMock.createMock(User.class);
        EasyMock.expect(mockUser.getScreenName()).andReturn(anyUser);

        Status mockStatus = EasyMock.createMock(Status.class);
        EasyMock.expect(mockStatus.getText()).andReturn(anyTweet);
        EasyMock.expect(mockStatus.getId()).andReturn(anyTweetId);
        EasyMock.expect(mockStatus.getCreatedAt()).andReturn(anyCreationDate);
        EasyMock.expect(mockStatus.getUser()).andReturn(mockUser);

        EasyMock.replay(mockUser, mockStatus);

        TwitterSentimentExample actualSentimentExample = new TwitterSentimentExample(mockStatus, anyQueryTerm);

        assertEquals(anyTweet, actualSentimentExample.getTweet());
        assertNull(actualSentimentExample.getSentiment());
        assertEquals(anyTweetId, actualSentimentExample.getTweetId());
        assertEquals(anyCreationDate, actualSentimentExample.getCreationDate());
        assertEquals(anyQueryTerm, actualSentimentExample.getQueryTerm());
        assertEquals(anyUser, actualSentimentExample.getUser());

        EasyMock.verify(mockUser, mockStatus);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsExcpetionWhenTweetIsMissing() {
        Integer anySentiment = 4;
        Long anyTweetId = 123l;
        Date anyCreationDate = new Date();
        String anyQueryTerm = "you";
        String anyUser = "me";

        new TwitterSentimentExample(null, anySentiment, anyTweetId, anyCreationDate, anyQueryTerm, anyUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrowsExcpetionWhenSentimentIsNegative() {
        String anyTweet = "Whateva! I do what I want #cartman";
        Integer anySentimentBelowBoundary = -123;
        Long anyTweetId = 93423l;
        Date anyCreationDate = new Date();
        String anyQueryTerm = "south+park";
        String anyUser = "eric";

        new TwitterSentimentExample(anyTweet, anySentimentBelowBoundary, anyTweetId, anyCreationDate, anyQueryTerm,
                anyUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrowsExcpetionWhenSentimentIsGreaterThan4() {
        String anyTweet = "All your base is belong to us";
        Integer anySentimentAboveBoundary = 619;
        Long anyTweetId = 98121l;
        Date anyCreationDate = new Date();
        String anyQueryTerm = "war";
        String anyUser = "koreans";

        new TwitterSentimentExample(anyTweet, anySentimentAboveBoundary, anyTweetId, anyCreationDate, anyQueryTerm,
                anyUser);
    }
}
