package pt.it.av.tnav.ml.clustering.partition;

import pt.it.av.tnav.utils.ArrayUtils;
import pt.it.av.tnav.utils.structures.Distance;
import pt.it.av.tnav.ml.clustering.cluster.Cluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * K-medoids algorithm implementation.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Kmedoids {
  /** Static library */
  private Kmedoids() {
  }

  /**
   * Create and initializes the initial centroids and clusters.
   * 
   * @param dps List of data points (Any class that implements {@link Distance})
   * @param k   number of clusters
   * @param <D>
   * @return
   */
  protected static <D extends Distance<D>> List<Cluster<D>> init(final List<D> dps, final int mappings[], final int k) {
    final List<Cluster<D>> clusters = new ArrayList<>(k);
    final int idxs[] = new int[dps.size()];

    for (int i = 1; i < idxs.length; i++) {
      idxs[i] = i;
    }

    ArrayUtils.shuffle(idxs);
    for (int i = 0; i < k; i++) {
      clusters.add(new Cluster<>(dps.get(idxs[i])));
      mappings[idxs[i]] = i;
    }
    return clusters;
  }

  /**
   * Returns the index of the closest {@link Cluster}.
   * 
   * @param dp      data point
   * @param centers {@list} of cluster centers
   * @param <D>     {@link Distance}
   * @return the index of the closest cluster
   */
  private static <D extends Distance<D>> int closestCluster(final D dp, final List<D> centers) {
    int rv = 0;
    double d = centers.get(0).distanceTo(dp);

    for (int i = 1; i < centers.size(); i++) {
      final double tmp = centers.get(i).distanceTo(dp);
      if (tmp < d) {
        d = tmp;
        rv = i;
      }
    }

    return rv;
  }

  /**
   * @param dps      List of data points (Any class that implements
   *                 {@link Distance})
   * @param clusters
   * @param <D>
   * @return
   */
  protected static <D extends Distance<D>> boolean assignment(final List<D> dps, final List<Cluster<D>> clusters,
      final List<D> centers, final int mapping[]) {
    boolean rv = false;
    for (int i = 0; i < dps.size(); i++) {
      final D dp = dps.get(i);
      final int idxC = closestCluster(dp, centers);
      if (mapping[i] != idxC) {
        rv = true;
        if (mapping[i] >= 0) {
          clusters.get(mapping[i]).remove(dp);
          mapping[i] = -1;
        }
        clusters.get(idxC).add(dp);
        mapping[i] = idxC;
      }
    }
    return rv;
  }

  public static <D extends Distance<D>> List<Cluster<D>> clustering(final List<D> dps, final int k) {
    return clustering(Kmedoids::init, dps, k);
  }

  protected static <D extends Distance<D>> List<Cluster<D>> clustering(final Init<D> c, final List<D> dps,
      final int k) {
    // mapping used to identity where each data point is attributed
    final int mapping[] = new int[dps.size()];
    // -1 means that the point is not attributed to any cluster
    Arrays.fill(mapping, -1);

    // init step
    final List<Cluster<D>> clusters = c.init(dps, mapping, k);

    // Initial centers
    final List<D> centers = new ArrayList<>(k);
    for (int i = 0; i < k; i++) {
      centers.add(clusters.get(i).center());
    }

    // assignment step
    assignment(dps, clusters, centers, mapping);

    boolean done = false;
    while (!done) {
      // update step
      for (int i = 0; i < k; i++) {
        centers.set(i, clusters.get(i).center());
      }

      // assignment step
      done = !assignment(dps, clusters, centers, mapping);
    }

    return clusters;
  }

  protected static interface Init<D extends Distance<D>> {
    public List<Cluster<D>> init(final List<D> dps, final int mappings[], final int k);
  }
}
