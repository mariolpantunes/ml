package pt.it.av.tnav.ml.optimization.gradienDescent;

import pt.it.av.tnav.utils.bla.Vector;

/**
 *
 */
public abstract class BatchGradienDescent implements GradientDescent{
  protected static final double GRADIENT_NORM_THRESHOLD = 1e-4;

  protected abstract Vector theta(Vector delta);

  protected abstract void setup();

  public Vector optimize(Vector iTheta, Gradient g) {
    /*Vector theta = iTheta;
    setup();
    double gradNorm = 0.0;
    do {
      Vector delta = g.delta(theta);
      theta = theta(delta);
      gradNorm = g.delta(theta).norm(2);
    } while (gradNorm > GRADIENT_NORM_THRESHOLD);
    return theta;*/
    return null;
  }
}
