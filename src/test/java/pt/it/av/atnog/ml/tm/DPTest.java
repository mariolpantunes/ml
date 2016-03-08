package pt.it.av.atnog.ml.tm;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.utils.structures.tuple.Pair;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for DP class.
 * Not sure if this class is going to be used in the long run.
 */
public class DPTest {
    @org.junit.Test
    public void test_similarity() {
        List<Pair<NGram, Double>> profile1 = new ArrayList<>(), profile2 = new ArrayList<>();
        profile1.add(new Pair(NGram.Unigram("engine"), 6.0));
        profile1.add(new Pair(NGram.Unigram("wheels"), 4.0));
        profile1.add(new Pair(NGram.Unigram("door"), 3.0));

        profile2.add(new Pair(NGram.Unigram("engine"), 4.0));
        profile2.add(new Pair(NGram.Unigram("wheels"), 6.0));
        profile2.add(new Pair(NGram.Unigram("driver"), 4.0));
        DP dp1 = new DP(NGram.Unigram("car"), profile1), dp2 = new DP(NGram.Unigram("bike"), profile2);


        assertTrue(dp1.similarity(dp1) == 1.0);
        assertTrue(dp2.similarity(dp2) == 1.0);
        assertTrue(dp1.similarity(dp2) == 0.7452841128534441);
        assertTrue(dp2.similarity(dp1) == 0.7452841128534441);
    }
}
