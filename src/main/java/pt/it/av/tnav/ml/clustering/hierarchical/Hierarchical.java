package pt.it.av.tnav.ml.clustering.hierarchical;

import pt.it.av.tnav.utils.structures.Distance;

import java.util.List;

public interface Hierarchical {
  <D extends Distance<? super D>> int[][] clustering(final List<D> dps);
}
