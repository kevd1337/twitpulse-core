package org.twitpulse.sentiment.corpus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.twitpulse.sentiment.TwitterSentimentExample;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.annotations.MapToColumn;
import com.googlecode.jcsv.annotations.internal.ValueProcessorProvider;
import com.googlecode.jcsv.reader.CSVEntryParser;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.AnnotationEntryParser;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;

/**
 * Utility class for reading the training and test data from Sentiment 140's twitter sentiment corpus.
 * 
 * More information on that corpus can be found here: http://help.sentiment140.com/for-students
 * 
 * Link to corpus: http://cs.stanford.edu/people/alecmgo/trainingandtestdata.zip
 * 
 * @author kevd1337
 * 
 */
public class Sentiment140CsvReader {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));

    // Private constructor to prevent instantiation
    private Sentiment140CsvReader() {
    }

    /**
     * Loads a list of sentiment examples from a Sentiment 140 training or testing csv data file. Note that in the
     * example test file provided by Sentiment 140 appear to use identifiers for their tweets that are not necessarily
     * the same as the actual twitter status Id.
     * 
     * @param filePath
     *            path to file
     * @return list of twitter sentiment examples from file
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static List<TwitterSentimentExample> loadExamples(String filePath) throws FileNotFoundException,
            ParseException {
        Reader reader = new FileReader(new File(filePath));

        ValueProcessorProvider provider = new ValueProcessorProvider();
        CSVEntryParser<Sentiment140CsvRecord> entryParser = new AnnotationEntryParser<Sentiment140CsvRecord>(
                Sentiment140CsvRecord.class, provider);
        CSVReader<Sentiment140CsvRecord> csvRecordReader = new CSVReaderBuilder<Sentiment140CsvRecord>(reader)
                .strategy(CSVStrategy.UK_DEFAULT).entryParser(entryParser).build();

        List<TwitterSentimentExample> examples = new ArrayList<TwitterSentimentExample>();
        Iterator<Sentiment140CsvRecord> iterator = csvRecordReader.iterator();
        while (iterator.hasNext()) {
            Sentiment140CsvRecord record = iterator.next();

            Integer sentimentLabel = null;
            if (!StringUtils.isEmpty(record.getSentimentLabel()) && record.getSentimentLabel().length() == 1
                    && Character.isDigit(record.getSentimentLabel().charAt(0))) {
                sentimentLabel = Integer.parseInt(record.getSentimentLabel());
            }

            examples.add(new TwitterSentimentExample(record.getTweetText(), sentimentLabel, record.getTweetId(),
                    DATE_FORMAT.parse(record.getCreatedAt()), record.getQueryTerm(), record.getUsername()));
        }

        return examples;
    }

    /**
     * Helper bean for reading in records from Sentiment 140 CSV training and test data files.
     * 
     * @author kevd1337
     * 
     */
    public static class Sentiment140CsvRecord {
        @MapToColumn(column = 0)
        private String sentimentLabel;
        @MapToColumn(column = 1)
        private Long tweetId;
        @MapToColumn(column = 2)
        private String createdAt;
        @MapToColumn(column = 3)
        private String queryTerm;
        @MapToColumn(column = 4)
        private String username;
        @MapToColumn(column = 5)
        private String tweetText;

        /**
         * @return the sentimentLabel
         */
        public String getSentimentLabel() {
            return sentimentLabel;
        }

        /**
         * @return the tweetId
         */
        public Long getTweetId() {
            return tweetId;
        }

        /**
         * @return the createdAt
         */
        public String getCreatedAt() {
            return createdAt;
        }

        /**
         * @return the queryTerm
         */
        public String getQueryTerm() {
            return queryTerm;
        }

        /**
         * @return the username
         */
        public String getUsername() {
            return username;
        }

        /**
         * @return the tweetText
         */
        public String getTweetText() {
            return tweetText;
        }

        /**
         * @param sentimentLabel
         *            the sentimentLabel to set
         */
        public void setSentimentLabel(String sentimentLabel) {
            this.sentimentLabel = sentimentLabel;
        }

        /**
         * @param tweetId
         *            the tweetId to set
         */
        public void setTweetId(Long tweetId) {
            this.tweetId = tweetId;
        }

        /**
         * @param createdAt
         *            the createdAt to set
         */
        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        /**
         * @param queryTerm
         *            the queryTerm to set
         */
        public void setQueryTerm(String queryTerm) {
            this.queryTerm = queryTerm;
        }

        /**
         * @param username
         *            the username to set
         */
        public void setUsename(String username) {
            this.username = username;
        }

        /**
         * @param tweetText
         *            the tweetText to set
         */
        public void setTweetText(String tweetText) {
            this.tweetText = tweetText;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}
