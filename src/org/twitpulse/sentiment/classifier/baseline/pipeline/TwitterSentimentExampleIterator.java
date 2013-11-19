package org.twitpulse.sentiment.classifier.baseline.pipeline;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.twitpulse.sentiment.TwitterSentimentExample;

import cc.mallet.types.Instance;

/**
 * Data iterator over TwitterSentimentExamples for used in conjunction with Mallet Pipes
 * 
 * @author kevd1337
 * 
 */
public class TwitterSentimentExampleIterator implements Iterator<Instance> {
    private final Iterator<TwitterSentimentExample> exampleIterator;
    private TwitterSentimentExample currentExample;

    /**
     * Constructor
     * 
     * @param examples
     *            list of twitter sentiment examples to iterate over
     */
    public TwitterSentimentExampleIterator(List<TwitterSentimentExample> examples) {
        this.exampleIterator = examples.iterator();

        try {
            this.currentExample = exampleIterator.next();
        } catch (NoSuchElementException e) {
            this.currentExample = null;
        }
    }

    @Override
    public boolean hasNext() {
        return currentExample != null;
    }

    @Override
    public Instance next() {
        String label = null;
        if (currentExample.getSentiment() != null) {
            label = currentExample.getSentiment().toString();
        }
        Instance carrier = new Instance(currentExample, label, UUID.randomUUID().toString(), null);

        try {
            this.currentExample = exampleIterator.next();
        } catch (NoSuchElementException e) {
            this.currentExample = null;
        }

        return carrier;
    }

    @Override
    public void remove() {
        throw new IllegalStateException("This Iterator<Instance> does not support remove().");
    }

}
