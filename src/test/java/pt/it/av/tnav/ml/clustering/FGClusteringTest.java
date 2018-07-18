package pt.it.av.tnav.ml.clustering;

import org.junit.BeforeClass;
import pt.it.av.tnav.ml.clustering.cluster.Cluster;
import pt.it.av.tnav.utils.structures.point.Point2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class FGClusteringTest {
  private static List<Point2D> dps = new ArrayList<>();

  @BeforeClass
  public static void setup() {
    dps.add(new Point2D(2, 2));
    dps.add(new Point2D(2, 3));
    dps.add(new Point2D(3, 2));
    dps.add(new Point2D(3, 3));
    dps.add(new Point2D(-2, -2));
    dps.add(new Point2D(-2, -3));
    dps.add(new Point2D(-3, -2));
    dps.add(new Point2D(-3, -3));
  }

  @org.junit.Test
  public void test_clustering() {
    //System.out.println("Points: " + PrintUtils.list(dps));

    FGClustering alg = new FGClustering();
    List<Cluster<Point2D>> clusters = alg.clustering(dps, 1.0);

    //System.out.println("Clusters: " + PrintUtils.list(clusters));

    // The number of points in the clusters must be the same as the initial number of points.
    int clusterPoints = 0;
    for (Cluster c : clusters) {
      clusterPoints += c.size();
      System.out.println(c);
    }
    assertTrue(clusterPoints == dps.size());

    // For this specific exemple one clusters contains positive number and the other negative numbers.
    Cluster<Point2D> c1 = clusters.get(0), c2 = clusters.get(1);

    Iterator<Point2D> it = c1.iterator();
    int quadrant = 0;
    boolean c1SameQ = true;
    if (it.hasNext()) {
      quadrant = it.next().quadrant();
    }

    while (it.hasNext()) {
      int tmpQ = it.next().quadrant();
      if (tmpQ != quadrant)
        c1SameQ = false;
    }

    it = c2.iterator();
    boolean c2SameQ = true;
    quadrant = 0;
    if (it.hasNext()) {
      quadrant = it.next().quadrant();
    }

    while (it.hasNext()) {
      int tmpQ = it.next().quadrant();
      if (tmpQ != quadrant)
        c2SameQ = false;
    }

    assertTrue(c1SameQ && c2SameQ);
  }
}
