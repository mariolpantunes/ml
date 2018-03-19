package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.utils.ArrayUtils;

/**
 * Alternative method to detect knee and curvature points in error curves.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 2.0
 */
public class Rmethod extends BaseCurvature {

  @Override
  public int find_knee(double[] x, double[] y) {
    double lnr[] = ArrayUtils.lnr(x, y);
    double f[] = new double[y.length];

    //System.out.println("f(x)="+lnr[0]+"ln(x)+"+lnr[1]);

    for (int i = 0; i < x.length; i++) {
      f[i] = lnr[0] * Math.log(x[i]) + lnr[1];
    }

    double r2 = ArrayUtils.r2(y, f);

    //System.out.println("R2="+r2);
    double a = lnr[0];
    //System.out.println("A=" + a);

    double rx = x[0];

    // Newtom method
    /*System.out.println("F("+rx+") = "+flnr(rx,a));
    int c = 0;
    while (!MathUtils.equals(flnr(rx, a), 0.0, 0.01)) {
      rx = rx - (flnr(rx, a) / f1lnr(rx, a));
      c++;
    }
    System.out.println("F("+rx+") = "+flnr(rx,a)+" ("+c+")");*/

    // Ln alternative close formula
    rx = a / Math.sqrt(2.0);
    //System.out.println("FLN(" + rx + ") = " + flnr(rx, a));

    return ArrayUtils.findCloseSorted(rx, x);
  }

  private double flnr(double rx, double a) {
    return 2 * a * Math.pow(rx, -3) + -1.0 * Math.pow(a, 3) * Math.pow(rx, -5);
  }

  private double f1lnr(double rx, double a) {
    return -6.0 * a * Math.pow(rx, -4) + 5.0 * Math.pow(a, 3) * Math.pow(rx, -6);
  }

  @Override
  public int find_elbow(double[] x, double[] y) {
    double pr[] = ArrayUtils.pr(x, y);
    double f[] = new double[y.length];

    //System.out.println("f(x)="+pr[0]+"x^"+pr[1]);

    for (int i = 0; i < x.length; i++) {
      f[i] = pr[0] * Math.pow(x[i], pr[1]);
    }

    double r2 = ArrayUtils.r2(y, f);
    //System.out.println("R2="+r2);

    double a = pr[0], b = pr[1];
    double u = a * b, v = u * (b - 1), k = v * (b - 2);
    double z = k * u * u - 3 * v * v * u;

    //System.out.println("A=" + a);
    //System.out.println("B=" + b);
    //System.out.println("U=" + u);
    //System.out.println("V=" + v);
    //System.out.println("K=" + k);
    //System.out.println("Z=" + z);

    double rx = x[0];

    // Newton Method
    /*System.out.println("F("+rx+") = "+fpr(rx,k,z,b));
    int c = 0;
    while (!MathUtils.equals(fpr(rx, k, z, b), 0.0, 0.001)) {
      rx = rx - (fpr(rx, k, z, b) / f1pr(rx, k, z, b));
      c++;
    }
    System.out.println("F("+rx+") = "+fpr(rx,k,z,b)+" ("+c+")");*/

    // Ln alternative
    rx = Math.pow((k / -z), (1.0 / (2 * b - 2)));
    //System.out.println("FLN(" + rx + ") = " + fpr(rx, k, z, b));

    return ArrayUtils.findCloseSorted(rx, x);
  }

  private double fpr(double x, double k, double z, double b) {
    return k * Math.pow(x, b - 3) + z * Math.pow(x, 3 * b - 5);
  }

  private double f1pr(double x, double k, double z, double b) {
    return (k * b - 3 * k) * Math.pow(x, b - 4) + (3 * z * b - 5 * z) * Math.pow(x, 3 * b - 6);
  }
}
