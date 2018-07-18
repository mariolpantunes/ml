package pt.it.av.tnav.ml.tm.dp;

import pt.it.av.tnav.utils.structures.Distance;
import pt.it.av.tnav.utils.structures.Similarity;
import pt.it.av.tnav.ml.tm.ngrams.NGram;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public interface DPPoint<P extends DPPoint> extends Distance<P>, Similarity<P> {
  NGram term();

  double affinity(final DPW dpw);
}
