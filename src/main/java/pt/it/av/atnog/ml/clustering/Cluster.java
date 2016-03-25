package pt.it.av.atnog.ml.clustering;

import java.util.*;


/**
 * Cluster represents a set of elements that are similiar in some way.
 */
public interface Cluster<T extends Element> extends Collection<T> {

    /**
     *
     * @return
     */
    double distortion();
}