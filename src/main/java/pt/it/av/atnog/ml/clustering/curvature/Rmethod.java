package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.utils.ArrayUtils;
import pt.it.av.atnog.utils.PrintUtils;

/**
 * R-method to detect knee/elbow points.
 * <p>
 * Regression method.
 * It uses a logarithm and power regression to fit the curve and detect knee and elbow points
 * respectively.
 * The root of the curvature (Kf(x)) is computed and used to find the maxium curvature point,
 * which represent the knee/elbow point.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 2.0
 */
public class Rmethod extends BaseCurvature {

  @Override
  public int find_knee(double[] x, double[] y) {
    int rv = -1;
    double lnr[] = ArrayUtils.lnr(x, y);
    double a = lnr[0], r2 = lnr[2];

    if (r2 >= 0.8) {
      double rx = a / Math.sqrt(2.0);
      rv = ArrayUtils.findClose(rx, x);
    }

    return rv;
  }

  @Override
  public int find_elbow(double[] x, double[] y) {
    int rv = -1;
    System.err.println(PrintUtils.array(x));
    System.err.println(PrintUtils.array(y));
    double pr[] = ArrayUtils.pr(x, y);
    double er[] = ArrayUtils.er(x, y);

    System.out.println("\t\t X=" + PrintUtils.array(x));
    System.out.println("\t\t Y=" + PrintUtils.array(y));

    double er2 = er[2], pr2 = pr[2];
    System.out.println("\t\t Power R2       = " + pr2);
    System.out.println("\t\t Exponencial R2 = " + er2);

    if (pr2 >= 0.8 && pr2 > er2) {
      double a = pr[0], b = pr[1];
      double u = a * b, v = u * (b - 1), w = v * (b - 2);
      //double z = w * u * u - 3 * v * v * u;
      double z = u * (w * u - 3 * v * v);

      double rx = Math.pow((-w / z), (1.0 / (2 * b - 2)));
      //System.out.println("FLN(" + rx + ") = " + fpr(rx, k, z, b));
      rv = ArrayUtils.findClose(rx, x);
    } else if (er2 > 0.8) {
      double a = er[0], b = er[1];
      System.out.println("\t\tf(x) = " + a + "e^x" + b);
      double rx = (-Math.log(2 * Math.pow(a, 2.0) * Math.pow(b, 2))) / (2 * b);
      System.out.println("\t\t RX = " + rx);
      rv = ArrayUtils.findClose(rx, x);
    }

    return rv;
  }
}
