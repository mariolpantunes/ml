package pt.it.av.atnog.ml.clustering.cluster;

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
   * Null clusters will not be considered.
   * Empty or clusters with a single element will be penalized with infinity distortion.
   *
   * @param clusters the list of clusters.
   * @param <D> Any Class that implements {@link Distance} interface.
   * @return the average distortion of cluster's list.
   */
  public static <D extends Distance<D>> double avgDistortion(List<Cluster<D>> clusters) {
    double rv = 0;
    // Penalizes empty clusters and clusters with only a element
    if (clusters.isEmpty() || singleElementClusters(clusters))
      rv = Double.POSITIVE_INFINITY;
    else {
      double distortion = 0.0;
      int n = 0;
      for (Cluster c : clusters)
        if(c != null) {
          distortion += c.distortion();
          n++;
        }
      rv = distortion / n;
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
  public static <D extends Distance<D>> boolean singleElementClusters(List<Cluster<D>> clusters) {
    boolean rv = true;
    for(Cluster<D> cluster : clusters) {
      if(cluster != null && cluster.size() > 1) {
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
  public static <D extends Distance<D>> boolean emptyClusters(List<Cluster<D>> clusters) {
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
   * Null clusters will not be considered.
   *
   * @param clusters the list of clusters.
   * @param <D> Any Class that implements {@link Distance} interface.
   * @return
   */
  public static <D extends Distance<D>> double avgSilhouette(List<Cluster<D>> clusters) {
    double rv = -1.0, count = 0;

    if (clusters.size() > 1) {
      // For all data points
      for (int c = 0; c < clusters.size(); c++) {
        Cluster<D> cluster = clusters.get(c);
        if (cluster != null) {
          count += cluster.size();
          for (int d = 0; d < cluster.size(); d++) {
            D dp = cluster.get(d);

            // a(i) -> average distance of datum i to all data points in the cluster
            double ai = cluster.avgDistance(dp);

            // b(i) -> lowest average distance of datum i to all points in other clusters,
            // of which i is not a member
            double bi = Double.MAX_VALUE;
            for (int i = 0; i < clusters.size(); i++) {
              if (i != c && clusters.get(i) != null) {
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
      }
      rv /= count;
    }

    return rv;
  }
}
