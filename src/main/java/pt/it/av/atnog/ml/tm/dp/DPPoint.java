package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.utils.structures.Distance;
import pt.it.av.atnog.utils.structures.Similarity;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public interface DPPoint<P extends DPPoint> extends Distance<P>, Similarity<P> {
  NGram term();

  double affinity(final DPW dpw);
}
