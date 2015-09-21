package pt.it.av.atnog.ml.optimization;

import pt.it.av.atnog.utils.bla.Vector;

/**
 * Created by mantunes on 11/27/14.
 */
public interface Gradient {
    Vector delta(Vector theta);
}
