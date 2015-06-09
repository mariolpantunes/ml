package pt.it.av.atnog.ml.tm;

/**
 * Created by mantunes on 08/06/2015.
 */
public class Bigram extends NGram {
    public Bigram(String a, String b) {
        super(2);
        array[0] = a;
        array[1] = b;
    }
}
