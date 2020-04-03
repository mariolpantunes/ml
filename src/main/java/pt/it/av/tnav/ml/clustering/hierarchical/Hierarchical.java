package pt.it.av.tnav.ml.clustering.hierarchical;

import java.util.List;

import pt.it.av.tnav.utils.structures.Distance;

public interface Hierarchical<D extends Distance<D>> {
    public int[][] clustering(final List<D> dps);
}