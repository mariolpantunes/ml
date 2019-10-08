package pt.it.av.tnav.ml.tm.stemmer;

import pt.it.av.tnav.ml.tm.ngrams.NGram;

/**
 * Implements a Distributional Profile optimizer based on P-value statistical significance.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public interface Stemmer {
    String stem(String term);
    NGram stem(NGram term);
    String[] stem(String term[]);
}
