package pt.it.av.tnav.ml.tm.dp;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.it.av.tnav.ml.tm.ngrams.NGram;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for DPW class.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWTest {

    private static DPW dpw1, dpw2;

    @BeforeClass
    public static void setup() {
        List<DPW.DpDimension> profile1 = new ArrayList<>(), profile2 = new ArrayList<>();
        profile1.add(new DPW.DpDimension(NGram.Unigram("engine"), NGram.Unigram("engine"), 6.0));
        profile1.add(new DPW.DpDimension(NGram.Unigram("wheels"), NGram.Unigram("wheels"), 4.0));
        profile1.add(new DPW.DpDimension(NGram.Unigram("door"), NGram.Unigram("door"), 3.0));
        dpw1 = new DPW(NGram.Unigram("car"), profile1);
        profile2.add(new DPW.DpDimension(NGram.Unigram("engine"), NGram.Unigram("engine"), 4.0));
        profile2.add(new DPW.DpDimension(NGram.Unigram("wheels"), NGram.Unigram("wheels"), 6.0));
        profile2.add(new DPW.DpDimension(NGram.Unigram("driver"), NGram.Unigram("driver"), 4.0));
        dpw2 = new DPW(NGram.Unigram("bike"), profile2);
    }

    @Test
    public void test_similarity() {
        assertTrue(dpw1.similarityTo(dpw1) == 1.0);
        assertTrue(dpw2.similarityTo(dpw2) == 1.0);
        assertTrue(dpw1.similarityTo(dpw2) == 0.7452841128534441);
        assertTrue(dpw2.similarityTo(dpw1) == 0.7452841128534441);
    }

    @org.junit.Test
    public void test_getCoor() {
        assertTrue(dpw1.dimention(NGram.Unigram("banana")) == 0.0);
        assertTrue(dpw1.dimention(NGram.Unigram("engine")) == 6.0);
        assertTrue(dpw1.dimention(NGram.Unigram("wheels")) == 4.0);
        assertTrue(dpw1.dimention(NGram.Unigram("door")) == 3.0);
        assertTrue(dpw2.dimention(NGram.Unigram("banana")) == 0.0);
        assertTrue(dpw2.dimention(NGram.Unigram("engine")) == 4.0);
        assertTrue(dpw2.dimention(NGram.Unigram("wheels")) == 6.0);
        assertTrue(dpw2.dimention(NGram.Unigram("driver")) == 4.0);
    }
}
