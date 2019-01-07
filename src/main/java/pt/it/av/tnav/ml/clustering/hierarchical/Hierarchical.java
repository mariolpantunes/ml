package pt.it.av.tnav.ml.clustering.hierarchical;

import pt.it.av.tnav.ml.clustering.cluster.Cluster;
import pt.it.av.tnav.utils.structures.Distance;

import java.util.List;

public interface Hierarchical {
  /**
   *
   * @param dps
   * @param <D>
   * @return
   */
  <D extends Distance<D>> int[][] clustering(final List<D> dps);

  /**
   *
   * @param dps
   * @param min
   * @param max
   * @param <D>
   * @return
   */
  <D extends Distance<D>> List<Cluster<D>> clustering(final List<D> dps, int min, int max);
}
