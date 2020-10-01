package pt.it.av.tnav.ml.sp;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import pt.it.av.tnav.utils.structures.point.Point2D;

public class SmoothingTest {
    /*@Test
    public void test_sma_last() {
        double values[] = { 0, 2, 4, 6, 8, 10 }, times[] = { 0, 1, 1.2, 2.3, 2.9, 5 };
        double correct[] = { 0.00, 1.80, 2.20, 5.40, 6.6, 9.0 };
        double out[] = new double[values.length];
        Smoothing.sma_last(values, times, 2, out);
        assertArrayEquals(correct, out, 0.1);
    }*/
    
    /*@Test
    public void test_sma_next() {
        double values[] = { 0, 2, 4, 6, 8, 10 }, times[] = { 0, 1, 1.2, 2.3, 2.9, 5 };
        double correct[] = { 0.00, 3.00, 3.20, 7.00, 7.60, 10.00 };
        double out[] = new double[values.length];
        Smoothing.sma_next(values, times, 2, out);
        assertArrayEquals(correct, out, 0.1);
    }*/

    @Test
    public void test_sma_linear() {
        Point2D input[] = { new Point2D(0, 0), new Point2D(1, 2), new Point2D(1.2, 4), new Point2D(2.3, 6),
                new Point2D(2.9, 8), new Point2D(5, 10) };
        Point2D correct[] = { new Point2D(0, 0.00), new Point2D(1, 2.69), new Point2D(1.2, 3.23),
                new Point2D(2.3, 6.28), new Point2D(2.9, 7.47), new Point2D(5, 9.76) };

        Point2D out[] = Smoothing.sma_linear(input, 2);
        assertArrayEquals(correct, out);
    }
}
