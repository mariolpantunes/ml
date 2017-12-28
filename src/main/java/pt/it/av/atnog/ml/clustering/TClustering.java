package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.ml.Iris;
import pt.it.av.atnog.utils.PrintUtils;
import pt.it.av.atnog.utils.structures.Point2D;
import pt.it.av.atnog.utils.structures.Point4D;

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
    List<Point4D> dps = Iris.load();

    //System.out.println(PrintUtils.list(points));
    List<Cluster<Point4D>> clusters = OptimalClustering.elbow(alg, dps, 2, (int)(dps.size()/10.0), 25);
    System.out.println(clusters.size());
    System.out.println(clusters);

    clusters = OptimalClustering.silhouette(alg, dps, 2, (int)(dps.size()/10.0), 25);
    System.out.println(clusters.size());
    System.out.println(clusters);

    clusters = OptimalClustering.clustering(alg, dps, 2, (int)(dps.size()/10.0), 1, 25);
    System.out.println(clusters.size());
    System.out.println(clusters);
  }
}
