package pt.it.av.tnav.ml.tm.dp.dpwOpt;

import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Add self dimention to the profile.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWSelfOpt implements DPWOpt {
  private static WeakReference<DPWOpt> wro = null;

  @Override
  public List<DPW.DpDimension> optimize(final NGram term,
                                        final NGram stemm,
                                        final List<DPW.DpDimension> dpDimensions) {
    boolean exists = false;
    double max = 0;
    for(DPW.DpDimension d : dpDimensions) {
      if(d.term.equals(term)) {
        exists = true;
      }
      if(max < d.value) {
        max = d.value;
      }
    }

    if(!exists) {
      dpDimensions.add(new DPW.DpDimension(term, stemm, max));
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
   * @return {@link DPWOpt} reference that points to a {@link DPWSelfOpt}.
   */
  public synchronized static DPWOpt build() {
    DPWOpt rv = null;
    if (wro == null) {
      rv = new DPWSelfOpt();
      wro = new WeakReference<>(rv);
    } else {
      rv = wro.get();
      if(rv == null) {
        rv = new DPWSelfOpt();
        wro = new WeakReference<>(rv);
      }
    }
    return rv;
  }
}
