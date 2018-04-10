package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.utils.ArrayUtils;

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

    //System.out.println("f(x)="+lnr[0]+"ln(x)+"+lnr[1]);
    //System.out.println("R2="+r2);
    double a = lnr[0], r2 = lnr[2];
    //System.out.println("A=" + a);

    if (r2 >= 0.8) {

      // Newtom method
    /*System.out.println("F("+rx+") = "+flnr(rx,a));
    double rx = x[0];
    int c = 0;
    while (!MathUtils.equals(flnr(rx, a), 0.0, 0.01)) {
      rx = rx - (flnr(rx, a) / f1lnr(rx, a));
      c++;
    }
    System.out.println("F("+rx+") = "+flnr(rx,a)+" ("+c+")");*/

      // Ln alternative close formula
      double rx = a / Math.sqrt(2.0);
      rv = ArrayUtils.findClose(rx, x);
      //System.out.println("FLN(" + rx + ") = " + flnr(rx, a));
    }

    return rv;
  }

  private double flnr(double rx, double a) {
    return 2 * a * Math.pow(rx, -3) + -1.0 * Math.pow(a, 3) * Math.pow(rx, -5);
  }

  private double f1lnr(double rx, double a) {
    return -6.0 * a * Math.pow(rx, -4) + 5.0 * Math.pow(a, 3) * Math.pow(rx, -6);
  }

  @Override
  public int find_elbow(double[] x, double[] y) {
    int rv = -1;
    double pr[] = ArrayUtils.pr(x, y);

    double a = pr[0], b = pr[1], r2 = pr[2];
    System.out.println("\t\tR2 = " + r2);
    if (r2 >= 0.8) {
      double u = a * b, v = u * (b - 1), w = v * (b - 2);
      //double z = w * u * u - 3 * v * v * u;
      double z = u * (w * u - 3 * v * v);

      //System.out.println("A=" + a);
      //System.out.println("B=" + b);
      //System.out.println("U=" + u);
      //System.out.println("V=" + v);
      //System.out.println("K=" + k);
      //System.out.println("Z=" + z);

      // Newton Method
      //System.out.println("F("+rx+") = "+fpr(rx,k,z,b));
    /*double rx = x[0];
    int c = 0;
    while (!MathUtils.equals(fpr(rx, k, z, b), 0.0, 0.1)) {
      rx = rx - (fpr(rx, k, z, b) / f1pr(rx, k, z, b));
      c++;
    }*/
      //System.out.println("F("+rx+") = "+fpr(rx,k,z,b)+" ("+c+")");*/

      // Ln alternative
      double rx = Math.pow((-w / z), (1.0 / (2 * b - 2)));
      //System.out.println("FLN(" + rx + ") = " + fpr(rx, k, z, b));
      rv = ArrayUtils.findClose(rx, x);
    }

    return rv;
  }

  private double fpr(double x, double k, double z, double b) {
    return k * Math.pow(x, b - 3) + z * Math.pow(x, 3 * b - 5);
  }

  private double f1pr(double x, double k, double z, double b) {
    return (k * b - 3 * k) * Math.pow(x, b - 4) + (3 * z * b - 5 * z) * Math.pow(x, 3 * b - 6);
  }
}
