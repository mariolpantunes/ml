package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.utils.ArrayUtils;

/**
 * Alternative method to detect knee and curvature points in error curves.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 2.0
 */
public class DFDE implements Curvature {

  @Override
  public int knee(final double[] x, final double y[]) {
    double m[] = centralSecondDerivative(x, y);
    double t = ArrayUtils.isoData(m);
    //System.err.println("T = " + t);
    //System.err.println(PrintUtils.array(y));
    //System.err.println(PrintUtils.array(m));

    int idx = 0;
    double dist = Math.abs(m[0] - t);
    for (int i = 1; i < m.length; i++) {
      if (Math.abs(m[i] - t) < dist) {
        idx = i;
        dist = Math.abs(m[i] - t);
      }
    }

    return idx+1;
  }

  @Override
  public int elbow(final double[] x, final double[] y) {
    double m[] = slopes(x, y);
    double t = ArrayUtils.isoData(m);
    //System.err.println("T = "+t);
    //System.err.println(PrintUtils.array(y));
    //System.err.println(PrintUtils.array(m));

    double dist = Math.abs(m[0] - t);
    int idx = 0;
    for (int i = 1; i < m.length; i++) {
      if (Math.abs(m[i] - t) < dist) {
        idx = i;
        dist = Math.abs(m[i] - t);
      }
    }

    return idx+1;
  }

  private double[] slopes(final double[] x, final double[] y) {
    double rv[] = new double[y.length - 1];

    for (int i = 0; i < y.length - 1; i++) {
      rv[i] = (y[i+1] - y[i]) / (x[i+1] - x[i]);
    }

    return rv;
  }

  private double[] centralFirstDerivative(final double[] x, final double[] y) {
    double rv[] = new double[y.length - 2];

    for (int i = 1; i < y.length - 1; i++) {
      rv[i-1] = (y[i+1] - y[i-1]) / ((x[i]-x[i-1])+(x[i+1]-x[i]));
    }

    return rv;
  }

  private double[] centralSecondDerivative(final double[] x, final double[] y) {
    double rv[] = new double[y.length - 2];

    for (int i = 1; i < y.length - 1; i++) {
      rv[i-1] = (y[i+1] - 2*y[i] + y[i]) / ((x[i]-x[i-1])*(x[i+1]-x[i]));
    }

    return rv;
  }
}
