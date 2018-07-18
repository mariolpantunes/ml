package pt.it.av.tnav.ml.clustering.density;

import pt.it.av.tnav.ml.clustering.cluster.Cluster;
import pt.it.av.tnav.utils.structures.Distance;
import pt.it.av.tnav.ml.clustering.curvature.Curvature;

import java.util.List;

public interface Density {

  <D extends Distance<D>> List<Cluster<D>> clustering(final List<D> dps, final int minPts);

  <D extends Distance<D>> List<Cluster<D>> clustering(final List<D> dps,
                                                   final int minPts, final Curvature curvature);
}
