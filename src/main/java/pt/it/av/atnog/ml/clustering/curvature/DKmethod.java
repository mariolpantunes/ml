package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.utils.ArrayUtils;
import pt.it.av.atnog.utils.PrintUtils;

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
}
