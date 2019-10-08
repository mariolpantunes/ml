package pt.it.av.tnav.ml.tm.dp.dpwOpt;

import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.PrintUtils;
import pt.it.av.tnav.utils.bla.Matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a Distributional Profile optimizer based on latent variables.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWNMFOpt implements DPWOpt {
  private final Matrix nf;
  private final List<String> map;

  /**
   *
   * @param nf
   * @param map
   */
  public DPWNMFOpt(final Matrix nf, final List<String> map) {
    this.nf = nf;
    this.map = map;
  }

  @Override
  public List<DPW.DpDimension> optimize(final NGram term,
                                        final NGram stemm,
                                        final List<DPW.DpDimension> dpDimensions) {
    int i = map.indexOf(term.toString());
    List<DPW.DpDimension> dimensions = new ArrayList<>(dpDimensions.size());

    for(DPW.DpDimension d : dpDimensions) {
      int j = map.indexOf(d.term.toString());
      if(j < 0) {
        System.out.println("Term "+d.term);
        System.out.println("Map: "+PrintUtils.list(map));
      }
      dimensions.add(new DPW.DpDimension(d.term, d.stemm, nf.get(i, j)));
    }

    return dimensions;
  }
}
