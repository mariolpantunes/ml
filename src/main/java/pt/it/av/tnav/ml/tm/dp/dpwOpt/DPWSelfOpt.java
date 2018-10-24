package pt.it.av.tnav.ml.tm.dp.dpwOpt;

import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;

import java.util.List;

/**
 * Add self dimention to the profile.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWSelfOpt implements DPWOpt {
  @Override
  public List<DPW.DpDimension> optimize(final NGram term,
                                        final NGram stemm,
                                        final List<DPW.DpDimension> dpDimensions) {
    boolean exists = false;
    double max = 0;
    for(DPW.DpDimension d : dpDimensions) {
      if(d.term.equals(term)) {
        exists = true;
      }
      if(max < d.value) {
        max = d.value;
      }
    }

    if(!exists) {
      dpDimensions.add(new DPW.DpDimension(term, stemm, max));
    }

    return dpDimensions;
  }
}
