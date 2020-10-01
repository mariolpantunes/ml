package pt.it.av.tnav.ml.clustering.curvature;

import pt.it.av.tnav.utils.ArrayUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Detect knee points in a curve using the "Kneedle" algorithm as described in the paper
 * "Finding a Kneedle in a Haystack: Detecting Knee Points in System Behavior".
 * </p>
 * Kneedle algorithm described in:
 * Satopaa, V., Albrecht, J., Irwin, D., & Raghavan, B. (2011, June).
 * Finding a" Kneedle" in a Haystack: Detecting Knee Points in System Behavior.
 * In 2011 31st International Conference on Distributed Computing Systems Workshops (pp. 166-171). IEEE.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Kneedle extends BaseCurvature{
  private static WeakReference<Curvature> wrc = null;

  /**
   * Find a single knee (the best one) in a batch manner.
   * Since it work on a batch manner it becomes simpler than the conventional algorithm.
   *
   * @param x
   * @param y
   * @return
   */
  public int find_knee(final double[] x, final double[] y) {
    double xn[] = new double[x.length],
        ys[] = new double[y.length], yn[] = new double[y.length];
    ArrayUtils.rescaling(x, xn);
    //Smoothing.sma_linear(y, x, 1, ys);
    ArrayUtils.rescaling(ys, yn);

    double[] yDiff = new double[y.length];
    for (int i = 0; i < y.length; i++) {
      yDiff[i] = yn[i] - xn[i];
    }

    return ArrayUtils.max(yDiff);
  }

  /**
   * Find a single curvature (the best one) in a batch manner.
   * Since it work on a batch manner it becomes simpler than the conventional algorithm.
   *
   * @param x
   * @param y
   * @return
   */
  public int find_elbow(final double[] x, final double[] y) {
    // Check the slope of the curvature
    double m = (y[y.length-1] - y[0]) / (x[x.length-1] - x[0]);
    int rv = 0;

    if(m >= 0) {
      rv = elbow_positive(x, y);
    } else {
      rv = elbow_negative(x, y);
    }

    return rv;
  }

  /**
   *
   * @param x
   * @param y
   * @return
   */
  private int elbow_positive(final double[] x, final double[] y) {
    double xn[] = new double[x.length],
        ys[] = new double[y.length], yn[] = new double[y.length];
    ArrayUtils.rescaling(x, xn);
    //Smoothing.sma_linear(y, x, 1, ys);
    ArrayUtils.rescaling(ys, yn);

    double[] yDiff = new double[y.length];
    for (int i = 0; i < y.length; i++) {
      yDiff[i] = yn[i] - xn[i];
    }

    return ArrayUtils.min(yDiff);
  }

  /**
   *
   * @param x
   * @param y
   * @return
   */
  private int elbow_negative(final double[] x, final double[] y) {
    double xn[] = new double[x.length],
        ys[] = new double[y.length], yn[] = new double[y.length];
    ArrayUtils.rescaling(x, xn);
    //Smoothing.sma_linear(y, x, 1, ys);
    ArrayUtils.rescaling(ys, yn);

    double[] yDiff = new double[y.length];
    for (int i = 0; i < y.length; i++) {
      yDiff[i] = yn[i] - (1.0 - xn[i]);
    }

    return ArrayUtils.min(yDiff);
  }

  /**
   * TODO: fix this
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

    double xn[] = new double[x.length], yn[] = new double[y.length];
    ArrayUtils.rescaling(y, yn);
    ArrayUtils.rescaling(x, xn);

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

  /**
   * Builds a static {@link WeakReference} to a {@link Curvature} class.
   * <p>
   *   This method should be used whenever the {@link Curvature} will be built and destroy multiple times.
   *   It will also share a single stemmer through several process/threads.
   * </p>
   *
   * @return {@link Curvature} reference that points to a {@link Kneedle}.
   */
  public synchronized static Curvature build() {
    Curvature rv = null;
    if (wrc == null) {
      rv = new DFDT();
      wrc = new WeakReference<>(rv);
    } else {
      rv = wrc.get();
      if(rv == null) {
        rv = new DFDT();
        wrc = new WeakReference<>(rv);
      }
    }
    return rv;
  }
}
