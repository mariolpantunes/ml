package pt.it.av.tnav.ml.clustering.curvature;

import java.lang.ref.WeakReference;

/**
 * A-method to detect knee/elbow points.
 * <p>
 *   Angle based method.
 *   Based on the definition of function curvature.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Amethod extends BaseCurvature {
  private static WeakReference<Curvature> wrc = null;

  @Override
  public int find_knee(double[] x, double[] y) {
    return 0;
  }

  @Override
  public int find_elbow(double[] x, double[] y) {
    return 0;
  }

  /**
   * Builds a static {@link WeakReference} to a {@link Curvature} class.
   * <p>
   *   This method should be used whenever the {@link Curvature} will be built and destroy multiple times.
   *   It will also share a single stemmer through several process/threads.
   * </p>
   *
   * @return {@link Curvature} reference that points to a {@link Amethod}.
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
