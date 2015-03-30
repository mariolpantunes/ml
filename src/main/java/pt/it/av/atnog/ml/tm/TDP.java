package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.structures.tuple.Pair;

import java.util.List;

/**
 * Created by mantunes on 3/20/15.
 */
public class TDP {
    private final String term;
    private final List<Pair<String, Double>> proflie;

    public TDP(String term, List<Pair<String, Double>> proflie) {
        this.term = term;
        this.proflie = proflie;
    }

    public double similarity(TDP tdp) {
        return 0.0;
    }

    public static TDP learn(String term, List<String> corpus) {
        return null;
    }
}
