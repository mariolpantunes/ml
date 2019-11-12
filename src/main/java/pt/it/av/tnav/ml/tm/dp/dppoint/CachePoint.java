package pt.it.av.tnav.ml.tm.dp.dppoint;

import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.structures.mutableNumber.MutableDouble;

import java.util.HashMap;

/**
 * CachePoint, semantic points used for clustering.
 * It is a wrapper used to speedup clustering.
 * When a distance metric is computed between two points,
 * the value is stored in a hash table preventing further calculations.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class CachePoint<P extends DPPoint<P>> implements DPPoint<CachePoint<P>> {
  private final P point;
  private final HashMap<NGram, MutableDouble> sims;
  private final MutableDouble zero = new MutableDouble();

  /**
   * Builds a CoOccPoint.
   *
   * @param point {@link DPPoint} associated with this wrapper.
   */
  public CachePoint(final P point) {
    this.point = point;
    sims = new HashMap<>();
  }

  @Override
  public DPW dpw() {
    return point.dpw();
  }

  @Override
  public double affinity(final DPW dpw) {
    return point.affinity(dpw);
  }

  /**
   * Returns the {@link DPPoint} associated with this wrapper.
   *
   * @return the {@link DPPoint} associated with this wrapper.
   */
  public P point() {
    return point;
  }

  @Override
  public double distanceTo(CachePoint<P> pointB) {
    double rv = 0.0;
    if(!pointB.term().equals(point.term())) {
      rv = Math.sqrt(1.0 - similarityTo(pointB));
    }
    return rv;
  }

  @Override
  public double similarityTo(CachePoint<P> pointB) {
    double rv;
    if(sims.containsKey(pointB.term()))
        rv =  sims.getOrDefault(point.term(), zero).doubleValue();
      else {
        rv = point.similarityTo(pointB.point);
        sims.put(pointB.term(), new MutableDouble(rv));
    }
    return rv;
  }

  @Override
  public String toString() {
    return point.toString();
  }
}
