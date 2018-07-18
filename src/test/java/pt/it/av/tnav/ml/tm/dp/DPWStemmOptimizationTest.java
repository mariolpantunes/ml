package pt.it.av.tnav.ml.tm.dp;

import pt.it.av.tnav.ml.tm.ngrams.NGram;

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
        List<DPW.DpDimension> dpDimensions = new ArrayList<>();
        dpDimensions.add(new DPW.DpDimension(NGram.Unigram("engine"), NGram.Unigram("engin"), 6.0));
        dpDimensions.add(new DPW.DpDimension(NGram.Unigram("engines"), NGram.Unigram("engin"), 6.0));
        dpDimensions.add(new DPW.DpDimension(NGram.Unigram("wheel"), NGram.Unigram("wheel"), 4.0));
        dpDimensions.add(new DPW.DpDimension(NGram.Unigram("wheels"), NGram.Unigram("wheel"), 4.0));
        dpDimensions.add(new DPW.DpDimension(NGram.Unigram("driver"), NGram.Unigram("driver"), 3.0));
        dpDimensions.add(new DPW.DpDimension(NGram.Unigram("drivers"), NGram.Unigram("driver"), 3.0));

        DPW DPW = new DPW(NGram.Unigram("car"), dpDimensions);
        DPW.optimize(new DPWStemmOptimization());
        List<DPW.DpDimension> opt_dpDimensions = DPW.dimentions();

        assertTrue(opt_dpDimensions.size() == 3);
        assertTrue(opt_dpDimensions.contains(new DPW.DpDimension(NGram.Unigram("engine"), NGram.Unigram("engin"), 12.0)));
        assertTrue(opt_dpDimensions.contains(new DPW.DpDimension(NGram.Unigram("wheel"), NGram.Unigram("wheel"), 8.0)));
        assertTrue(opt_dpDimensions.contains(new DPW.DpDimension(NGram.Unigram("driver"), NGram.Unigram("driver"), 6.0)));
    }
}
