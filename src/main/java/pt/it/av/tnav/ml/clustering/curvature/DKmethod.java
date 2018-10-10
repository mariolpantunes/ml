package pt.it.av.tnav.ml.clustering.curvature;

import pt.it.av.tnav.utils.ArrayUtils;
import pt.it.av.tnav.utils.PrintUtils;

import java.lang.ref.WeakReference;

/**
 * DK-method to detect knee/elbow points.
 * <p>
 *   Discrete Curvature (Kf(x)) Method.
 *   Computes the values of curvature analitically.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 2.0
 */
public class DKmethod extends BaseCurvature{
  private static WeakReference<Curvature> wrc = null;

  @Override
  public int find_knee(final double[] x, final double[] y) {
    return dkMethod(x, y);
  }

  @Override
  public int find_elbow(final double[] x, final double[] y) {
    return dkMethod(x, y);
  }

  private int dkMethod(final double[] x, final double[] y) {
    double fd[] = ArrayUtils.cfd(x,y), sd[] = ArrayUtils.csd(x,y);

    int rv = 0;
    double max = sd[0]/(Math.pow(1+Math.pow(fd[0],2),3/2));

    for(int i = 1; i < sd.length; i++) {
      double current = Math.abs(sd[i]/(Math.pow(1+Math.pow(fd[i],2),3/2)));
      if(current > max) {
        max = current;
        rv = i;
      }
    }

    return rv;
  }

  /**
   * Builds a static {@link WeakReference} to a {@link Curvature} class.
   * <p>
   *   This method should be used whenever the {@link Curvature} will be built and destroy multiple times.
   *   It will also share a single stemmer through several process/threads.
   * </p>
   *
   * @return {@link Curvature} reference that points to a {@link DKmethod}.
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
