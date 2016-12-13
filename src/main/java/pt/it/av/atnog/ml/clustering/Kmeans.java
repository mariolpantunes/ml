package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.structures.Distance;

import java.util.List;

/**
 * Kmeans algorithms interface.
 * TODO: in the future maybe can become centroid based algorithms
 * TODO: due to contrains in time, for now I am going to implement Kmedoid and Kmeans++
 * TODO: in the future I will try do to add single linkage clustering and a density based algorithm
 *
 * @author Mário Antunes
 * @version 1.0
 */
public interface Kmeans {
    /**
     *
     * @param objects
     * @param k
     * @param <E>
     * @return
     */
    <D extends Distance> List<? extends Cluster<Element<D>>> clustering(List<D> objects, int k);
}
