package pt.it.av.atnog.ml.clustering;


import pt.it.av.atnog.ml.clustering.cluster.Cluster;
import pt.it.av.atnog.utils.ArrayUtils;
import pt.it.av.atnog.utils.structures.Distance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * K-medoid algorithm implementation.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Kmedoids implements Kmeans {

  /**
   * @param dps List of data points (Any class that implements {@link Distance})
   * @param k   number of clusters
   * @param <D>
   * @return
   */
  protected <D extends Distance<D>> List<Cluster<D>> init(final List<D> dps, final int mappings[],
                                                          final int k) {
    List<Cluster<D>> clusters = new ArrayList<>(k);
    int idxs[] = new int[dps.size()];

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
   * @param dp
   * @param centers
   * @param <D>
   * @return
   */
  private <D extends Distance<D>> int closestCluster(final D dp, final List<D> centers) {
    int rv = 0;
    double d = centers.get(0).distanceTo(dp);

    for (int i = 1; i < centers.size(); i++) {
      double tmp = centers.get(i).distanceTo(dp);
      if (tmp < d) {
        d = tmp;
        rv = i;
      }
    }

    return rv;
  }

  /**
   * @param dps      List of data points (Any class that implements {@link Distance})
   * @param clusters
   * @param <D>
   * @return
   */
  protected <D extends Distance<D>> boolean assignment(final List<D> dps,
                                                       final List<Cluster<D>> clusters,
                                                       final List<D> centers, final int mapping[]) {
    boolean rv = false;
    for (int i = 0; i < dps.size(); i++) {
      D dp = dps.get(i);
      int idxC = closestCluster(dp, centers);
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

  public <D extends Distance<D>> List<Cluster<D>> clustering(final List<D> dps, final int k) {
    // mapping used to identity where each datapoint is attributed
    int mapping[] = new int[dps.size()];
    // -1 means that the point is not atribuated to any cluster
    Arrays.fill(mapping, -1);

    // init step
    List<Cluster<D>> clusters = init(dps, mapping, k);

    // Initial centers
    List<D> centers = new ArrayList<>(k);
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
}
