package pt.it.av.atnog.ml.regression;

/**
 * Univariate Regression.
 * <p>
 *
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class UnivariateRegression {
  /**
   * Utility class, lets make the constructor private.
   */
  private UnivariateRegression() {
  }

  /**
   * Logarithmic regression.
   * <p>
   * It computes the logarithmic equation (\(y = a\lnb{x} + b\) that minimizes the following cost:
   * \(Q=\sum_{i=0}^{n}(y_i-(a \times u_i  + b))^2\)
   * Where \(v = ln(x)\).
   * </p>
   *
   * @param x
   * @param y
   * @return
   */
  public static LNR lnr(final double x[], final double y[]) {
    return lnr(x, y, 0, 0, x.length);
  }

  /**
   * Logarithmic regression.
   * <p>
   * It computes the logarithmic equation (\(y = a\lnb{x} + b\) that minimizes the following cost:
   * \(Q=\sum_{i=0}^{n}(y_i-(a \times u_i  + b))^2\)
   * Where \(v = ln(x)\).
   * </p>
   *
   * @param x
   * @param y
   * @param bX
   * @param bY
   * @param l
   * @return
   */
  public static LNR lnr(final double x[], final double y[], final int bX, final int bY, final int l) {
    double sx = 0.0, sy = 0.0, xy = 0.0, x2 = 0.0;

    for (int i = 0; i < l; i++) {
      double u = Math.log(x[i + bY]);
      sx += u;
      sy += y[i + bY];
      xy += u * y[i + bY];
      x2 += Math.pow(u, 2);
    }

    double d = l * x2 - Math.pow(sx, 2.0),
        m = (l * xy - sx * sy) / d,
        b = (sy * x2 - sx * xy) / d,
        my = sy / l, sse = 0.0, sst = 0.0;

    for (int i = 0; i < l; i++) {
      double u = Math.log(x[i + bY]), f = m * u + b;
      sse += Math.pow(y[i + bY] - f, 2);
      sst += Math.pow(y[i + bY] - my, 2);
    }

    return new LNR(m,b,1 - (sse / sst));
  }

  /**
   * Exponential regression.
   * <p>
   * It computes the exponential equation (\(y = a \times e^{xb}\) that minimizes the following cost:
   * \(Q=\sum_{i=0}^{n}(v_i-(b \times x_i + k))^2\)
   * Where \(v = ln(y)\) and \(k = ln(a)\).
   * </p>
   *
   * @param x
   * @param y
   * @return
   */
  public static ER er(final double x[], final double y[]) {
    return er(x, y, 0, 0, x.length);
  }

  /**
   * Exponential regression.
   * <p>
   * It computes the exponential equation (\(y = a \times e^{xb}\) that minimizes the following cost:
   * \(Q=\sum_{i=0}^{n}(v_i-(b \times x_i + k))^2\)
   * Where \(v = ln(y)\) and \(k = ln(a)\).
   * </p>
   *
   * @param x
   * @param y
   * @param bX
   * @param bY
   * @param l
   * @return
   */
  public static ER er(final double x[], final double y[], final int bX, final int bY, final int l) {
    double sx = 0.0, sy = 0.0, xy = 0.0, x2 = 0.0;

    for (int i = 0; i < l; i++) {
      double v = Math.log(y[i + bY]);
      sx += x[i + bX];
      sy += v;
      xy += x[i + bX] * v;
      x2 += Math.pow(x[i + bX], 2);
    }

    double d = l * x2 - Math.pow(sx, 2.0),
        m = (l * xy - sx * sy) / d,
        b = (sy * x2 - sx * xy) / d,
        my = sy / l, sse = 0.0, sst = 0.0;

    for (int i = 0; i < l; i++) {
      double v = Math.log(y[i + bY]), f = m * x[i + bX] + b;
      sse += Math.pow(v - f, 2);
      sst += Math.pow(v - my, 2);
    }

    return new ER(Math.exp(b), m, 1 - (sse / sst));
  }

  /**
   * Power regression.
   * <p>
   * It computes the power equation (\(y = a \times x^b\) that minimizes the following cost:
   * \(Q=\sum_{i=0}^{n}(v_i-(b \times u_i + k))^2\)
   * Where \(v = ln(y)\), \(u = ln(x)\) and \(k = ln(a)\).
   * </p>
   *
   * @param x
   * @param y
   * @return
   */
  public static PR pr(final double x[], final double y[]) {
    return pr(x, y, 0, 0, x.length);
  }

  /**
   * Power regression.
   * <p>
   *   It computes the power equation (\(y = a \times x^b\) that minimizes the following cost:
   *   \(Q=\sum_{i=0}^{n}(v_i-(b \times u_i + k))^2\)
   *   Where \(v = ln(y)\), \(u = ln(x)\) and \(k = ln(a)\).
   * </p>
   *
   * @param x
   * @param y
   * @param bX
   * @param bY
   * @param l
   * @return
   */
  public static PR pr(final double x[], final double y[], final int bX, final int bY, final int l) {
    double sx = 0.0, sy = 0.0, xy = 0.0, x2 = 0.0;

    for (int i = 0; i < l; i++) {
      double u = Math.log(x[i + bX]),
          v = Math.log(y[i + bY]);
      //System.out.print("("+u+"; "+v+") ");
      sx += u;
      sy += v;
      xy += u * v;
      x2 += Math.pow(u, 2);
    }
    //System.out.println();
    //System.out.println(sx+" "+sy+" "+xy+" "+x2);

    double d = l * x2 - Math.pow(sx, 2.0),
        m = (l * xy - sx * sy) / d,
        b = (sy * x2 - sx * xy) / d,
        my = sy / l, sse = 0.0, sst = 0.0;

    for (int i = 0; i < l; i++) {
      double u = Math.log(x[i + bX]),
          v = Math.log(y[i + bY]),
          f = m * u + b;
      sse += Math.pow(v - f, 2);
      sst += Math.pow(v - my, 2);
    }

    return new PR(Math.exp(b), m, 1 - (sse / sst));
  }

  /**
   * Linear regression.
   * <p>
   *   It computes the linear equation (\(y = x \times m + b\)) that minimizes the following cost:
   *   \(Q=\sum_{i=0}^{n}(y_i-(x_i \times m + b))^2\)
   * </p>
   *
   * @param x
   * @param y
   * @param bX
   * @param bY
   * @param l
   * @return
   */
  public static LR lr(final double x[], final double y[]) {
    return lr(x,y,0,0,x.length);
  }

  /**
   * Linear regression.
   * <p>
   *   It computes the linear equation (\(y = x \times m + b\)) that minimizes the following cost:
   *   \(Q=\sum_{i=0}^{n}(y_i-(x_i \times m + b))^2\)
   * </p>
   *
   * @param x
   * @param y
   * @param bX
   * @param bY
   * @param l
   * @return
   */
  public static LR lr(final double x[], final double y[], final int bX, final int bY, final int l) {
    double sx = 0.0, sy = 0.0, xy = 0.0, x2 = 0.0;

    for (int i = 0; i < l; i++) {
      sx += x[i + bX];
      x2 += Math.pow(x[i + bX], 2);
      xy += x[i + bX] * y[i + bY];
      sy += y[i + bY];
    }

    double d = l * x2 - Math.pow(sx, 2.0),
        m = (l * xy - sx * sy) / d,
        b = (sy * x2 - sx * xy) / d,
        my = sy / l, sse = 0.0, sst = 0.0;

    for (int i = 0; i < l; i++) {
      double f = m * x[i + bX] + b;
      sse += Math.pow(y[i + bY] - f, 2);
      sst += Math.pow(y[i + bY] - my, 2);
    }

    return new LR(m, b, 1 - (sse / sst));
  }

  /**
   *
   */
  public interface URM {
    /**
     *
     * @param x
     * @return
     */
    double solve(final double x);

    /**
     *
     * @return
     */
    double a();

    /**
     *
     * @return
     */
    double b();

    /**
     *
     * @return
     */
    double r2();
  }

  /**
   *
   */
  public static abstract class BRM implements URM {
    protected final double a, b, r2;

    public BRM(final double a, final double b, final double r2) {
      this.a = a;
      this.b = b;
      this.r2 = r2;
    }

    @Override
    public abstract double solve(final double x);

    @Override
    public double a() {
      return a;
    }

    @Override
    public double b() {
      return b;
    }

    @Override
    public double r2() {
      return r2;
    }

    @Override
    public boolean equals(Object o) {
      // self check
      if (this == o)
        return true;
      // null check
      if (o == null)
        return false;
      // type check and cast
      if (getClass() != o.getClass())
        return false;
      LNR lnr = (LNR) o;
      // field comparison
      return a == lnr.a && b == lnr.b;
    }

    @Override
    public int hashCode() {
      int rv = 1;
      rv = (31 * rv) + Double.hashCode(a);
      rv = (31 * rv) + Double.hashCode(b);
      rv = (31 * rv) + Double.hashCode(r2);
      return rv;
    }

  }

  /**
   *
   */
  public static class LNR extends BRM{

    public LNR(final double a, final double b, final double r2) {
      super(a,b,r2);
    }

    @Override
    public double solve(final double x) {
      return a*Math.log(x)+b;
    }

    @Override
    public String toString() {
      return "f(x) = "+a+"ln(x)+"+b+" ("+r2+")";
    }
  }

  /**
   *
   */
  public static class ER extends BRM{

    public ER(final double a, final double b, final double r2) {
      super(a, b, r2);
    }

    @Override
    public double solve(final double x) {
      return a*Math.exp(x*b);
    }

    @Override
    public String toString() {
      return "f(x) = "+a+"e^x+"+b+" ("+r2+")";
    }
  }

  /**
   *
   */
  public static class PR extends BRM{

    public PR(final double a, final double b, final double r2) {
      super(a,b,r2);
    }

    @Override
    public double solve(final double x) {
      return a*Math.pow(x, b);
    }

    @Override
    public String toString() {
      return "f(x) = "+a+"x^"+b+" ("+r2+")";
    }
  }

  /**
   *
   */
  public static class LR extends BRM {

    public LR(final double a, final double b, final double r2) {
      super(a, b, r2);
    }

    @Override
    public double solve(final double x) {
      return a*x+b;
    }

    @Override
    public String toString() {
      return "f(x) = "+a+"x+"+b+" ("+r2+")";
    }
  }
}
