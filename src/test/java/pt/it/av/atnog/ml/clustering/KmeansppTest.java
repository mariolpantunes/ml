package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.MathUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for K-means++ algorithm.
 */
public class KmeansppTest {
    @org.junit.Test
    public void test_clustering() {
        final int NPoints = 10;
        List<Point1D> points = generateRandomPoints1D(NPoints);

        //System.out.println("Points: " + PrintUtils.list(points));

        Kmeans alg = new Kmeanspp();
        List<? extends Cluster<Element<Point1D>>> clusters = alg.clustering(points, 2);

        //System.out.println("Clusters: " + PrintUtils.list(clusters));

        // The number of points in the clusters must be the same as the initial number of points.
        int clusterPoints = 0;
        for (Cluster c : clusters)
            clusterPoints += c.size();
        assertTrue(clusterPoints == NPoints);

        // For this specific exemple one clusters contains positive number and the other negative numbers.
        Cluster<Element<Point1D>> c1 = clusters.get(0), c2 = clusters.get(1);

        Iterator<Element<Point1D>> it = c1.iterator();
        boolean c1SameSign = true;
        int sign = 0;
        if (it.hasNext()) {
            double x = it.next().element().x;
            sign = (Math.signum(x) != 0.0) ? (int) Math.signum(x) : 1;
        }

        while (it.hasNext()) {
            double x = it.next().element().x;
            int tmpSign = (Math.signum(x) != 0.0) ? (int) Math.signum(x) : 1;
            if (tmpSign != sign)
                c1SameSign = false;

        }

        it = c2.iterator();
        boolean c2SameSign = true;
        sign = 0;
        if (it.hasNext()) {
            double x = it.next().element().x;
            sign = (Math.signum(x) != 0.0) ? (int) Math.signum(x) : 1;
        }

        while (it.hasNext()) {
            double x = it.next().element().x;
            int tmpSign = (Math.signum(x) != 0.0) ? (int) Math.signum(x) : 1;
            if (tmpSign != sign)
                c2SameSign = false;

        }

        assertTrue(c1SameSign && c2SameSign);
    }

    /**
     * Generate a random list of 1-dimenonsion points.
     *
     * @param nPoints the number of point to generate
     * @return list with random points
     */
    private final List<Point1D> generateRandomPoints1D(int nPoints) {
        List<Point1D> points = new ArrayList<Point1D>(nPoints);
        for (int i = 0; i < nPoints; i++) {
            points.add(new Point1D(MathUtils.randomBetween(-10, 10)));
        }
        return points;
    }
}
