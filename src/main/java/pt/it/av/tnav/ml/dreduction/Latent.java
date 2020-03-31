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
  public static Matrix nmf_dpw(final List<DPW> context, final int minLat, final int maxLat, final int reps) {
    Matrix sim = Matrix.identity(context.size());

    for (int i = 0; i < context.size() - 1; i++) {
      for (int j = i + 1; j < context.size(); j++) {
        double similarity = context.get(i).dimention(context.get(j).term())
            + context.get(j).dimention(context.get(i).term());
        sim.set(i, j, similarity);
        sim.set(j, i, similarity);
      }
    }

    for (int i = 0; i < context.size(); i++) {
      sim.set(i, i, context.get(i).dimention(context.get(i).term()));
    }

    Matrix wh[] = sim.nmf((int) Math.round(context.size() / (double) minLat));
    Matrix nf = wh[0].mul(wh[1]), tnf = null;
    double bcost = sim.euclideanDistance(nf);
    for (int i = 1; i < reps; i++) {
      wh = sim.nmf((int) Math.round(context.size() / (double) minLat));
      tnf = wh[0].mul(wh[1]);
      double cost = sim.euclideanDistance(tnf);
      if (cost < bcost) {
        nf = tnf;
        bcost = cost;
      }
    }

    for (int d = minLat + 1; d <= maxLat && d < context.size(); d++) {
      for (int i = 0; i < reps; i++) {
        wh = sim.nmf((int) Math.round(context.size() / (double) d));
        tnf = wh[0].mul(wh[1]);
        double cost = sim.euclideanDistance(tnf);
        if (cost < bcost) {
          nf = tnf;
          bcost = cost;
        }
      }
    }

    nf.uDiv(nf.max());
    for (int i = 0; i < context.size(); i++) {
      nf.set(i, i, 1.0);
    }

    return nf;
  }
}
