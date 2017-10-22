package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.structures.Point1D;
import pt.it.av.atnog.utils.structures.Point2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for K-medoids algorithm.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class KmedoidsTest {

  @org.junit.Test
  public void test_clustering() {
    List<Point1D> points = new ArrayList<Point1D>();
    points.add(new Point1D(10.0));
    points.add(new Point1D(8.0));
    points.add(new Point1D(5.0));
    points.add(new Point1D(-5.0));
    points.add(new Point1D(-8.0));
    points.add(new Point1D(-10.0));

    //System.out.println("Points: " + PrintUtils.list(points));

    Kmedoids alg = new Kmedoids();
    List<Cluster<Point1D>> clusters = alg.clustering(points, 2);

    //System.out.println("Clusters: " + PrintUtils.list(clusters));

    // The number of points in the clusters must be the same as the initial number of points.
    int clusterPoints = 0;
    for (Cluster c : clusters)
      clusterPoints += c.size();
    System.err.println(clusterPoints);
    System.err.println(points.size());
    assertTrue(clusterPoints == points.size());

    // For this specific exemple one clusters contains positive number and the other negative numbers.
    Cluster<Point1D> c1 = clusters.get(0),
        c2 = clusters.get(1);

    Iterator<Point1D> it = c1.iterator();
    boolean c1SameSign = true;
    int sign = 0;
    if (it.hasNext()) {
      double x = it.next().x();
      sign = (Math.signum(x) != 0.0) ? (int) Math.signum(x) : 1;
    }

    while (it.hasNext()) {
      double x = it.next().x();
      int tmpSign = (Math.signum(x) != 0.0) ? (int) Math.signum(x) : 1;
      if (tmpSign != sign)
        c1SameSign = false;

    }

    it = c2.iterator();
    boolean c2SameSign = true;
    sign = 0;
    if (it.hasNext()) {
      double x = it.next().x();
      sign = (Math.signum(x) != 0.0) ? (int) Math.signum(x) : 1;
    }

    while (it.hasNext()) {
      double x = it.next().x();
      int tmpSign = (Math.signum(x) != 0.0) ? (int) Math.signum(x) : 1;
      if (tmpSign != sign)
        c2SameSign = false;

    }

    assertTrue(c1SameSign && c2SameSign);
  }

  @org.junit.Test(timeout = 3000)
  public void test_loop() {
    List<Point2D> points = new ArrayList<>();
    points.add(new Point2D(-3.0, -6.0));
    points.add(new Point2D(5.0, -8.0));
    points.add(new Point2D(-3.0, -10.0));
    points.add(new Point2D(-1.0, 5.0));
    points.add(new Point2D(-9.0, -2.0));

    Kmedoids alg = new Kmedoids();
    List<Cluster<Point2D>> clusters = alg.clustering(points, 2);
    assertTrue(clusters != null);
  }
}