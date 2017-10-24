package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.structures.Distance;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * DBSCAN algorithm implementation.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class DBSCAN {

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
        List<Integer> neighbors = rangeQuery(i, dps, mapping, eps);
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
              List<Integer> neighborsQ = rangeQuery(q, dps, mapping, eps);
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
    return clusters;
  }

  private <D extends Distance> List<Integer> rangeQuery(final int idx, final List<D> dps,
                                                        final int[] mapping, final double eps) {
    return null;
  }
}
