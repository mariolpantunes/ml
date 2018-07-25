package pt.it.av.tnav.ml.tm.dp.dpwOpt;

import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.bla.Matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DPWNMFOpt implements DPWOpt {
  private final Matrix nf;
  private final List<String> map;

  public DPWNMFOpt(final Matrix nf, List<String> map) {
    this.nf = nf;
    this.map = map;
  }

  @Override
  public List<DPW.DpDimension> optimize(final NGram term, final List<DPW.DpDimension> dpDimensions) {
    int i = Collections.binarySearch(map, term.toString());
    List<DPW.DpDimension> dimensions = new ArrayList<>(dpDimensions.size());

    for(DPW.DpDimension d : dpDimensions) {
      int j = Collections.binarySearch(map, d.term.toString());
      dimensions.add(new DPW.DpDimension(d.term, d.stemm, nf.get(i,j)));
    }

    return dimensions;
  }
}
