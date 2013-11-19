package org.twitpulse.cli;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.twitpulse.sentiment.TwitterSentimentExample;
import org.twitpulse.sentiment.classifier.baseline.BaselineSentimentPredictor;
import org.twitpulse.sentiment.corpus.Sentiment140CsvReader;

/**
 * Command line tool for training the baseline twitter sentiment predictor
 * 
 * @author kevd1337
 * 
 */
public class BaselineClassifierTrainer {

    /**
     * Command line entry point
     * 
     * @param args
     *            command line arguments
     * 
     * @throws ParseException
     * @throws IOException
     */
    public static void main(String args[]) throws ParseException, IOException {
        if (args.length != 2) {
            System.out
                    .println("Usage: BaselineClassifierTrainer <sentiment-140-training-data-file> <target-model-file>");
            System.exit(0);
        }

        String trainingFilepath = args[0];
        String targetModelFilepath = args[1];

        System.out.println("Loading training data...");
        List<TwitterSentimentExample> trainingExamples = Sentiment140CsvReader.loadExamples(trainingFilepath);
        BaselineSentimentPredictor classifier = new BaselineSentimentPredictor();
        classifier.train(trainingExamples);

        System.out.println("\nSaving model...");
        classifier.saveModel(targetModelFilepath);
        System.out.println("\nDone.");
    }
}
