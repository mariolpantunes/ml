package pt.it.av.atnog.ml.tm.tokenizer;

import pt.it.av.atnog.ml.tm.ngrams.NGram;

import java.util.Iterator;
import java.util.List;

/**
 * Created by mantunes on 11/17/15.
 */
public interface Tokenizer {
    Iterator<String> tokenizeIt(String input);
    Iterator<NGram> tokenizeIt(String input, int n);
    List<String> tokenize(String input);
}
