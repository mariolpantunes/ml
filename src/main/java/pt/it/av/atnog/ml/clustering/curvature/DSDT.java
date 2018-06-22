package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.utils.ArrayUtils;

/**
 * DSDT-method (Dynamic Second Derivative Threshold)
 * <p>
 *
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 2.0
 */
public class DSDT extends BaseCurvature {

  @Override
  public int find_knee(final double[] x, final double y[]) {
    return ((x.length > 2)?itRefinement(x, y):-1);
  }

  @Override
  public int find_elbow(final double[] x, final double[] y) {
    return ((x.length > 2)?itRefinement(x, y):-1);
  }

  private int itRefinement(final double x[], final double[] y) {
    int cutoff = 0, lastCurve, curve = 0;

    do {
      lastCurve = curve;
      curve = dsdt(x, y, cutoff, y.length - cutoff);
      cutoff = (int) Math.ceil(curve / 2.0);
      //System.out.println("LastCurve = " + lastCurve + " Curve = " + curve + " Cutoff = " + cutoff + " Length = " + (y.length - cutoff));
    } while (lastCurve < curve && (y.length - cutoff) > Lmethod.MINCUTOFF);

    return curve;
  }

  public int dsdt(final double[] x, final double[] y, final int bIdx, final int len) {
    double m[] = ArrayUtils.csd(x, y, bIdx, bIdx, len);
    double t = ArrayUtils.isoData(m);
    int idx = ArrayUtils.findClose(t, m);
    return idx + 1 + bIdx;
  }
}
