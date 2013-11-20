package org.twitpulse.cli;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.twitpulse.sentiment.TwitterSentimentExample;
import org.twitpulse.sentiment.TwitterSentimentPrediction;
import org.twitpulse.sentiment.classifier.baseline.BaselineSentimentPredictor;
import org.twitpulse.sentiment.corpus.Sentiment140CsvReader;

/**
 * Command line tool for making predictions with the twitter sentiment predictor from examples in a CSV file (formated
 * in similar fashion to the sentiment 140 data files)
 * 
 * @author kevd1337
 * 
 * @throws ParseException
 * @throws IOException
 */
public class SentimentPredictorCLI {

    /**
     * Command line entry point
     * 
     * @param args
     *            command line arguments
     * 
     * @throws ParseException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws ParseException, ClassNotFoundException, IOException {
        if (args.length != 1 && args.length != 2 && args.length != 3) {
            System.out
                    .println("Usage: SentimentPredictorCLI <model-file> <example-data-file: sentiment 140 csv file format, use sentiment label as blank or ? if value is unknown> <optional-neutral-confidence-threshold: between 0 and 1>");
            System.exit(0);
        }

        String modelFilePath = args[0];
        String exampleFilePath = args[1];
        Double confidenceThreshold = null;
        if (args.length == 3) {
            confidenceThreshold = Double.parseDouble(args[2]);
        }

        List<TwitterSentimentExample> examples = Sentiment140CsvReader.loadExamples(exampleFilePath);
        BaselineSentimentPredictor classifier = new BaselineSentimentPredictor(confidenceThreshold);
        classifier.loadModel(modelFilePath);

        List<TwitterSentimentPrediction> predictions = classifier.classify(examples);
        for (TwitterSentimentPrediction prediction : predictions) {
            System.out.println(prediction.getSentiment());
        }
    }
}
