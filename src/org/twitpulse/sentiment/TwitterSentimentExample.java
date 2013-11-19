package org.twitpulse.sentiment;

import java.util.Date;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import twitter4j.Status;

/**
 * Encapsulates a twitter sentiment example used for training and helping evaluating a given tweet's sentiment
 * 
 * @author kevd1337
 * 
 */
public class TwitterSentimentExample {
    private final String tweet;
    private final Integer sentiment;
    private final Long tweetId;
    private final Date creationDate;
    private final String queryTerm;
    private final String user;

    /**
     * Constructor
     * 
     * @param tweet
     *            text of a twitter user's status update (i.e tweet)
     * @param sentiment
     *            sentiment of the given tweet (if known), where 0 signifies negative sentiment, 2 signifies neutral,
     *            and 4 signifies positive sentiment. Null values are used to represent tweets with unknown sentiment.
     * @param tweetId
     *            id associated with the given tweet
     * @param creationDate
     *            date that tweet was created
     * @param queryTerm
     *            search query term used to find the given tweet
     * @param user
     *            user associated with given tweet
     */
    public TwitterSentimentExample(String tweet, Integer sentiment, Long tweetId, Date creationDate, String queryTerm,
            String user) {
        Validate.isTrue(sentiment == null || (sentiment.intValue() >= 0 && sentiment.intValue() <= 4),
                "If sentiment value is specified (i.e. known), the value must be between 0 and 4.");
        Validate.notNull(tweet, "Tweet must be specified.");

        this.sentiment = sentiment;
        this.tweetId = tweetId;
        this.creationDate = creationDate;
        this.queryTerm = queryTerm;
        this.user = user;
        this.tweet = tweet;
    }

    /**
     * Constructor
     * 
     * @param twitterStatus
     *            twitter status object
     * @param queryTerm
     *            search query term used to find the given tweet
     */
    public TwitterSentimentExample(Status twitterStatus, String queryTerm) {
        this(twitterStatus.getText(), null, twitterStatus.getId(), twitterStatus.getCreatedAt(), queryTerm,
                twitterStatus.getUser().getScreenName());
    }

    /**
     * @return the sentiment
     */
    public Integer getSentiment() {
        return sentiment;
    }

    /**
     * @return the tweetId
     */
    public Long getTweetId() {
        return tweetId;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return the queryTerm
     */
    public String getQueryTerm() {
        return queryTerm;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the tweet
     */
    public String getTweet() {
        return tweet;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        TwitterSentimentExample rhs = (TwitterSentimentExample) obj;
        return new EqualsBuilder().append(tweet, rhs.tweet).append(sentiment, rhs.sentiment)
                .append(tweetId, rhs.tweetId).append(creationDate, rhs.creationDate).append(queryTerm, rhs.queryTerm)
                .append(user, rhs.user).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(619, 98121).append(tweet).append(sentiment).append(tweetId).append(creationDate)
                .append(queryTerm).append(user).toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
