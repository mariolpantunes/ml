package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.utils.ArrayUtils;

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
    int cutoff = 0, lastCurve, curve = 0;

    do {
      lastCurve = curve;
      curve = dfdt(x, y, cutoff, y.length - cutoff);
      cutoff = (int) Math.ceil(curve / 2.0);
      //System.out.println("LastCurve = " + lastCurve + " Curve = " + curve + " Cutoff = " + cutoff + " Length = " + (y.length - cutoff));
    } while (lastCurve < curve && (y.length - cutoff) > Lmethod.MINCUTOFF);

    return curve;
  }

  public int dfdt(final double[] x, final double[] y, final int bIdx, final int len) {
    double m[] = ArrayUtils.cfd(x, y, bIdx, bIdx, len);
    double t = ArrayUtils.isoData(m);
    int idx = ArrayUtils.findClose(t, m);
    return idx + 1 + bIdx;
  }
}
