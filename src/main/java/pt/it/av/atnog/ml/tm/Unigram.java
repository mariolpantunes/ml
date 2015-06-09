package pt.it.av.atnog.ml.tm;

/**
 * Created by mantunes on 08/06/2015.
 */
public class Unigram extends NGram {
    public Unigram(String term) {
        super(1);
        array[0] = term;
    }
}
