package pt.it.av.tnav.ml.clustering.curvature;

import pt.it.av.tnav.utils.ArrayUtils;

import java.lang.ref.WeakReference;

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
  private static WeakReference<Curvature> wrc = null;

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

  /**
   *
   * @param x
   * @param y
   * @param bIdx
   * @param len
   * @return
   */
  public int dsdt(final double[] x, final double[] y, final int bIdx, final int len) {
    double m[] = ArrayUtils.csd(x, y, bIdx, bIdx, len);
    double t = ArrayUtils.isoData(m);
    int idx = ArrayUtils.findClose(t, m);
    return idx + 1 + bIdx;
  }

  /**
   * Builds a static {@link WeakReference} to a {@link Curvature} class.
   * <p>
   *   This method should be used whenever the {@link Curvature} will be built and destroy multiple times.
   *   It will also share a single stemmer through several process/threads.
   * </p>
   *
   * @return {@link Curvature} reference that points to a {@link DSDT}.
   */
  public synchronized static Curvature build() {
    Curvature rv = null;
    if (wrc == null) {
      rv = new DFDT();
      wrc = new WeakReference<>(rv);
    } else {
      rv = wrc.get();
      if(rv == null) {
        rv = new DFDT();
        wrc = new WeakReference<>(rv);
      }
    }
    return rv;
  }
}
