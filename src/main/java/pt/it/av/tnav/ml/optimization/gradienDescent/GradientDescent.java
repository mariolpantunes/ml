package pt.it.av.tnav.ml.optimization.gradienDescent;

import pt.it.av.tnav.utils.bla.Vector;

interface GradientDescent {
  Vector optimize(Vector iTheta, Gradient g);
}
