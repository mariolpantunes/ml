package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.ArrayUtils;
import pt.it.av.atnog.utils.structures.Distance;

import java.util.*;

/**
 * DBSCAN algorithm implementation.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class DBSCAN {

  public <D extends Distance> List<Cluster<D>> clustering(final List<D> dps, final int minPts) {
    // array with average distance to closest minPts
    double dist[] = new double[dps.size()];

    // Find minPits closer points
    for (int i = 0; i < dps.size(); i++) {
      dist[i] = ArrayUtils.mean(kCloserPoints(dps, i, minPts));
    }
    // Sort distances
    Arrays.sort(dist);

    //System.err.println(PrintUtils.array(dist));
    //System.err.println("EPS = "+dist[AutoThres.elbow(x, dist)]);

    // Find elbow and use it as EPS
    return clustering(dps, dist[AutoThres.elbow(dist)], minPts);
  }

  public static <D extends Distance> double[] kCloserPoints(List<D> dps, final int idx, final int k) {
    double rv[] = new double[k];

    D dp = dps.get(idx);

    // Init the return array with the first k element distance, that are not the idx point
    int i = 0, rvIdx = 0;
    while (rvIdx < k) {
      if (i != idx) {
        rv[rvIdx] = dp.distanceTo(dps.get(i));
        rvIdx++;
      }
      i++;
    }

    // Find the index of the larger distance
    int maxIdx = ArrayUtils.max(rv);

    for (; i < dps.size(); i++) {
      if (i != idx) {
        double distance = dp.distanceTo(dps.get(i));
        if (distance < rv[maxIdx]) {
          rv[maxIdx] = distance;
          maxIdx = ArrayUtils.max(rv);
        }
      }
    }

    return rv;
  }

  /**
   *
   * @param dps
   * @param eps
   * @param minPts
   * @param <D>
   * @return
   */
  public <D extends Distance> List<Cluster<D>> clustering(final List<D> dps, final double eps,
                                                          final int minPts) {
    int clusterCount = -1;
    int mapping[] = new int[dps.size()];
    Arrays.fill(mapping, -1);

    for (int i = 0; i < dps.size(); i++) {
      if (mapping[i] == -1) {
        List<Integer> neighbors = rangeQuery(i, dps, eps);
        if (neighbors.size() < minPts) {
          mapping[i] = -2; // Represents Noise
        } else {
          ++clusterCount;
          mapping[i] = clusterCount;
          Deque<Integer> seeds = new ArrayDeque<>(neighbors);
          while(!seeds.isEmpty()) {
            int q = seeds.pollFirst();
            if(mapping[q] == -2) {
              mapping[q] = clusterCount;
            } else if(mapping[q] == -1) {
              mapping[q] = clusterCount;
              List<Integer> neighborsQ = rangeQuery(q, dps, eps);
              if (neighborsQ.size() >= minPts) {
                for(int j = 0; j < neighborsQ.size(); j++) {
                  seeds.addLast(neighborsQ.get(j));
                }
              }
            }
          }
        }
      }
    }

    List<Cluster<D>> clusters = new ArrayList<>();
    for (int i = 0; i <= clusterCount; i++) {
      clusters.add(new Cluster<D>());
    }

    for (int i = 0; i < mapping.length; i++) {
      if (mapping[i] >= 0) {
        clusters.get(mapping[i]).add(dps.get(i));
      }
    }

    return clusters;
  }

  /**
   * Returns a list with the indexes of the closest neighbors.
   * This method implements the sequencial search.
   *
   * @param idx
   * @param dps
   * @param eps
   * @param <D>
   * @return
   */
  private <D extends Distance> List<Integer> rangeQuery(final int idx, final List<D> dps,
                                                        final double eps) {
    List<Integer> rv = new ArrayList<>();
    D dp = dps.get(idx);

    for(int i = 0;  i < dps.size(); i++) {
      if(i != idx && dp.distanceTo(dps.get(i)) <= eps) {
        rv.add(i);
      }
    }

    return rv;
  }
}
