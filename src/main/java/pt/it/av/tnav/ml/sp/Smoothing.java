package pt.it.av.tnav.ml.sp;

import pt.it.av.tnav.utils.MathUtils;

public final class Smoothing {
  private Smoothing() {
  }

  public static void sma_last(final double[] values, final double[] times, final int width, final double out[]) {
    int left = 0, right = 0;
    double t_left_new, t_right_new, roll_area, left_area, right_area = 0;

    // Initialize output
    roll_area = left_area = values[0] * width;
    out[0] = values[0];

    for (int i = 1; i < values.length; i++) {
      // Remove truncated area on left and right end
      roll_area -= (left_area + right_area);

      // Expand interval on right end
      t_right_new = times[i] + (width / 2.0);
      while ((right < values.length - 1) && (times[right + 1] <= t_right_new)) {
        right++;
        roll_area += values[right - 1] * (times[right] - times[right - 1]);
      }

      // Shrink interval on left end
      t_left_new = times[i] - (width / 2.0);
      while (times[left] < t_left_new) {
        roll_area -= values[left] * (times[left + 1] - times[left]);
        left++;
      }

      // Add truncated area on left and right end
      left_area = values[Math.max(0, left - 1)] * (times[left] - t_left_new);
      right_area = values[right] * (t_right_new - times[right]);
      roll_area += left_area + right_area;

      // Save SMA value for current time window
      out[i] = roll_area / width;
    }
  }

  public static void sma_next(final double[] values, final double[] times, final int width, final double out[]) {
    int left = 0, right = 0;
    double t_left_new, t_right_new, roll_area, left_area, right_area = 0;

    // Initialize output
    roll_area = left_area = values[0] * width;
    out[0] = values[0];

    for (int i = 1; i < values.length; i++) {
      // Remove truncated area on left and right end
      roll_area -= (left_area + right_area);

      // Expand interval on right end
      t_right_new = times[i] + (width / 2.0);
      while ((right < values.length - 1) && (times[right + 1] <= t_right_new)) {
        right++;
        roll_area += values[right] * (times[right] - times[right - 1]);
      }

      // Shrink interval on left end
      t_left_new = times[i] - (width / 2.0);
      while (times[left] < t_left_new) {
        roll_area -= values[left + 1] * (times[left + 1] - times[left]);
        left++;
      }

      // Add truncated area on left and rigth end
      left_area = values[left] * (times[left] - t_left_new);
      right_area = values[right] * (t_right_new - times[right]);
      roll_area += left_area + right_area;

      // Save SMA value for current time window
      out[i] = roll_area / width;
    }
  }

  public static void sma_linear(final double[] values, final double[] times, final int width, final double out[]) {
    int left = 0, right = 0;
    double t_left_new, t_right_new, roll_area, left_area, right_area = 0;

    // Initialize output
    roll_area = left_area = values[0] * width;
    out[0] = values[0];

    for (int i = 1; i < values.length; i++) {
      // Remove truncated area on left and right end
      roll_area -= (left_area + right_area);

      // Expand interval on right end
      t_right_new = times[i] + (width / 2.0);
      while ((right < values.length - 1) && (times[right + 1] <= t_right_new)) {
        right++;
        roll_area += (values[right] + values[right - 1]) / 2 * (times[right] - times[right - 1]);
      }

      // Shrink interval on left end
      t_left_new = times[i] - (width / 2.0);
      while (times[left] < t_left_new) {
        roll_area -= (values[left] + values[left + 1]) / 2 * (times[left + 1] - times[left]);
        left++;
      }

      // Add truncated area on left and right end
      left_area = MathUtils.trapezoid_left(times[Math.max(0, left - 1)], t_left_new, times[left],
          values[Math.max(0, left - 1)], values[left]);
      right_area = MathUtils.trapezoid_right(times[right], t_right_new, times[Math.min(right + 1, values.length - 1)],
          values[right], values[Math.min(right + 1, values.length - 1)]);
      roll_area += left_area + right_area;

      // Save SMA value for current time window
      out[i] = roll_area / width;
    }
  }
}