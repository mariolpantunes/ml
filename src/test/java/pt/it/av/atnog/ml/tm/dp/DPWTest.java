package pt.it.av.atnog.ml.tm.dp;

import java.util.ArrayList;
import java.util.List;

import pt.it.av.atnog.ml.tm.ngrams.NGram;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for DPW class.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWTest {
    @org.junit.Test
    public void test_similarity() {
        List<DPW.Coordinate> profile1 = new ArrayList<>(), profile2 = new ArrayList<>();
        profile1.add(new DPW.Coordinate(NGram.Unigram("engine"), NGram.Unigram("engine"), 6.0));
        profile1.add(new DPW.Coordinate(NGram.Unigram("wheels"), NGram.Unigram("wheels"), 4.0));
        profile1.add(new DPW.Coordinate(NGram.Unigram("door"), NGram.Unigram("door"), 3.0));

        profile2.add(new DPW.Coordinate(NGram.Unigram("engine"), NGram.Unigram("engine"), 4.0));
        profile2.add(new DPW.Coordinate(NGram.Unigram("wheels"), NGram.Unigram("wheels"), 6.0));
        profile2.add(new DPW.Coordinate(NGram.Unigram("driver"), NGram.Unigram("driver"), 4.0));
        DPW DPW1 = new DPW(NGram.Unigram("car"), profile1), DPW2 = new DPW(NGram.Unigram("bike"), profile2);


        assertTrue(DPW1.similarityTo(DPW1) == 1.0);
        assertTrue(DPW2.similarityTo(DPW2) == 1.0);
        assertTrue(DPW1.similarityTo(DPW2) == 0.7452841128534441);
        assertTrue(DPW2.similarityTo(DPW1) == 0.7452841128534441);
    }
}
