package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.utils.ArrayUtils;
import pt.it.av.atnog.utils.PrintUtils;

/**
 * DFDT-method (Dynamic First Derivative Threshold)
 * <p>
 *
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 2.0
 */
public class DFDT extends BaseCurvature {

  @Override
  public int find_knee(final double[] x, final double y[]) {
    return itRefinement(x, y);
  }

  @Override
  public int find_elbow(final double[] x, final double[] y) {
    return itRefinement(x, y);
  }

  private int itRefinement(final double x[], final double[] y) {
    int cutoff = 0, lastCurve = x.length, curve = x.length;

    do {
      lastCurve = curve;
      curve = dfdt(x, y, cutoff, y.length - cutoff);
      cutoff = curve / 2;
    } while (curve != lastCurve);

    return curve;
  }

  public int dfdt(final double[] x, final double[] y, final int bIdx, final int len) {
    double m[] = ArrayUtils.cfd(x, y, bIdx, bIdx, len);
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

    return idx + 1 + bIdx;
  }
}
