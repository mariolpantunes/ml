package pt.it.av.tnav.ml.dreduction;

import pt.it.av.tnav.utils.bla.Matrix;
import pt.it.av.tnav.utils.structures.Similarity;
import pt.it.av.tnav.ml.tm.dp.DPW;

import java.util.List;

/**
 * Dimensionality reduction based on latent methods.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class Latent {
  private Latent() {
  }

  /**
   *
   * @param points
   * @param minLat
   * @param maxLat
   * @param reps
   * @param <E>
   * @return
   */
  public static <E extends Similarity<E>> Matrix nmf(final List<E> points, final int minLat, final int maxLat,
      final int reps) {
    Matrix sim = Matrix.identity(points.size());

    for (int i = 0; i < points.size() - 1; i++) {
      for (int j = i + 1; j < points.size(); j++) {
        double similarity = points.get(i).similarityTo(points.get(j));
        sim.set(i, j, similarity);
        sim.set(j, i, similarity);
      }
    }

    Matrix wh[] = sim.nmf((int) Math.round(points.size() / (double) minLat));
    Matrix nf = wh[0].mul(wh[1]), tnf = null;
    double bcost = sim.euclideanDistance(nf);
    for (int i = 1; i < reps; i++) {
      wh = sim.nmf((int) Math.round(points.size() / (double) minLat));
      tnf = wh[0].mul(wh[1]);
      double cost = sim.euclideanDistance(tnf);
      if (cost < bcost) {
        nf = tnf;
        bcost = cost;
      }
    }

    for (int d = minLat + 1; d <= maxLat && d < points.size(); d++) {
      for (int i = 0; i < reps; i++) {
        wh = sim.nmf((int) Math.round(points.size() / (double) d));
        tnf = wh[0].mul(wh[1]);
        double cost = sim.euclideanDistance(tnf);
        if (cost < bcost) {
          nf = tnf;
          bcost = cost;
        }
      }
    }

    return nf;
  }

  /**
   *
   * @param points
   * @param minLat
   * @param maxLat
   * @param reps
   * @param <E>
   * @return
   */
  public static Matrix nmf_dpw(final List<DPW> context) {
    Matrix V = Matrix.identity(context.size());

    for (int i = 0; i < context.size() - 1; i++) {
      for (int j = i + 1; j < context.size(); j++) {
        double similarity = context.get(i).dimention(context.get(j).term())
            + context.get(j).dimention(context.get(i).term());
        V.set(i, j, similarity);
        V.set(j, i, similarity);
      }
    }

    for (int i = 0; i < context.size(); i++) {
      V.set(i, i, context.get(i).dimention(context.get(i).term()));
    }

    V = V.uDiv(V.max());

    int k = Math.min(V.rows(), V.columns()) - 1;
    int bestK = -1;
    double bestPress = Double.MAX_VALUE;

    Matrix Mask = V.mask();

    for (int i = 1; i <= k; i++) {
      // System.err.println("K = "+k);
      double press = 0.0;

      for (int j = 0; j < Mask.size(); j++) {
        // System.err.println("Mask = "+j);
        if (Mask.get(j) == 1) {
          Mask.set(j, 0);
          // Several tries
          Matrix WH[] = V.nmf(i, Mask);
          Matrix wh = WH[0].mul(WH[1]);
          double cost = V.distanceTo(wh);
          for (int idx = 0; idx < 100; idx++) {
            // System.err.println("IDX = "+idx);
            Matrix tWH[] = V.nmf(i, Mask);
            wh = tWH[0].mul(tWH[1]);
            double tcost = V.distanceTo(wh);
            if (tcost < cost) {
              cost = tcost;
              WH = tWH;
            }
          }

          wh = WH[0].mul(WH[1]);

          press += Math.pow(V.get(j) - wh.get(j), 2.0);

          Mask.set(j, 1);
        }
      }

      if (bestPress > press) {
        bestPress = press;
        bestK = i;
      }
    }

    Matrix WH[] = V.nmf(bestK, Mask);
    Matrix wh = WH[0].mul(WH[1]);

    double cost = V.distanceTo(wh);

    for (int i = 0; i < 100; i++) {
      Matrix tWH[] = V.nmf(bestK, Mask);
      wh = tWH[0].mul(tWH[1]);
      double tcost = V.distanceTo(wh, Mask);
      if (tcost < cost && Double.isFinite(tcost)) {
        cost = tcost;
        WH = tWH;
      }
    }

    wh = WH[0].mul(WH[1]);
    for (int i = 0; i < context.size(); i++) {
      wh.set(i, i, 1.0);
    }
    return wh;
  }
}
