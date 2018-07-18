package pt.it.av.tnav.ml.clustering.density;

import pt.it.av.tnav.ml.clustering.cluster.Cluster;
import pt.it.av.tnav.utils.structures.Distance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OPTICS {

  /**
   *
   */
  private OPTICS() {}

  /**
   *
   * @param dps
   * @param eps
   * @param minPts
   * @param <D>
   * @return
   */
  public <D extends Distance<D>> List<Cluster<D>> clustering(final List<D> dps, final double eps,
                                                             final int minPts) {
    double rd[] = new double[dps.size()];
    boolean pr[] = new boolean[dps.size()];
    Arrays.fill(rd, -1);

    for (int i = 0; i < dps.size(); i++) {
      if (!pr[i]) {
        List<Integer> neighbors = rangeQuery(i, dps, eps);
        pr[i] = true;

      }
    }

    return null;
  }

  private <D extends Distance<D>> List<Integer> rangeQuery(final int idx, final List<D> dps,
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
