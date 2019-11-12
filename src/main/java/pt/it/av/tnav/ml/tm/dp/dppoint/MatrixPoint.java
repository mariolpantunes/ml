package pt.it.av.tnav.ml.tm.dp.dppoint;

import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.bla.Matrix;

import java.util.List;

public class MatrixPoint implements DPPoint<MatrixPoint>{
  private final DPW dpw;
  private final Matrix matrix;
  private final List<String> map;
  private final int selfIdx;

  public MatrixPoint(final DPW dpw, final Matrix matrix, final List<String> map) {
    this.dpw = dpw;
    this.matrix = matrix;
    this.map = map;
    this.selfIdx = map.indexOf(dpw.term().toString());
  }

  @Override
  public double affinity(DPW dpw) {
    double rv = 1.0;
    if(!this.dpw.term().equals(dpw.term())) {
      int p2 = map.indexOf(dpw.term().toString());
      rv = matrix.get(selfIdx, p2);
    }
    return rv;
  }

  @Override
  public DPW dpw() {
    return dpw;
  }

  @Override
  public double distanceTo(MatrixPoint point) {
    return 1.0 - similarityTo(point);
  }

  @Override
  public double similarityTo(MatrixPoint point) {
    double rv = 1.0;
    if(!point.term().equals(dpw.term())) {
      int p2 = map.indexOf(point.term().toString());
      rv = matrix.get(selfIdx, p2);
    }
    return rv;
  }

  @Override
  public String toString() {
    return dpw.term().toString();
  }
}
