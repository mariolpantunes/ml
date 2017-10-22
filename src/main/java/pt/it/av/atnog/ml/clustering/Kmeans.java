package pt.it.av.atnog.ml.clustering;


import pt.it.av.atnog.utils.structures.Distance;

import java.util.List;

/**
 * Kmeans algorithms interface.
 * TODO: in the future maybe can become centroid based algorithms
 * TODO: due to contrains in time, for now I am going to implement Kmedoid and Kmeans++
 * TODO: in the future I will try do to add single linkage clustering and a density based algorithm
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public interface Kmeans {

    /**
     * @param dps
     * @param k
     * @param <D>
     * @return
     */
    <D extends Distance> List<Cluster<D>> clustering(final List<D> dps, final int k);
}
