package pt.it.av.tnav.ml.tm.dp.dpwOpt;

import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.bla.Vector;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Implements a Distributional Profile optimizer based on Elbow method.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWElbowOptmization implements DPWOpt {
  private final int min;

  //TODO: Added kneedle algorithm in where...

  /**
   * @param min
   */
  public DPWElbowOptmization(final int min) {
    this.min = min;
  }

  @Override
  public List<DPW.DpDimension> optimize(final NGram term, final List<DPW.DpDimension> dpDimensions) {
    if (dpDimensions.size() > min) {
      Comparator<DPW.DpDimension> c = (DPW.DpDimension a, DPW.DpDimension b) -> (Double.compare(b.value, a.value));
      Collections.sort(dpDimensions, c);
      Vector v = new Vector(dpDimensions.size());
      int i = 0;
      for (DPW.DpDimension p : dpDimensions)
        v.set(i++, p.value);
      double t = 0; //v.curvature();
      dpDimensions.removeIf(p -> p.value < t);
    }
    return dpDimensions;
  }
}
