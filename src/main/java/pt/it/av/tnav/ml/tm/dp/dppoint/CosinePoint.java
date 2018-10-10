package pt.it.av.tnav.ml.tm.dp.dppoint;

import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;

import java.util.List;

/**
 * CosinePoint, semantic points used for clustering.
 * This points uses cosine as distance metric.
 *
 * @author Mário Antunes
 * @version 2.0
 */
public class CosinePoint implements DPPoint<CosinePoint> {
  private final DPW dpw;

  /**
   * Builds a CosinePoint.
   *
   * @param dpw the {@link DPW} associated with the point.
   */
  public CosinePoint(final DPW dpw) {
    this.dpw = dpw;
  }

  /**
   * Builds a CosinePoint.
   *
   * @param term {@link NGram} that this point will represent.
   * @param coordinates the distributional profile of the above mentioned term.
   */
  public CosinePoint(final NGram term, List<DPW.DpDimension> coordinates) {
    dpw = new DPW(term, coordinates);
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
    return this.dpw.similarityTo(dpw);
  }

  @Override
  public double distanceTo(CosinePoint point) {
    double rv = 0.0;
    if(!point.term().equals(term())) {
      //rv = 1.0 - similarityTo(point);
      rv = Math.sqrt(1.0 - similarityTo(point));
      //rv = - Math.log(similarityTo(point)+Double.MIN_VALUE);
      //rv = 1.0/(similarityTo(point) - 1.0);
      //rv = 2.0*Math.acos(similarityTo(point))/Math.PI;
    }
    return rv;
  }

  @Override
  public double similarityTo(CosinePoint point) {
    double rv = 1.0;
    if(!point.term().equals(term())) {
      rv = dpw.similarityTo(point.dpw);
    }
    return rv;
  }

  @Override
  public String toString() {
    return dpw.term().toString();
  }
}
