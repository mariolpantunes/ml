package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.utils.ArrayUtils;

/**
 * Implements methods to detect knee and curvature points in error curves.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public abstract class BaseCurvature implements Curvature{
  @Override
  public final int knee(double[] x, double[] y) {
    int rv = -1;
    double slope = (y[y.length - 1] - y[0]) / (x[x.length - 1] - x[0]);

    if (slope != 0) {
      if (slope < 0) {
        double yr[] = ArrayUtils.reverse(y);
        rv = x.length - find_knee(x, yr) - 1;
      } else {
        rv = find_knee(x, y);
      }
    }

    return rv;
  }

  @Override
  public final int elbow(double[] x, double[] y) {
    int rv = -1;
    double slope = (y[y.length - 1] - y[0]) / (x[x.length - 1] - x[0]);

    if (slope != 0) {
      if (slope > 0) {
        double yr[] = ArrayUtils.reverse(y);
        rv = x.length - find_elbow(x, yr) - 1;
      } else {
        rv = find_elbow(x, y);
      }
    }

    return rv;
  }

  /**
   *
   * @param x
   * @param y
   * @return
   */
  public abstract int find_knee(double[] x, double[] y);

  /**
   *
   * @param x
   * @param y
   * @return
   */
  public abstract int find_elbow(double[] x, double[] y);
}
