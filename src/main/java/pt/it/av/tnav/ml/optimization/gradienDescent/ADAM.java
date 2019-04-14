package pt.it.av.tnav.ml.optimization.gradienDescent;

import pt.it.av.tnav.utils.ArrayUtils;
import pt.it.av.tnav.utils.MathUtils;
import pt.it.av.tnav.utils.PrintUtils;
import pt.it.av.tnav.utils.bla.Vector;

public class ADAM {
  private double b1 = 0.9, b2 = 0.999, alpha = 1e-4, eps = MathUtils.eps();
  private final int MAX_ITERACTION = 1000;

  public ADAM() { }

  public double[] optimize(double[] theta, Gradient g) {
    double[] m = new double[theta.length], v = new double[theta.length];
    double[] mH = new double[theta.length], vH = new double[theta.length];
    double[] gradient = g.gradient(theta);
    double gradNorm = ArrayUtils.norm(gradient, 0, theta.length, 2);

    //System.err.println("Gradient = "+PrintUtils.array(gradient));

    for(int i = 1; gradNorm > 0.001 && i < MAX_ITERACTION; i++) {
      //System.err.println("Gradient Norm = "+gradNorm);
      for(int j = 0; j < gradient.length; j++) {
        m[j] = b1*m[j] + (1.0-b1)*gradient[j];
        v[j] = b2*v[j] + (1.0-b2)*gradient[j]*gradient[j];
        mH[j] = m[j] / (1.0-(Math.pow(1.0 - b1, i)));
        vH[j] = v[j] / (1.0-(Math.pow(1.0 - b2, i)));
        theta[j] = theta[j] - alpha * mH[j] / (Math.sqrt(vH[j]) + eps);
      }
      gradient = g.gradient(theta);
      //System.err.println("Gradient = "+PrintUtils.array(gradient));
      gradNorm = ArrayUtils.norm(gradient, 0, theta.length, 2);
    }

    //System.err.println("Gradient Norm = "+gradNorm);

    return theta;
  }
}
