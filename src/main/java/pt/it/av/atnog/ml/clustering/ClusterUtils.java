package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.ml.tm.dp.DPPoint;
import pt.it.av.atnog.utils.structures.Distance;

import java.util.List;

/**
 * Implements methods to help deal with lists of {@link Cluster}.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class ClusterUtils {
  /**
   * Private constructor.
   * This class is a static library.
   */
  private ClusterUtils() {  }

  /**
   * Returns the average distortion of cluster's list.
   *
   * @param clusters the list of clusters.
   * @param <D> Any Class that implements {@link Distance} interface.
   * @return the average distortion of cluster's list.
   */
  public static <D extends Distance> double avgDistortion(List<Cluster<D>> clusters) {
    double rv = 0;
    // Penalizes empty clusters and clusters with only a element
    if (clusters.isEmpty() || singleElementClusters(clusters))
      rv = Double.MAX_VALUE;
    else {
      double distortion = 0.0;
      for (Cluster c : clusters)
        distortion += c.distortion();
      rv = distortion / clusters.size();
    }
    return rv;
  }

  /**
   * Returns true if all the clusters only have a single element, otherwise false.
   *
   * @param clusters the list of clusters.
   * @param <D> Any Class that implements {@link Distance} interface.
   * @return true if all the clusters only have a single element, otherwise false
   */
  public static <D extends Distance> boolean singleElementClusters(List<Cluster<D>> clusters) {
    boolean rv = true;
    for(Cluster<D> cluster : clusters) {
      if(cluster.size() > 1) {
        rv = false;
        break;
      }
    }
    return rv;
  }

  /**
   * Returns true if the list of clusters contains empty clusters, otherwise false.
   *
   * @param clusters list of clusters.
   * @param <D> Any Class that implements {@link Distance} interface.
   * @return true if the list of clusters contains empty clusters, otherwise false.
   */
  public static <D extends Distance> boolean emptyClusters(List<Cluster<D>> clusters) {
    boolean rv = false;

    for (Cluster c : clusters) {
      if (c.isEmpty()) {
        rv = true;
        break;
      }
    }

    return rv;
  }

  /**
   * Returns the average Silhouette score for the cluster's list.
   *
   * @param clusters the list of clusters.
   * @param <D> Any Class that implements {@link Distance} interface.
   * @return
   */
  public static <D extends Distance> double avgSilhouette(List<Cluster<D>> clusters) {
    double rv = 0.0, count = 0;

    if(clusters.isEmpty()) {
      rv = Double.MIN_VALUE;
    } else {
      // For all data points
      for (int c = 0; c < clusters.size(); c++) {
        Cluster<D> cluster = clusters.get(c);
        count += cluster.size();
        for (int d = 0; d < cluster.size(); d++) {
          D dp = cluster.get(d);

          // a(i) -> average distance of datum i to all data points in the cluster
          double ai = cluster.avgDistance(dp);

          // b(i) -> lowest average distance of datum i to all points in other clusters,
          // of which i is not a member
          double bi = Double.MAX_VALUE;
          for (int i = 0; i < clusters.size(); i++) {
            if (i != c) {
              double tbi = clusters.get(i).avgDistance(dp);
              if (tbi < bi) {
                bi = tbi;
              }
            }
          }
          // s(i) = b(i) - a(i) / max{b(i), a(i)}
          rv += ((bi - ai) / Double.max(bi, ai));
        }
      }
      rv /= count;
    }

    return rv;
  }
}
