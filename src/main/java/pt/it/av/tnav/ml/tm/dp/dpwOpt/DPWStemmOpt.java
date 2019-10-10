package pt.it.av.tnav.ml.tm.dp.dpwOpt;

import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWStemmOpt implements DPWOpt {
  private static WeakReference<DPWOpt> wro = null;

  @Override
  public List<DPW.DpDimension> optimize(final NGram term,
                                        final NGram stemm,
                                        final List<DPW.DpDimension> dpDimensions) {
    for (int i = 0; i < dpDimensions.size() - 1; i++) {
      DPW.DpDimension a = dpDimensions.get(i);
      for (int j = i + 1; j < dpDimensions.size(); j++) {
        DPW.DpDimension b = dpDimensions.get(j);
        if (a.stemm.equals(b.stemm)) {
          double total = a.value + b.value;
          if (a.stemm.equals(stemm)) {
            dpDimensions.set(i, new DPW.DpDimension(term, stemm, total));
          } else if (a.term.length() < b.term.length()) {
            dpDimensions.set(i, new DPW.DpDimension(a.term, a.stemm, total));
          } else {
            dpDimensions.set(i, new DPW.DpDimension(b.term, b.stemm, total));
          }
          a = dpDimensions.get(i);
          dpDimensions.remove(j);
        }
      }
    }
    return dpDimensions;
  }

  /**
   * Builds a static {@link WeakReference} to a {@link DPWOpt} class.
   * <p>
   * This method should be used whenever the {@link DPWOpt} will be built and destroy multiple times.
   * It will also share a single stemmer through several process/threads.
   * </p>
   *
   * @return {@link DPWOpt} reference that points to a {@link DPWStemmOpt}.
   */
  public synchronized static DPWOpt build() {
    DPWOpt rv = null;
    if (wro == null) {
      rv = new DPWStemmOpt();
      wro = new WeakReference<>(rv);
    } else {
      rv = wro.get();
      if (rv == null) {
        rv = new DPWStemmOpt();
        wro = new WeakReference<>(rv);
      }
    }
    return rv;
  }
}
