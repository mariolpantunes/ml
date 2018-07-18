package pt.it.av.tnav.ml.tm.StopWords;

import pt.it.av.tnav.ml.tm.StopWords.StopWords;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * StopWords class that supports blacklisting some terms.
 * <p>
 * Enables to control and blacklist some terms of the StopWord list.
 * Can be used to process a set of tokens, but keeping special terms tipically known as stop words.
 * The keeped terms can be used to detect patterns or other relevant information.
 * </p>
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class BlacklistStopWords implements StopWords {
    private final StopWords sw;
    private final List<String> blacklist;

    /**
     * BlacklistStopWords constructor.
     *
     * @param sw another StopWord class, not {@code null}
     * @param blacklist list of stop words to be ignored, not {@code null}
     */
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

    @Override
    public boolean isStopWord(String token) {
        boolean rv = false;
        if(Collections.binarySearch(blacklist, token) < 0)
            if(sw.isStopWord(token))
                rv = true;
        return rv;
    }
}
