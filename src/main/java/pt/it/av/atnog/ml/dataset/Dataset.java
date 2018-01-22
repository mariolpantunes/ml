package pt.it.av.atnog.ml.dataset;

import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.structures.Point4D;

import java.util.List;

/**
 *
 * @param <DP>
 */
public interface Dataset<DP extends Vector> {
  /**
   * Returns a {@link List} of {@link Vector} with the data from the dataset.
   *
   * @return a {@link List} of {@link Vector} with the data from the dataset.
   */
  List<DP> load();
}
