package pt.it.av.atnog.ml.tm.tokenizer;

import pt.it.av.atnog.ml.tm.ngrams.NGram;

import java.util.Iterator;
import java.util.List;

/**
 *
 */
public interface Tokenizer {
    /**
     *
     * @param input
     * @return
     */
    Iterator<String> tokenizeIt(String input);

    /**
     *
     * @param input
     * @param n
     * @return
     */
    Iterator<NGram> tokenizeIt(String input, int n);

    /**
     *
     * @param input
     * @return
     */
    List<String> tokenize(String input);

    /**
     *
     * @param input
     * @param n
     * @return
     */
    List<NGram> tokenize(String input, int n);
}
