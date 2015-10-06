package pt.it.av.atnog.ml.tm.stemmer;

import pt.it.av.atnog.ml.tm.ngrams.NGram;

/**
 * Created by mantunes on 10/6/15.
 */
public interface Stemmer {
    String stem(String term);
    NGram stem(NGram term);
}
