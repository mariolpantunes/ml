package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.ArrayUtils;
import pt.it.av.atnog.utils.structures.mutableNumber.MutableInteger;
import pt.it.av.atnog.utils.structures.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Detect knee points in a curve using the "Kneedle" algorithm as described in the paper "Finding a" Kneedle" in a
 * Haystack: Detecting Knee Points in System Behavior".
 * <p>
 * NOTE: This implementation does not check the concavity of the curve.
 * Also no smoothing of the curve is applied by this method.
 * <p>
 * Kneedle algorithm described in:
 * Satopaa, V., Albrecht, J., Irwin, D., & Raghavan, B. (2011, June).
 * Finding a" Kneedle" in a Haystack: Detecting Knee Points in System Behavior.
 * In 2011 31st International Conference on Distributed Computing Systems Workshops (pp. 166-171). IEEE.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Kneedle {
  /**
   *
   */
  private Kneedle() {
  }

  /**
   * @param x
   * @param y
   * @return
   */
  public static int[] knee(final double[] x, final double[] y, final double sensitivity) {
    //int knees[] = new int[];
    checkConstraints(x, y);

    List<Integer> kneeIndices = new ArrayList<Integer>();
    List<Integer> lmxIndices = new ArrayList<Integer>();
    List<Double> lmxThresholds = new ArrayList<Double>();

    double[] xn = normalize(x);
    double[] yn = normalize(y);

    double[] yDiff = new double[y.length];
    for (int i = 0; i < y.length; i++) {
      yDiff[i] = yn[i] - xn[i];
    }

    boolean detectKneeForLastLmx = false;
    for (int i = 1; i < y.length - 1; i++) {

      // check if the difference values of a point are bigger
      // than for its left and right neighbour => local maximum
      if (yDiff[i] > yDiff[i - 1] && yDiff[i] > yDiff[i + 1]) {

        // local maximum found
        lmxIndices.add(i);

        // compute the threshold value for this local maximum
        // NOTE: As stated in the paper the threshold Tlmx is computed. Since the mean distance of all consecutive
        // x-values summed together for a normalized function is always (1 / (n -1)) we do not have to compute the
        // whole sum here as stated in the paper.
        double tlmx = yDiff[i] - sensitivity / (xn.length - 1);
        lmxThresholds.add(tlmx);

        // try to find out if the current local maximum is a knee point
        detectKneeForLastLmx = true;
      }

      // check for new knee point
      if (detectKneeForLastLmx) {
        if (yDiff[i + 1] < lmxThresholds.get(lmxThresholds.size() - 1)) {

          // knee detected
          kneeIndices.add(lmxIndices.get(lmxIndices.size() - 1));
          detectKneeForLastLmx = false;
        }
      }
    }

    int knees[] = new int[kneeIndices.size()];
    for (int i = 0; i < kneeIndices.size(); i++) {
      knees[i] = kneeIndices.get(i);
    }

    return knees;
  }

  /**
   * @param values
   * @return
   */
  private static double[] normalize(final double[] values) {
    double normalized[] = new double[values.length];
    Pair<MutableInteger, MutableInteger> minMax = ArrayUtils.minMax(values);
    double min = values[minMax.a.intValue()], max = values[minMax.b.intValue()];
    for (int i = 0; i < values.length; i++) {
      normalized[i] = (values[i] - min) / (max - min);
    }
    return normalized;
  }

  /**
   * @param x
   * @param y
   */
  private static void checkConstraints(final double[] x, final double[] y) {

    if (x.length != y.length || x.length < 2) {
      throw new IllegalArgumentException("x and y arrays must have size > 1 and the same number of elements");
    }

    for (int i = 0; i < x.length - 1; i++) {
      if (x[i + 1] <= x[i]) {
        throw new IllegalArgumentException("x values must be sorted and increasing");
      }
    }
  }
}
