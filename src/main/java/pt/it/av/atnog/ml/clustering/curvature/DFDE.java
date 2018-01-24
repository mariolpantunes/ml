package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.utils.ArrayUtils;

/**
 * Alternative method to detect knee and curvature points in performance curves.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 2.0
 */
public class DFDE implements Curvature {

  @Override
  public int knee (final double[] x, final double y[]) {
    return dfde(x,y);
  }

  @Override
  public int elbow(final double[] x, final double[] y) {
    //boolean pSlope = ((y[y.length - 1] - y[0]) > 0) ? true : false;
    return dfde(x,y);
  }

  private int dfde(final double[] x, final double[] y) {
    double m[] = new double[y.length - 1];
    for(int i = 0 ; i < y.length - 1; i++) {
      m[i] = Math.abs((y[i] - y[i + 1]) / (x[i] - x[i + 1]));
    }

    double t = ArrayUtils.isoData(m);
    //System.err.println("T = "+t);
    //System.err.println(PrintUtils.array(ys));
    //System.err.println(PrintUtils.array(m));

    double dist = Math.abs(m[0] - t);
    int idx = 0;
    for (int i = 1; i < m.length; i++) {
      if (Math.abs(m[i] - t) < dist) {
        idx = i;
        dist = Math.abs(m[i] - t);
      }
    }

    return idx;
  }
}
