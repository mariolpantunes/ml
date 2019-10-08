package pt.it.av.tnav.ml.tm.dp.dpwOpt;

import pt.it.av.tnav.ml.clustering.curvature.Curvature;
import pt.it.av.tnav.ml.clustering.curvature.DFDT;
import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.bla.Vector;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Implements a Distributional Profile optimizer based on Elbow method.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWElbowOpt implements DPWOpt {
  private static WeakReference<DPWOpt> wro = null;
  private final int min;

  /**
   * @param min
   */
  public DPWElbowOpt(final int min) {
    this.min = min;
  }

  @Override
  public List<DPW.DpDimension> optimize(final NGram term,
                                        final NGram stemm,
                                        final List<DPW.DpDimension> dpDimensions) {
    if (dpDimensions.size() > min) {
      Comparator<DPW.DpDimension> c =
          (DPW.DpDimension a, DPW.DpDimension b) -> (Double.compare(b.value, a.value));
      Collections.sort(dpDimensions, c);
      double x[] = new double[dpDimensions.size()], y[] = new double[dpDimensions.size()];
      for(int i = 0; i < dpDimensions.size(); i++) {
        x[i] = i;
        y[i] = dpDimensions.get(i).value;
      }

      int idx = DFDT.build().elbow(x,y);
      if(idx >= 0) {
        dpDimensions.removeIf(p -> p.value < y[idx]);
      }
    }
    return dpDimensions;
  }

  /**
   * Builds a static {@link WeakReference} to a {@link DPWOpt} class.
   * <p>
   *   This method should be used whenever the {@link DPWOpt} will be built and destroy multiple times.
   *   It will also share a single stemmer through several process/threads.
   * </p>
   *
   * @return {@link DPWOpt} reference that points to a {@link DPWElbowOpt}.
   */
  public synchronized static DPWOpt build() {
    DPWOpt rv = null;
    if (wro == null) {
      rv = new DPWElbowOpt(DPWStatisticOpt.MD);
      wro = new WeakReference<>(rv);
    } else {
      rv = wro.get();
      if(rv == null) {
        rv = new DPWElbowOpt(DPWStatisticOpt.MD);
        wro = new WeakReference<>(rv);
      }
    }
    return rv;
  }
}
