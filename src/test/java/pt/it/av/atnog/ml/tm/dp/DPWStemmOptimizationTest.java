package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.ml.tm.ngrams.NGram;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for DPW Stemm optimization algorithm.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWStemmOptimizationTest {
    @org.junit.Test
    public void test_similarity() {
        List<DPW.Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new DPW.Coordinate(NGram.Unigram("engine"), NGram.Unigram("engin"), 6.0));
        coordinates.add(new DPW.Coordinate(NGram.Unigram("engines"), NGram.Unigram("engin"), 6.0));
        coordinates.add(new DPW.Coordinate(NGram.Unigram("wheel"), NGram.Unigram("wheel"), 4.0));
        coordinates.add(new DPW.Coordinate(NGram.Unigram("wheels"), NGram.Unigram("wheel"), 4.0));
        coordinates.add(new DPW.Coordinate(NGram.Unigram("driver"), NGram.Unigram("driver"), 3.0));
        coordinates.add(new DPW.Coordinate(NGram.Unigram("drivers"), NGram.Unigram("driver"), 3.0));

        DPW DPW = new DPW(NGram.Unigram("car"), coordinates);
        DPW.optimize(new DPWStemmOptimization());
        List<DPW.Coordinate> opt_coordinates = DPW.coordinates();

        assertTrue(opt_coordinates.size() == 3);
        assertTrue(opt_coordinates.contains(new DPW.Coordinate(NGram.Unigram("engine"), NGram.Unigram("engin"), 12.0)));
        assertTrue(opt_coordinates.contains(new DPW.Coordinate(NGram.Unigram("wheel"), NGram.Unigram("wheel"), 8.0)));
        assertTrue(opt_coordinates.contains(new DPW.Coordinate(NGram.Unigram("driver"), NGram.Unigram("driver"), 6.0)));
    }
}
