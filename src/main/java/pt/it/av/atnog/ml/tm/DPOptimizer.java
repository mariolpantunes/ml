package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.structures.tuple.Pair;

import java.util.List;

/**
 * Created by mantunes on 6/15/15.
 */
public interface DPOptimizer {
    List<Pair<NGram,Double>> optimize(List<Pair<NGram, Double>> profile);
}
