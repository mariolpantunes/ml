package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.ml.tm.ngrams.NGram;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by mantunes on 3/28/16.
 */
public class DPStemmOptimizationTest {
    @org.junit.Test
    public void test_similarity() {
        List<DP.Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new DP.Coordinate(NGram.Unigram("engine"), NGram.Unigram("engin"), 6.0));
        coordinates.add(new DP.Coordinate(NGram.Unigram("engines"), NGram.Unigram("engin"), 6.0));
        coordinates.add(new DP.Coordinate(NGram.Unigram("wheel"), NGram.Unigram("wheel"), 4.0));
        coordinates.add(new DP.Coordinate(NGram.Unigram("wheels"), NGram.Unigram("wheel"), 4.0));
        coordinates.add(new DP.Coordinate(NGram.Unigram("driver"), NGram.Unigram("driver"), 3.0));
        coordinates.add(new DP.Coordinate(NGram.Unigram("drivers"), NGram.Unigram("driver"), 3.0));

        DP dp = new DP(NGram.Unigram("car"), coordinates);
        dp.optimize(new DPStemmOptimization());
        List<DP.Coordinate> opt_coordinates = dp.coordinates();

        assertTrue(opt_coordinates.size() == 3);
        assertTrue(opt_coordinates.contains(new DP.Coordinate(NGram.Unigram("engine"), NGram.Unigram("engin"), 12.0)));
        assertTrue(opt_coordinates.contains(new DP.Coordinate(NGram.Unigram("wheel"), NGram.Unigram("wheel"), 8.0)));
        assertTrue(opt_coordinates.contains(new DP.Coordinate(NGram.Unigram("driver"), NGram.Unigram("driver"), 6.0)));
    }
}
