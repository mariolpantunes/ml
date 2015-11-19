package pt.it.av.atnog.ml.tm.tokenizer;

import pt.it.av.atnog.ml.tm.ngrams.NGram;

import java.util.Iterator;

/**
 * Created by mantunes on 11/17/15.
 */
public interface Tokenizer {
    Iterator<String> tokenize(String input);
    Iterator<NGram> tokenize(String input, int n);
}
