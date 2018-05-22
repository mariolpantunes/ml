package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.ml.regression.UnivariateRegression;
import pt.it.av.atnog.utils.ArrayUtils;

/**
 * S-method to detect knee/elbow points.
 * <p>
 *   Slide method.
 *   It expands the idea of the L-method, but considers three parts instead of two.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 2.0
 */
public class Smethod extends BaseCurvature {

  @Override
  public int find_knee(double[] x, double[] y) {
    int cutoff = x.length, lastCurve, curve = x.length;

    do {
      lastCurve = curve;
      curve = sMethod(x, y, cutoff)[0];
      cutoff = curve * 2;
      //System.out.println("LastCurve = "+lastCurve+" Curve = "+curve+" Cutoff = "+cutoff);
    } while (lastCurve > curve && cutoff >= Lmethod.MINCUTOFF);

    return curve;
  }

  @Override
  public int find_elbow(double[] x, double[] y) {
    int cutoff = x.length, lastCurve, curve = x.length;

    do {
      lastCurve = curve;
      curve = sMethod(x, y, cutoff)[1];
      cutoff = curve * 2;
      //System.out.println("LastCurve = "+lastCurve+" Curve = "+curve+" Cutoff = "+cutoff);
    } while (lastCurve > curve && cutoff >= Lmethod.MINCUTOFF);

    return curve;
  }

  /**
   *
   * @param x
   * @param y
   * @return
   */
  private int[] sMethod(final double x[], final double[] y, final int length) {
    int p[] = {1, 2};
    double smetric = Double.POSITIVE_INFINITY;

    for(int i = 1; i < length - 3; i++) {
      UnivariateRegression.LR lrl = UnivariateRegression.lr(x, y, 0, 0, i + 1);
      for (int j = i + 1; j < length - 2; j++) {
        UnivariateRegression.LR lrm = UnivariateRegression.lr(x, y, i, i, j - (i + 1)),
            lrr = UnivariateRegression.lr(x, y, j, j, length - (j + 1));

        double crmse = sMetric(x, y, lrl, lrm, lrr, i, j, length);
        if (crmse < smetric) {
          p[0] = i;
          p[1] = j;
          smetric = crmse;
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
  private double sMetric(final double x[], final double[] y, final UnivariateRegression.LR lrl,
                         final UnivariateRegression.LR lrm, final UnivariateRegression.LR lrr,
                      final int p1, final int p2, final int length) {
    double rmsel = Lmethod.rmse(x,y,lrl,0,p1+1),
        rmsem = Lmethod.rmse(x,y,lrm,p1,p2+1),
        rmser = Lmethod.rmse(x,y,lrr,p2,length);

    return (p1 * rmsel + (p2-p1) * rmsem +(length-p2) * rmser)/length;
  }
}
