package pt.it.av.atnog.ml.clustering.curvature;

/**
 * A-method to detect knee/elbow points.
 * <p>
 *   Angle based method. Based on the definition of function curvature.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Amethod extends BaseCurvature {
  @Override
  public int find_knee(double[] x, double[] y) {
    return 0;
  }

  @Override
  public int find_elbow(double[] x, double[] y) {
    return 0;
  }
}
