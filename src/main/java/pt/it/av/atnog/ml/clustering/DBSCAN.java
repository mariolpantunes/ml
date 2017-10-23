package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.structures.Distance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DBSCAN algorithm implementation.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class DBSCAN {


  //Seed set S = N \ {P}                            /* Neighbors to expand */
  //for each point Q in S {                         /* Process every seed point */
  //  if label(Q) = Noise then label(Q) = C        /* Change Noise to border point */
  //  if label(Q) ≠ undefined then continue        /* Previously processed */
  //      label(Q) = C                                 /* Label neighbor */
  //  Neighbors N = RangeQuery(DB, dist, Q, eps)   /* Find neighbors */
  //  if |N| ≥ minPts then {                       /* Density check */
  //    S = S ∪ N                                 /* Add new neighbors to seed set */
  //  }
  //}*/


  public <D extends Distance> List<Cluster<D>> clustering(final List<D> dps, final double eps,
                                                          final int minPts) {
    int clusterCount = -1;
    int mapping[] = new int[dps.size()];
    Arrays.fill(mapping, -1);

    for (int i = 0; i < dps.size(); i++) {
      if (mapping[i] == -1) {
        List<D> neighbors = rangeQuery(dps, mapping, eps);
        if (neighbors.size() < minPts) {
          mapping[i] = -2; // Represents Noise
        } else {
          ++clusterCount;
          mapping[i] = clusterCount;


        }
      }
    }

    List<Cluster<D>> clusters = new ArrayList<>();
    return clusters;
  }

  private <D extends Distance> List<D> rangeQuery(final List<D> dps, final int[] mapping,
                                                  final double eps) {
    return null;
  }
}
