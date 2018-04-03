package pt.it.av.atnog.ml.clustering.curvature;

/**
 * Menger Curvature method to detect knee/elbow points.
 * <p>
 *
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 2.0
 */
public class MengerCurvature extends BaseCurvature {
  @Override
  public int find_knee(final double[] x, final double[] y) {
    int idx = 1;
    double dk = dc(x, y, idx);

    for(int i = 2; i < x.length - 1; i++) {
      double cdk = dc(x, y, i);
      if(cdk < dk) {
        idx = i;
        dk = cdk;
      }
    }

    return idx;
  }

  @Override
  public int find_elbow(final double[] x, final double[] y) {
    int idx = 1;
    double dk = dc(x, y, idx);

    for(int i = 2; i < x.length - 1; i++) {
      double cdk = dc(x, y, i);
      if(cdk > dk) {
        idx = i;
        dk = cdk;
      }
    }

    return idx;
  }

  /**
   *
   * @param x
   * @param y
   * @param i
   * @return
   */
  private double dc(final double[] x, final double[] y, final int i) {
    double pq = Math.sqrt(Math.pow(x[i-1] - x[i], 2.0) + Math.pow(y[i-1] - y[i], 2.0)),
    qr = Math.sqrt(Math.pow(x[i] - x[i+1], 2.0) + Math.pow(y[i] - y[i+1], 2.0)),
    rp = Math.sqrt(Math.pow(x[i-1] - x[i+1], 2.0) + Math.pow(y[i-1] - y[i+1], 2.0));

    double A = 4.0 * Math.pow(pq, 2.0) * Math.pow(qr, 2.0),
        B = Math.pow(pq, 2.0) + Math.pow(qr, 2.0) - Math.pow(rp, 2.0);

    return Math.sqrt(A-Math.pow(B,2.0))/(pq*qr*rp);
  }
}
