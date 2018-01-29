package pt.it.av.atnog.ml.clustering.curvature;

import pt.it.av.atnog.utils.ArrayUtils;

public class Lmethod implements Curvature {

  @Override
  public int knee(final double x[], final double[] y) {
    //return lMethod(x, y, x.length);
    return itRefinement(x, y);
  }

  @Override
  public int elbow(final double x[], final double[] y) {
    //return lMethod(x, y, x.length);
    return itRefinement(x, y);
  }

  /**
   *
   * @param x
   * @param y
   * @return
   */
  private int itRefinement(final double x[], final double[] y) {
    int cutoff =  x.length, lastCurve =  x.length, curve =  x.length;

    do {
      lastCurve = curve;
      curve = lMethod(x, y, cutoff);
      cutoff = curve * 2;
    } while(curve>= lastCurve);

    return curve;
  }

  /**
   *
   * @param x
   * @param y
   * @return
   */
  private int lMethod(final double x[], final double[] y, final int length) {
    int idx = 1;
    double lrl[] = ArrayUtils.lr(x, y,0,0,idx+1),
    lrr[] = ArrayUtils.lr(x, y, idx+1, idx+1, length - (idx+1));
    double rmse = rmse(x, y, lrl, lrr, idx, length);

    for(int i = 2; i < length-2; i++) {
      lrl = ArrayUtils.lr(x, y,0,0,i+1);
      lrr = ArrayUtils.lr(x, y,i+1, i+1, length - (i+1));

      double crmse = rmse(x, y, lrl, lrr, i, length);
      if(crmse < rmse) {
        idx = i;
        rmse = crmse;
      }
    }

    return idx;
  }

  /**
   *
   * @param x
   * @param y
   * @param lrl
   * @param lrr
   * @param idx
   * @return
   */
  private double rmse(final double x[], final double[] y, final double lrl[], final double lrr[],
                      final int idx, final int length) {
    double msel = 0.0, mser = 0.0;

    for(int i = 0; i < idx+1; i++) {
      msel += Math.pow(y[i]-(lrl[0]*x[i]+lrl[1]) ,2.0);
    }

    for(int i = idx+1; i < length; i++) {
      mser += Math.pow(y[i]-(lrr[0]*x[i]+lrr[1]) ,2.0);
    }

    return (idx * Math.sqrt(msel) + (length-idx) * Math.sqrt(mser))/length;
  }
}
