package pt.it.av.atnog.ml.tm;

/**
 * Created by mantunes on 6/15/15.
 */
public class NGramStats {
    protected int rawFrequency, documentFrequency;

    @Override
    public String toString() {
        return "("+rawFrequency+"; "+documentFrequency+")";
    }
}
