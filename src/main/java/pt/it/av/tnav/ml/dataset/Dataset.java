package pt.it.av.tnav.ml.dataset;

import pt.it.av.tnav.utils.structures.Distance;

import java.util.List;

/**
 *
 * @param <T>
 */
public interface Dataset<T extends Distance<T>> {
  /**
   * Returns a {@link List} of {@link Vector} with the data from the dataset.
   *
   * @return a {@link List} of {@link Vector} with the data from the dataset.
   */
  List<T> load();

  /**
   * @return
   */
  int classes();
}
