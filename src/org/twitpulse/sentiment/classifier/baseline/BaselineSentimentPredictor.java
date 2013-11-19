package org.twitpulse.sentiment.classifier.baseline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.twitpulse.sentiment.TwitterSentimentExample;
import org.twitpulse.sentiment.TwitterSentimentPrediction;
import org.twitpulse.sentiment.classifier.TwitterSentimentClassifier;
import org.twitpulse.sentiment.classifier.baseline.pipeline.TwitterSentimentExample2BaselineFeatureTokens;
import org.twitpulse.sentiment.classifier.baseline.pipeline.TwitterSentimentExampleIterator;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.types.Alphabet;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.util.Randoms;

/**
 * Baseline twitter sentiment predictor.
 * 
 * @author kevd1337
 * 
 */
public class BaselineSentimentPredictor implements TwitterSentimentClassifier {
    private static final int DEFAULT_MAX_TRAINING_ROUNDS = 50;

    private Double confidenceThreshold;
    private Classifier underlyingClassifier;

    /**
     * Constructor
     */
    public BaselineSentimentPredictor() {
        this(null);
    }

    /**
     * Constructor
     * 
     * @param confidenceThreshold
     *            minimum confidence required for a definitive sentiment prediction, otherwise prediction will be set to
     *            neutral
     */
    public BaselineSentimentPredictor(Double confidenceThreshold) {
        this.confidenceThreshold = confidenceThreshold;
    }

    @Override
    public void train(List<TwitterSentimentExample> trainingExamples) {
        InstanceList instances = new InstanceList(buildTrainPipe());
        instances.addThruPipe(new TwitterSentimentExampleIterator(trainingExamples));

        System.out.println("Setting up training and testing data set splits...");
        InstanceList[] instanceLists = instances.split(new Randoms(), new double[] { 0.90, 0.10, 0.0 });

        LabelAlphabet labelAlpha = instances.get(0).getLabeling().getLabelAlphabet();

        System.out.println("Starting Training...");
        MaxEntTrainer trainer = new MaxEntTrainer();
        Classifier classifier = trainer.train(instanceLists[0], DEFAULT_MAX_TRAINING_ROUNDS);
        Trial trainingTrial = new Trial(classifier, instanceLists[0]);

        System.out.println("\nTraining evalution: ");
        System.out.println("============================");
        System.out.println("Overall accuracy:" + trainingTrial.getAccuracy());
        for (Object labelEntry : labelAlpha.toArray()) {
            System.out.println("f1(" + labelEntry + "): " + trainingTrial.getF1(labelEntry));
            System.out.println("recall(" + labelEntry + "): " + trainingTrial.getRecall(labelEntry));
            System.out.println("precision(" + labelEntry + "): " + trainingTrial.getPrecision(labelEntry));
        }

        Trial testTrial = new Trial(classifier, instanceLists[1]);
        System.out.println("\nTesting evaluation:");
        System.out.println("============================");
        System.out.println("Overall accuracy:" + testTrial.getAccuracy());
        for (Object labelEntry : labelAlpha.toArray()) {
            System.out.println("f1(" + labelEntry + "): " + testTrial.getF1(labelEntry));
            System.out.println("recall(" + labelEntry + "): " + testTrial.getRecall(labelEntry));
            System.out.println("precision(" + labelEntry + "): " + testTrial.getPrecision(labelEntry));
        }
        System.out.println("\nTraining Done.");

        this.underlyingClassifier = classifier;
    }

    @Override
    public List<TwitterSentimentPrediction> classify(List<TwitterSentimentExample> examples) {
        if (underlyingClassifier == null) {
            throw new IllegalStateException("Predictor has not been trained yet; cannot classify.");
        }

        synchronized (underlyingClassifier) {
            InstanceList instances = new InstanceList(buildClassifyPipe(underlyingClassifier.getAlphabet()));
            instances.addThruPipe(new TwitterSentimentExampleIterator(examples));

            ArrayList<Classification> classificationResults = underlyingClassifier.classify(instances);
            List<TwitterSentimentPrediction> predictions = new ArrayList<TwitterSentimentPrediction>();
            int i = 0;
            for (Classification classification : classificationResults) {
                Long tweetId = examples.get(i).getTweetId();
                String tweet = examples.get(i).getTweet();
                Integer sentiment = Integer.parseInt(classification.getLabelVector().getBestLabel().toString());
                Double confidence = classification.getLabelVector().getBestValue();

                TwitterSentimentPrediction prediction = null;

                if (sentiment != 2 && confidenceThreshold != null && confidence < confidenceThreshold) {
                    sentiment = 2;
                    confidence = 1.0;
                }

                prediction = new TwitterSentimentPrediction(tweetId, tweet, sentiment, confidence);
                predictions.add(prediction);
                i++;
            }
            return predictions;
        }
    }

    /**
     * Saves model for predictor to file; note that threshold isn't saved
     * 
     * @param targetFilePath
     *            target file path for model
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveModel(String targetFilePath) throws FileNotFoundException, IOException {
        if (underlyingClassifier == null) {
            throw new IllegalStateException("Predictor has not been trained yet; cannot be saved.");
        }

        synchronized (underlyingClassifier) {
            saveClassifier(underlyingClassifier, targetFilePath);
        }
    }

    /**
     * Loads model for predictor; note that threshold isn't set in the model
     * 
     * @param modelFilePath
     *            path to model file
     * 
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     * @throws IOException
     *             pipeList.add(new PrintInputAndTarget());
     */
    public void loadModel(String modelFilePath) throws FileNotFoundException, ClassNotFoundException, IOException {
        synchronized (underlyingClassifier) {
            this.underlyingClassifier = loadClassifier(modelFilePath);
        }
    }

    /**
     * Helper that builds training mallet pipeline
     * 
     * @return mallet pipeline
     */
    private static Pipe buildTrainPipe() {
        List<Pipe> pipeList = new ArrayList<Pipe>();

        pipeList.add(new TwitterSentimentExample2BaselineFeatureTokens());
        pipeList.add(new TokenSequence2FeatureSequence());
        pipeList.add(new Target2Label());
        pipeList.add(new FeatureSequence2FeatureVector());
        // pipeList.add(new PrintInputAndTarget());

        return new SerialPipes(pipeList);
    }

    /**
     * Helper that builds classifying mallet pipeline
     * 
     * @param dataAlphabet
     *            feature vocabulary for training data set
     * 
     * @return mallet pipeline
     */
    private static Pipe buildClassifyPipe(Alphabet dataAlphabet) {
        List<Pipe> pipeList = new ArrayList<Pipe>();

        pipeList.add(new TwitterSentimentExample2BaselineFeatureTokens());
        pipeList.add(new TokenSequence2FeatureSequence(dataAlphabet));
        pipeList.add(new FeatureSequence2FeatureVector());

        return new SerialPipes(pipeList);
    }

    /**
     * Helper that saves underlying classifier
     * 
     * @param classifier
     *            classifier to save
     * @param modelFilePath
     *            target model file path
     * 
     *            }
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void saveClassifier(Classifier classifier, String modelFilePath) throws FileNotFoundException,
            IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(modelFilePath)));
        oos.writeObject(classifier);
        oos.close();
    }

    /**
     * Helper that loads a new underlying classifier
     * 
     * @param modelFilePath
     *            model file path
     * 
     * @return classifier requested
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static Classifier loadClassifier(String modelFilePath) throws FileNotFoundException, IOException,
            ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(modelFilePath)));
        Classifier classifier = (Classifier) ois.readObject();
        ois.close();
        return classifier;
    }
}
