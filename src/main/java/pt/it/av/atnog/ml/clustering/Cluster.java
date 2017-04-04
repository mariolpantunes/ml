package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.structures.Distance;

import java.util.*;


/**
 * Cluster represents a set of elements that are similiar in some way.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public interface Cluster<D extends Distance> extends Collection<D> {

    /**
     * Returns the distiortion of the cluster.
     *
     * @return the distortion of the cluster.
     */
    double distortion();

    /**
     * Returns the distortion of the this cluster merged with element e.
     *
     * @param e extra element used to computed the merged distortion.
     * @return the distortion of the cluster with the extra element.
     */
    double distortion(D e);

    /**
     * Returns the distortion resulting of merging this cluster with cluster c.
     *
     * @param c extra cluster used to computed the merged distortion.
     * @return the distortion resulting of merging this cluster with cluster c.
     */
    double distortion(Cluster<D> c);
}