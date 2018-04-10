package pt.it.av.atnog.ml.clustering.hierarchical;

import pt.it.av.atnog.utils.structures.Distance;

import java.util.List;

public interface Hierarchical {
  <D extends Distance<? super D>> int[][] clustering(final List<D> dps);
}
