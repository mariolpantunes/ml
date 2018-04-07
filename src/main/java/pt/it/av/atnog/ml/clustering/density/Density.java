package pt.it.av.atnog.ml.clustering.density;

import pt.it.av.atnog.ml.clustering.cluster.Cluster;
import pt.it.av.atnog.ml.clustering.curvature.Curvature;
import pt.it.av.atnog.utils.structures.Distance;

import java.util.List;

public interface Density {

  <D extends Distance> List<Cluster<D>> clustering(final List<D> dps, final int minPts);

  <D extends Distance> List<Cluster<D>> clustering(final List<D> dps,
                                                   final int minPts, final Curvature curvature);
}
