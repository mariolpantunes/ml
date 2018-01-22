package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.ml.dataset.Dataset;
import pt.it.av.atnog.ml.dataset.Iris;
import pt.it.av.atnog.ml.clustering.elbow.AutoK;
import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.structures.Point4D;

import java.util.List;

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
    Dataset dataset = new Iris();
    List<Point4D> dps = dataset.load();

    //System.out.println(PrintUtils.list(points));
    List<Cluster<Point4D>> clusters = AutoK.elbow(alg, dps, 2, 30);
    System.out.println(clusters.size());
    System.out.println(clusters);

    clusters = AutoK.silhouette(alg, dps, 2, 30);
    System.out.println(clusters.size());
    System.out.println(clusters);

    clusters = AutoK.clustering(alg, dps, 2, 30, 4);
    System.out.println(clusters.size());
    System.out.println(clusters);
  }
}
