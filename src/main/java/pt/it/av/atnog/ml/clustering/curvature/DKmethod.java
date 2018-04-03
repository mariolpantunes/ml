package pt.it.av.atnog.ml.clustering.curvature;

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
  @Override
  public int find_knee(double[] x, double[] y) {
    return 0;
  }

  @Override
  public int find_elbow(double[] x, double[] y) {
    return 0;
  }
}
