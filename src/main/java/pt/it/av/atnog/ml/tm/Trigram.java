package pt.it.av.atnog.ml.tm;

/**
 * Created by mantunes on 08/06/2015.
 */
public class Trigram extends NGram{
    public Trigram(String a, String b, String c) {
        super(3);
        array[0] = a;
        array[1] = b;
        array[2] = c;
    }
}
