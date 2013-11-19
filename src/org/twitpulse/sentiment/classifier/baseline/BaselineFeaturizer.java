package org.twitpulse.sentiment.classifier.baseline;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.twitpulse.sentiment.TwitterSentimentExample;

import cmu.arktweetnlp.Twokenize;

/**
 * Simple featurizer used by Baseline Predictor, based on heuristics and an quick literature search.
 * 
 * @author kevd1337
 * 
 */
public class BaselineFeaturizer {

    // To prevent instantiation of this class
    private BaselineFeaturizer() {
    }

    /**
     * Baseline features, consisting of unigrams with a few equivalence classes (query term, screennames, URLs, and
     * words with multiple occurrences of same letter)
     * 
     * @param example
     *            twitter sentiment example
     * 
     * @return unigrams (normalized string/word form) to be used as features for the given example
     */
    public static List<String> baselineUnigramFeatures(TwitterSentimentExample example) {
        String normalizedTweet = example.getTweet();
        // Query term equivalence class
        if (example.getQueryTerm() != null) {
            normalizedTweet = StringUtils.replace(normalizedTweet, example.getQueryTerm(), QUERY_TERM_EQUIVALENCE);
        }
        // Twitter screenname equivalence class
        normalizedTweet = normalizedTweet.replaceAll("@[\\S]*", USERNAME_EQUIVALENCE);

        // Embedded URL equivalence class
        normalizedTweet = normalizedTweet.replaceAll(URL, URL_EQUIVALENCE);

        // Reduce multiple consecutive occurrences of same letter in a word to equivalence class (i.e
        // noooooooooooooooooo and nooo are treated the same)
        normalizedTweet = normalizedTweet.replaceAll("(\\p{javaLetter})\\1{3,}", "$1$1");

        return Twokenize.tokenizeRawTweetText(normalizedTweet);
    }

    /**
     * Pieces for URL matching, based on what ARK's Twokenize uses
     */
    private static final String PUNCTUATION_CHARACTERS = "['\"“”‘’.?!…,:;]";
    private static final String ENTITY = "&(?:amp|lt|gt|quot);";
    private static final String URL_START_1 = "(?:https?://|\\bwww\\.)";
    private static final String COMMON_TLDS = "(?:com|org|edu|gov|net|mil|aero|asia|biz|cat|coop|info|int|jobs|mobi|museum|name|pro|tel|travel|xxx)";
    private static final String CC_TLDS = "(?:ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|"
            + "bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|"
            + "er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|"
            + "hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|"
            + "lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|"
            + "nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|"
            + "sl|sm|sn|so|sr|ss|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|"
            + "va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|za|zm|zw)";

    private static final String URL_START_2 = "\\b(?:[A-Za-z\\d-])+(?:\\.[A-Za-z0-9]+){0,3}\\." + "(?:" + COMMON_TLDS
            + "|" + CC_TLDS + ")" + "(?:\\." + CC_TLDS + ")?(?=\\W|$)";
    private static final String URL_BODY = "(?:[^\\.\\s<>][^\\s<>]*?)?";
    private static final String URL_EXTRA_CRAP_BEFORE_END = "(?:" + PUNCTUATION_CHARACTERS + "|" + ENTITY + ")+?";
    private static final String URL_END = "(?:\\.\\.+|[<>]|\\s|$)";
    private static final String URL = "(?:" + URL_START_1 + "|" + URL_START_2 + ")" + URL_BODY + "(?=(?:"
            + URL_EXTRA_CRAP_BEFORE_END + ")?" + URL_END + ")";
    private static final String QUERY_TERM_EQUIVALENCE = "QUERY_TERM";
    private static final String USERNAME_EQUIVALENCE = "USERNAME";
    private static final String URL_EQUIVALENCE = "URL";
}
