package pt.it.av.tnav.ml.clustering.curvature;

import pt.it.av.tnav.ml.sp.Smoothing;
import pt.it.av.tnav.ml.sp.RDP;
import pt.it.av.tnav.utils.MathUtils;
import pt.it.av.tnav.utils.csv.CSV;
import pt.it.av.tnav.utils.structures.point.Point2D;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Detect knee points in a curve using the "Kneedle" algorithm as described in
 * the paper "Finding a Kneedle in a Haystack: Detecting Knee Points in System
 * Behavior".
 * </p>
 * Kneedle algorithm described in: Satopaa, V., Albrecht, J., Irwin, D., &
 * Raghavan, B. (2011, June). Finding a" Kneedle" in a Haystack: Detecting Knee
 * Points in System Behavior. In 2011 31st International Conference on
 * Distributed Computing Systems Workshops (pp. 166-171). IEEE.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class Kneedle {

  private static int[] findMinMax(final List<Point2D> values) {
    int minX = 0, maxX = 0, minY = 0, maxY = 0;

    for (int i = 1; i < values.size(); i++) {
      Point2D p = values.get(i);
      // search for X
      if (p.x() < values.get(minX).x()) {
        minX = i;
      } else if (p.x() > values.get(maxX).x()) {
        maxX = i;
      }
      // search for Y
      if (p.y() < values.get(minY).y()) {
        minY = i;
      } else if (p.y() > values.get(maxY).y()) {
        maxY = i;
      }
    }

    return new int[] { minX, maxX, minY, maxY };
  }

  private static List<Point2D> normalize(final List<Point2D> values) {
    List<Point2D> rv = new ArrayList<>();

    // Find max min for each coordinate
    int idx[] = findMinMax(values);
    double minX = values.get(idx[0]).x(), maxX = values.get(idx[1]).x(), minY = values.get(idx[2]).y(),
        maxY = values.get(idx[3]).y();

    // Normalize the points
    for (Point2D p : values) {
      double x = (p.x() - minX) / (maxX - minX), y = (p.y() - minY) / (maxY - minY);
      rv.add(new Point2D(x, y));
    }

    return rv;
  }

  private static List<Point2D> differences(final List<Point2D> values, final Direction cd, final Concavity c) {
    List<Point2D> rv = new ArrayList<>();

    for (Point2D p : values) {
      double x = p.x(), y = 0;

      if (cd == Direction.Decreasing) {
        y = p.x() + p.y();
        if (c == Concavity.Counterclockwise) {
          y = 1.0 - y;
        }
      } else {
        y = p.y() - p.x();
        if (c == Concavity.Counterclockwise) {
          y = Math.abs(y);
        }
      }
      rv.add(new Point2D(x, y));
    }

    return rv;
  }

  public static int[] knee(final List<Point2D> values) {
    return knee(values, 1.0, Direction.Decreasing, Concavity.Counterclockwise);
  }

  public static int[] knee(final List<Point2D> values, final double sensitivity) {
    return knee(values, sensitivity, Direction.Decreasing, Concavity.Counterclockwise);
  }

  public static int[] knee(final List<Point2D> values, final double sensitivity, final Direction cd,
      final Concavity c) {
    // int knees[] = new int[];
    // checkConstraints(x, y);

    // Smoothing
    List<Point2D> Ds = Smoothing.sma_linear(values, 3);

    // TODO: remove
    try {
      CSV csv = new CSV();
      csv.addLines(Ds);
      final String csvFile = "/home/mantunes/multiknee/plot_smooth.csv";
      FileWriter w = new FileWriter(csvFile);
      csv.write(w);
      w.flush();
      w.close();
    } catch (Exception e) {

    }

    // Normalization
    List<Point2D> Dsn = normalize(Ds);

    // TODO: remove
    try {
      CSV csv = new CSV();
      csv.addLines(Dsn);
      final String csvFile = "/home/mantunes/multiknee/plot_normalization.csv";
      FileWriter w = new FileWriter(csvFile);
      csv.write(w);
      w.flush();
      w.close();
    } catch (Exception e) {

    }

    // TODO: test RDP here
    List<Point2D> Drdp = RDP.rdp(Dsn);
    double space_saving = MathUtils.round((1.0 - (Drdp.size() / (double) Dsn.size())) * 100.0, 2);
    System.out.println("Number of data points after RDP: " + Drdp.size() + " (" + space_saving + "%)");

    // write CSV with reduction data
    try {
      CSV csv = new CSV();
      csv.addLines(Drdp);
      FileWriter w = new FileWriter("/home/mantunes/multiknee/plot_rdp.csv");
      csv.write(w);
      w.flush();
      w.close();
    } catch (Exception e) {

    }

    // Compute the differences
    List<Point2D> Dd = differences(Drdp, cd, c);

    // TODO: remove
    try {
      CSV csv = new CSV();
      csv.addLines(Dd);
      final String csvFile = "/home/mantunes/multiknee/plot_diff.csv";
      FileWriter w = new FileWriter(csvFile);
      csv.write(w);
      w.flush();
      w.close();
    } catch (Exception e) {

    }

    // Find local maxima
    List<Integer> kneeIndices = new ArrayList<Integer>();
    List<Integer> lmxIndices = new ArrayList<Integer>();
    List<Double> lmxThresholds = new ArrayList<Double>();

    boolean detectKneeForLastLmx = false;
    for (int i = 1; i < Dd.size() - 1; i++) {
      double y0 = Dd.get(i - 1).y(), y = Dd.get(i).y(), y1 = Dd.get(i + 1).y();

      // check if the difference values of a point are bigger
      // than for its left and right neighbour => local maximum
      if (y > y0 && y > y1) {

        // local maximum found
        lmxIndices.add(i);

        // compute the threshold value for this local maximum
        // NOTE: As stated in the paper the threshold Tlmx is computed. Since the mean
        // distance of all consecutive
        // x-values summed together for a normalized function is always (1 / (n -1)) we
        // do not have to compute the
        // whole sum here as stated in the paper.
        double tlmx = Dd.get(i).y() - sensitivity / (Dd.size() - 1);
        lmxThresholds.add(tlmx);

        // try to find out if the current local maximum is a knee point
        detectKneeForLastLmx = true;
      }

      // check for new knee point
      if (detectKneeForLastLmx) {
        if (Dd.get(i + 1).y() < lmxThresholds.get(lmxThresholds.size() - 1)) {
          // knee detected
          kneeIndices.add(lmxIndices.get(lmxIndices.size() - 1));
          detectKneeForLastLmx = false;
        }
      }
    }

    System.err.println("Knee Indices: " + kneeIndices.size());

    int knees[] = new int[kneeIndices.size()];
    for (int i = 0; i < kneeIndices.size(); i++) {
      knees[i] = kneeIndices.get(i);
    }

    return knees;
  }

  enum Direction {
    Increasing, Decreasing
  }

  enum Concavity {
    /* tangent goes anti-clockwise */
    Counterclockwise,
    /* tangent goes clockwise */
    Clockwise
  }
}
