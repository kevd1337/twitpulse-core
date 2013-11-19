package org.twitpulse.sentiment.classifier.baseline.pipeline;

import java.io.Serializable;
import java.util.List;

import org.twitpulse.sentiment.TwitterSentimentExample;
import org.twitpulse.sentiment.classifier.baseline.BaselineFeaturizer;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.TokenSequence;

/**
 * Mallet Pipe that takes a TwitterSentimentExample as input and passes the tokenized features along
 * 
 * @author kevd1337
 * 
 */
public class TwitterSentimentExample2BaselineFeatureTokens extends Pipe implements Serializable {
    private static final long serialVersionUID = -990722564371175180L;

    @Override
    public Instance pipe(Instance carrier) {
        TwitterSentimentExample example = (TwitterSentimentExample) carrier.getData();
        List<String> features = BaselineFeaturizer.baselineUnigramFeatures(example);

        TokenSequence tokenSequence = new TokenSequence();
        for (String featureToken : features) {
            tokenSequence.add(featureToken);
        }

        carrier.setData(tokenSequence);
        return carrier;
    }
}
