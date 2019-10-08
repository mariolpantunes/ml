package pt.it.av.tnav.ml.tm.dp.dppoint;

import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.utils.structures.Distance;
import pt.it.av.tnav.utils.structures.Similarity;
import pt.it.av.tnav.ml.tm.ngrams.NGram;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public interface DPPoint<P extends DPPoint<?>> extends Distance<P>, Similarity<P> {
  /**
   *
   * @return
   */
  NGram term();

  /**
   *
   * @param dpw
   * @return
   */
  double affinity(final DPW dpw);

  /**
   * Returns the {@link DPW} associated with the point.
   *
   * @return the {@link DPW} associated with the point.
   */
  DPW dpw();
}
