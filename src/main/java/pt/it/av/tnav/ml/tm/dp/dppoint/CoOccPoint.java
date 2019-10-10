package pt.it.av.tnav.ml.tm.dp.dppoint;

import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;

/**
 * CoOccPoint, semantic points used for clustering.
 * This points uses co-occurrence as distance metric.
 * It uses the maximum value co-occurrence value between
 * all candidates to normalize the distance metric.
 *
 * @author Mário Antunes
 * @version 2.0
 */
public class CoOccPoint implements DPPoint<CoOccPoint> {
  private final DPW dpw;
  private final double max;

  /**
   * Builds a CoOccPoint.
   *
   * @param term {@link NGram} that this point will represent
   * @param coordinates the distributional profile of the above mentioned term.
   * @param max the maximum value co-occurrence value between all candidates.
   */
  public CoOccPoint(final DPW dpw, final double max) {
    this.dpw = dpw;
    this.max = max;
  }

  @Override
  public NGram term() {
    return dpw.term();
  }

  @Override
  public DPW dpw() {
    return dpw;
  }

  @Override
  public double affinity(final DPW dpw) {
    double rv = 1.0;
    if(this.dpw.term().equals(dpw.term())) {
      //rv = (coOcc(dpw.term()) +  dpw.dimention(this.dpw.term())) / (2.0*max);
      rv = Math.min(coOcc(dpw.term())/max, dpw.dimention(this.dpw.term())/max);
    }
    return rv;
  }

  /**
   * Returns the number of co-occurrences with the parameter term.
   *
   * @param term
   * @return the number of co-occurrences with the parameter term.
   */
  public double coOcc(NGram term) {
    return dpw.dimention(term);
  }

  @Override
  public double distanceTo(CoOccPoint point) {
    return 1.0 - similarityTo(point);
  }

  @Override
  public double similarityTo(CoOccPoint point) {
    double rv = 1.0;
    if(!point.term().equals(dpw.term())) {
      //rv = (coOcc(point.term()) +  point.coOcc(dpw.term())) / (2.0*max);
      rv = Math.min(coOcc(point.term())/max, point.coOcc(dpw.term()) / max);
    }
    return rv;
  }

  @Override
  public String toString() {
    return dpw.term().toString();
  }
}
