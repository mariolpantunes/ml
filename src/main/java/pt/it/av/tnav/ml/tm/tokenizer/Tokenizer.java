package pt.it.av.tnav.ml.tm.tokenizer;

import pt.it.av.tnav.ml.tm.ngrams.NGram;

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
     * @return
     */
    List<String> tokenize(String input);
}
