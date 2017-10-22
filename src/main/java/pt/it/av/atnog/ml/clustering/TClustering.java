package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.PrintUtils;
import pt.it.av.atnog.utils.structures.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Test Clustering.
 * Internal use ontly....
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class TClustering {
  public static void main(String[] args) {
    Kmeans alg = new Kmeanspp();

    List<Point2D> points = generate2DPoints(15);

    System.out.println(PrintUtils.list(points));

    List<? extends Cluster> clusters = OptimalClustering.clustering(alg, points, 1, 5, 2);
    System.out.println(clusters);
  }

  private static List<Point2D> generate2DPoints(int n) {
    List<Point2D> rv = new ArrayList<>(n);
    for (int i = 0; i < n; i++)
      rv.add(new Point2D(ThreadLocalRandom.current().nextInt(-10, 10),
          ThreadLocalRandom.current().nextInt(-10, 10)));
    return rv;
  }
}
