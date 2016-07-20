package pt.it.av.atnog.ml.tm.corpus;

import pt.it.av.atnog.ml.tm.ngrams.NGram;

import java.util.Iterator;

/**
 * Corpus interface provides a iterator with relevant pieces of text related with the provided ngram.
 */
public interface Corpus {

    /**
     * Provides relevant pieces of text related with the provided ngram
     *
     * @param ngram used to find relevant pieces of text
     * @return iterator with the relevant pieces of text
     */
    Iterator<String> iterator(NGram ngram);
}
