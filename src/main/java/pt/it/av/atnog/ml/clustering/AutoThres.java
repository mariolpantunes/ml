package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.ArrayUtils;

/**
 * Alternative method to detect knee and elbow points in performance curves.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 2.0
 */
public class AutoThres {
  /**
   * Private constructor.
   * This code is a static library.
   */
  private AutoThres() {}

  public static int knee (final double[] y) {
    boolean pSlope = ((y[y.length - 1] - y[0]) > 0) ? true : false;
    double ys[] = new double[y.length];
    ArrayUtils.mm(y, ys, 1);
    double m[] = new double[ys.length - 1];
    for(int i = 0; i < ys.length - 1; i++) {
      m[i] = Math.abs(ys[i] - ys[i + 1]);
    }

    double t = ArrayUtils.isoData(m);
    //System.err.println("T = "+t);
    //System.err.println(PrintUtils.array(m));

    double dist = Math.abs(m[0] - t);
    int idx = 0;
    for (int i = 1; i < m.length; i++) {
      if (Math.abs(m[i] - t) < dist) {
        idx = i;
        dist = Math.abs(m[i] - t);
      }
    }

    if (pSlope) {
      if (m[idx] > t) {
        idx = idx - 1;
      }
    } else {
      if (m[idx] > t) {
        idx = idx + 1;
      }
    }

    //for(; i < y.length && m[i] > t; i++);
    //System.err.println("I = "+i);
    //return (i-1);

    return idx;
  }

  public static int elbow(final double[] y) {
    boolean pSlope = ((y[y.length - 1] - y[0]) > 0) ? true : false;
    double ys[] = new double[y.length];
    ArrayUtils.mm(y, ys, 1);
    double m[] = new double[ys.length - 1];
    for (int i = 0; i < ys.length - 1; i++) {
      m[i] = Math.abs(ys[i] - ys[i + 1]);
    }

    double t = ArrayUtils.isoData(m);
    //System.err.println("T = "+t);
    //System.err.println(PrintUtils.array(m));

    double dist = Math.abs(m[0] - t);
    int idx = 0;
    for (int i = 1; i < m.length; i++) {
      if (Math.abs(m[i] - t) < dist) {
        idx = i;
        dist = Math.abs(m[i] - t);
      }
    }

    if (pSlope) {
      if (m[idx] < t) {
        idx = idx + 1;
      }
    } else {
      if (m[idx] < t) {
        idx = idx - 1;
      }
    }

    //for(; i < y.length && m[i] > t; i++);
    //System.err.println("I = "+i);
    //return (i-1);

    return idx;
  }

  public static int elbow(final double[] x, final double[] y) {
    boolean pSlope = ((y[y.length - 1] - y[0]) > 0) ? true : false;
    double xs[] = new double[x.length], ys[] = new double[y.length];
    ArrayUtils.mm(y, ys, 1);
    ArrayUtils.mm(x, xs, 1);
    double m[] = new double[y.length - 1];
    for(int i = 0 ; i < y.length - 1; i++) {
      m[i] = Math.abs((ys[i] - ys[i + 1]) / (xs[i] - xs[i + 1]));
    }

    double t = ArrayUtils.isoData(m);
    //System.err.println("T = "+t);
    //System.err.println(PrintUtils.array(ys));
    //System.err.println(PrintUtils.array(m));

    double dist = Math.abs(m[0] - t);
    int idx = 0;
    for (int i = 1; i < m.length; i++) {
      if (Math.abs(m[i] - t) < dist) {
        idx = i;
        dist = Math.abs(m[i] - t);
      }
    }

    if (pSlope) {
      if (m[idx] < t) {
        idx = idx + 1;
      }
    } else {
      if (m[idx] < t) {
        idx = idx - 1;
      }
    }

    //System.err.println("IDX = "+idx);
    return idx;
    //return ArrayUtils.max(m);
  }
}
