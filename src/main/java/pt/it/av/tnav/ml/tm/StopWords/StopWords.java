package pt.it.av.tnav.ml.tm.StopWords;

import java.util.List;

/**
 * Interface that represents a StopWords class.
 * <p>
 * Provides methods to identify and remove stops words from a set of tokens.
 * </p>
 *
 * @author Mário Antunes
 * @version 1.0
 */
public interface StopWords {
    /**
     * Returns a List with all the stop words.
     * @see java.util.List
     *
     * @return a List with all the stop words
     */
    List<String> stopWords();

    /**
     * Return {@code true} if the token is a stop word, otherwise {@code false}
     *
     * @param token token to be verified, not {@code null}
     * @return {@code true} if the token is a stop word, otherwise {@code false}
     */
    boolean isStopWord(String token);
}
