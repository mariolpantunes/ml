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
   * Returns a {@link NGram} with the term represented by the {@link DPPoint}.
   * 
   * @return a {@link NGram} with the term represented by the {@link DPPoint}
   */
  default NGram term() {
    return dpw().term();
  }

  /**
   * Return a {@link NGram} with the stem represented by the {@link DPPoint}.
   * 
   * @return a {@link NGram} with the stem represented by the {@link DPPoint}
   */
  default NGram stem() {
    return dpw().stem();
  }

  /**
   * Returns the strengh of association's value between the {@link DPPoint} and a
   * specific {@link DPW} profile.
   * 
   * @return the strengh of association's value between the {@link DPPoint} and a
   *         specific {@link DPW} profile
   */
  double value(final DPW dpw);

  /**
   * Return the affinity (bias) between the {@link DPPoint} and a specific
   * {@link DPW} profile.
   * 
   * @param dpw a {@link DPW} profile
   * @return the affinity (bias) between the {@link DPPoint} and a specific
   *         {@link DPW} profile
   */
  double affinity(final DPW dpw);

  /**
   * Returns the {@link DPW} associated with this {@link DPPoint}.
   *
   * @return the {@link DPW} associated with this {@link DPPoint}.
   */
  DPW dpw();
}
