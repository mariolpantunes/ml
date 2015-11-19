package pt.it.av.atnog.ml.tm.StopWords;

import pt.it.av.atnog.ml.tm.StopWords.StopWords;

import java.util.List;

/**
 * Created by mantunes on 8/6/15.
 */
public class BlacklistStopWords implements StopWords {
    private final StopWords sw;
    private final List<String> blacklist;

    public BlacklistStopWords(final StopWords sw, final List<String> blacklist) {
        this.sw = sw;
        this.blacklist = blacklist;
    }

    @Override
    public List<String> stopWords() {
        List<String> stopwords = sw.stopWords();
        stopwords.removeAll(blacklist);
        return stopwords;
    }
}
