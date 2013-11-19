package org.twitpulse.sentiment.corpus;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.twitpulse.sentiment.TwitterSentimentExample;

/**
 * Tests for {@link Sentiment140CsvReader}
 * 
 * @author kevd1337
 * 
 */
public class Sentiment140CsvReaderTest {

    @Test
    public void testReadingEamplesFromCsvFile() throws FileNotFoundException, ParseException {
        List<TwitterSentimentExample> expectedExamples = new ArrayList<>();
        expectedExamples.add(new TwitterSentimentExample("Reading my kindle2...  Love it... Lee childs is good read.",
                4, 4l, Sentiment140CsvReader.DATE_FORMAT.parse("Mon May 11 03:18:03 UTC 2009"), "kindle2", "vcu451"));
        expectedExamples.add(new TwitterSentimentExample("Ok, first assesment of the #kindle2 ...it fucking rocks!!!",
                4, 5l, Sentiment140CsvReader.DATE_FORMAT.parse("Mon May 11 03:18:54 UTC 2009"), "kindle2", "chadfu"));

        List<TwitterSentimentExample> actualExamples = Sentiment140CsvReader
                .loadExamples("tst/test-files/test-sentiment140-format-file.csv");

        assertEquals(expectedExamples, actualExamples);
    }
}
