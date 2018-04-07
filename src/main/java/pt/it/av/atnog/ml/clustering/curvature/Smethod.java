package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.utils.ArrayUtils;

/**
 * S-method to detect knee/elbow points.
 * <p>
 *   Slips method.
 *   It expands the idea of the L-method, but considers three parts instead of two.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 2.0
 */
public class Smethod extends BaseCurvature {

  @Override
  public int find_knee(double[] x, double[] y) {
    return sMethod(x, y, x.length)[0];
  }

  @Override
  public int find_elbow(double[] x, double[] y) {
    return sMethod(x, y, x.length)[1];
  }

  /**
   *
   * @param x
   * @param y
   * @return
   */
  private int[] sMethod(final double x[], final double[] y, final int length) {
    int p[] = {1, 2};
    double rmse = Double.POSITIVE_INFINITY;

    for(int i = 1; i < length - 3; i++) {
      for (int j = i + 1; j < length - 2; j++) {
        double lrl[] = ArrayUtils.lr(x, y, 0, 0, i + 1),
            lrc[] = ArrayUtils.lr(x, y, i, i, j - (i + 1)),
            lrr[] = ArrayUtils.lr(x, y, j, j, length - (j + 1));

        double crmse = rmse(x, y, lrl, lrc, lrr, i, j, length);
        if (crmse < rmse) {
          p[0] = i;
          p[1] = j;
          rmse = crmse;
        }
      }
    }

    return p;
  }

  /**
   *
   * @param x
   * @param y
   * @param lrl
   * @param lrc
   * @param lrr
   * @param p1
   * @param p2
   * @return
   */
  private double rmse(final double x[], final double[] y, final double lrl[],
                      final double lrc[], final double lrr[],
                      final int p1, final int p2, final int length) {
    double msel = 0.0, msec=0.0, mser = 0.0;

    for(int i = 0; i < p1+1; i++) {
      msel += Math.pow(y[i]-(lrl[0]*x[i]+lrl[1]) ,2.0);
    }

    for (int i = p1; i < p2 + 1; i++) {
      msec += Math.pow(y[i]-(lrc[0]*x[i]+lrc[1]) ,2.0);
    }

    for (int i = p2; i < length; i++) {
      mser += Math.pow(y[i]-(lrr[0]*x[i]+lrr[1]) ,2.0);
    }

    return (p1 * Math.sqrt(msel) + (p2-p1) * Math.sqrt(msec) +(length-p2) * Math.sqrt(mser))/length;
  }
}
