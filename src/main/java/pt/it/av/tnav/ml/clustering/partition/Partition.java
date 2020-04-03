package pt.it.av.tnav.ml.clustering.partition;

import java.util.List;

import pt.it.av.tnav.ml.clustering.cluster.Cluster;
import pt.it.av.tnav.utils.structures.Distance;

public interface Partition<D extends Distance<D>> {
    public List<Cluster<D>> clustering(final List<D> dps, final int k);
}