package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.ArrayUtils;
import pt.it.av.atnog.utils.PrintUtils;

import java.util.ArrayList;

public class AutoThres {
  private AutoThres() {}

  public static int knee (final double[] y) {
    double ys[] = new double[y.length];
    ArrayUtils.mm(y, ys, 1);
    double m[] = new double[ys.length - 1];
    for(int i = 0 ; i < ys.length - 1; i++) {
      m[i] = ys[i] - ys[i+1];
    }

    double t = ArrayUtils.isoData(m);

    //System.err.println("T = "+t);
    System.err.println(PrintUtils.array(m));

    int i = 0;
    for(; i < y.length && m[i] > t; i++);

    //System.err.println("I = "+i);

    return (i-1);
  }

  public static int knee (final double[] x, final double[] y) {
    double m[] = new double[y.length - 1];
    for(int i = 0 ; i < y.length - 1; i++) {
      m[i] = (y[i] - y[i+1]) / (x[i] - x[i+1]);
    }

    double t = ArrayUtils.isoData(m);

    int i = 0;
    for(; i < y.length && m[i] > t; i++);

    return (i-1);
  }
}
