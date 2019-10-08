package pt.it.av.tnav.ml.clustering.curvature;

import java.lang.ref.WeakReference;

/**
 * Implements methods to detect knee and curvature points in error curves.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public interface Curvature {
  /**
   *
   * @param x
   * @param y
   * @return
   */
  int knee(final double[] x, final double[] y);

  /**
   *
   * @param x
   * @param y
   * @return
   */
  int elbow(final double[] x,final double[] y);
}
