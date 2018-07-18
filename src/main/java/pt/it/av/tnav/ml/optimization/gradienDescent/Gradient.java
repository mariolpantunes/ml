package pt.it.av.tnav.ml.optimization.gradienDescent;

import pt.it.av.tnav.utils.bla.Vector;

/**
 *
 */
public interface Gradient {
    Vector delta(Vector theta);
}
