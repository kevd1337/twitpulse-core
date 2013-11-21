package org.twitpulse.webapp.api;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.twitpulse.sentiment.TwitterSentimentExample;
import org.twitpulse.sentiment.TwitterSentimentPrediction;
import org.twitpulse.sentiment.classifier.TwitterSentimentClassifier;
import org.twitpulse.sentiment.classifier.baseline.BaselineSentimentPredictor;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Simple web service for prediction sentiment of a given Tweet
 * 
 * @author kevd1337
 * 
 */
@Path("/twitpulse")
@Produces(MediaType.APPLICATION_JSON)
public class TwitterSentimentPredictionService {
    private static final TwitterSentimentClassifier SENTIMENT_CLASSIFIER = initializeClassifier();
    private static final Twitter TWITTER = initializeTwitter();

    /**
     * Initializes the classifier to be used by this service
     * 
     * @return baseline sentiment classifier
     */
    private static final TwitterSentimentClassifier initializeClassifier() {
        try {
            Double confidenceThreshold = null;
            if (System.getenv("CONFIDENCE_THRESHOLD") != null) {
                confidenceThreshold = Double.parseDouble(System.getenv("CONFIDENCE_THRESHOLD"));
            }
            BaselineSentimentPredictor classifier = new BaselineSentimentPredictor(confidenceThreshold);
            classifier.loadModel("res/baseline.model");

            return classifier;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to setup sentiment classifier, " + e.getMessage(), e);
        }
    }

    /**
     * Initializes Twitter4j Twitter instance to be used to access twitter APIs
     * 
     * @return twitter instance
     */
    private static final Twitter initializeTwitter() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(System.getenv("TWITTER_CONSUMER_KEY"))
                .setOAuthConsumerSecret(System.getenv("TWITTER_CONSUMER_SECRET"))
                .setOAuthAccessToken(System.getenv("TWITTER_ACCESS_TOKEN"))
                .setOAuthAccessTokenSecret(System.getenv("TWITTER_ACCESS_TOKEN_KEY"));
        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
        Twitter twitter = twitterFactory.getInstance();

        return twitter;
    }

    /**
     * API for predicting a twitter status' sentiment
     * 
     * @param statusId
     *            status id of Tweet
     * @param queryTerm
     *            query term that was used to find the Tweet
     * 
     * @return sentiment (where 0 is negative, 2 is neutral, and 4 is positive)
     * 
     * @throws TwitterException
     */
    @GET
    @Path("/sentiment-by-status-id")
    public Integer predictSentiment(@QueryParam("statusId") Long statusId, @QueryParam("queryTerm") String queryTerm)
            throws TwitterException {
        Status twitterStatus = TWITTER.showStatus(statusId);
        TwitterSentimentExample example = new TwitterSentimentExample(twitterStatus, queryTerm);
        List<TwitterSentimentExample> examples = Arrays.asList(example);
        List<TwitterSentimentPrediction> predictions = SENTIMENT_CLASSIFIER.classify(examples);

        return predictions.get(0).getSentiment();
    }
}
