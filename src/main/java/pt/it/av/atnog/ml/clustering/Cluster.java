package pt.it.av.atnog.ml.clustering;

import java.util.*;


/**
 * Cluster represents a set of elements that are similiar in some way.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public interface Cluster<T extends Element> extends Collection<T> {

    /**
     *
     * @return
     */
    double distortion();
}