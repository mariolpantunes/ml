package pt.it.av.atnog.ml.clusteringV2;

import pt.it.av.atnog.utils.ArrayUtils;
import pt.it.av.atnog.utils.PrintUtils;
import pt.it.av.atnog.utils.Utils;
import pt.it.av.atnog.utils.structures.Distance;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Kmeans++ implementation.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Kmeanspp extends Kmedoids {
  @Override
  protected <D extends Distance> List<Cluster<D>> init(final List<D> dps, final int mappings[],
                                                       final int k) {
    List<Cluster<D>> clusters = new ArrayList<>(k);

    if (dps.size() > 0) {
      int idxs[] = new int[dps.size()];
      for (int i = 1; i < idxs.length; i++) {
        idxs[i] = i;
      }
      ArrayUtils.shuffle(idxs);

      clusters.add(new Cluster(dps.get(idxs[0])));
      mappings[idxs[0]] = 0;

      double array[] = new double[dps.size()];

      for (int i = 1; i < k; i++) {
        double total = 0.0;
        for(int j = 0; j < dps.size(); j++) {
          if(mappings[j] >= 0)
            array[j] = 0.0;
          else
            array[j] = distanceClosestCluster(dps.get(j), clusters);
          total += array[j];
        }

        double rnd = Math.random() * total;

        int idx = -1;
        double v = rnd;
        for (int j = 0; j < array.length && idx < 0; j++) {
          rnd -= array[j];
          if (rnd < array[j])
            idx = j;
        }

        if (idx == -1) {
          System.err.println("TOT " + total);
          System.err.println("RND " + v);
          System.err.println("CUR " + rnd);
          System.err.println(PrintUtils.array(array));
        }

        clusters.add(new Cluster(dps.get(idx)));
        mappings[idx] = i;
      }
    }

    return clusters;
  }

  /**
   * This method may suffer from overflow
   *
   * @param e
   * @param clusters
   * @param <D>
   * @return
   */
  private <D extends Distance> double distanceClosestCluster(final D dp,
                                                             List<Cluster<D>> clusters) {
    double rv = clusters.get(0).center().distanceTo(dp);

    for(int i = 1; i < clusters.size(); i++){
      double tmp = clusters.get(i).center().distanceTo(dp);
      if (tmp < rv) {
        rv = tmp;
      }
    }

    return Math.pow(rv, 2.0);
  }
}
