package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.structures.Similarity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Distributional Profile of a Word ({@link NGram}).
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPW implements Similarity<DPW> {
  private final NGram term;
  private List<DpDimension> dpDimensions;

  /**
   * {@link DPW} constructor.
   *
   * @param term {@link NGram} represented by the profile
   * @param dpDimensions {@link List} of the {@link DpDimension}
   */
  public DPW(final NGram term, List<DpDimension> dpDimensions) {
    this.term = term;
    this.dpDimensions = dpDimensions;
  }

  /**
   * Returns the {@link NGram}.
   *
   * @return the {@link NGram}
   */
  public NGram term() {
    return term;
  }

  /**
   * Returns the number of dimentions in the {@link DPW} profile.
   *
   * @return the number of dimentions in the {@link DPW} profile
   */
  public int size() {
    return dpDimensions.size();
  }

  /**
   * Return the weight of the term in this profile.
   *
   * @param term {@link DpDimension} name ({@link NGram})
   * @return the weight of the term in this profile
   */
  public double dimention(NGram term) {
    double rv = 0.0;
    Comparator<DpDimension> c = (DpDimension a, DpDimension b) -> (a.term.compareTo(b.term));
    Collections.sort(dpDimensions, c);
    int idx = Collections.binarySearch(dpDimensions, new DpDimension(term, term, 0), c);
    if (idx >= 0)
      rv = dpDimensions.get(idx).value;
    return rv;
  }

  /**
   * Returns a list with all the {@link DpDimension}.
   *
   * @return a list with all the {@link DpDimension}
   */
  public List<DpDimension> dimentions() {
    return dpDimensions;
  }

  @Override
  public double similarityTo(DPW dpw) {
    double rv = 0.0;
    if (term.equals(dpw.term))
      rv = 1.0;
    else
      rv = similarity(dpDimensions, dpw.dpDimensions);
    return rv;
  }

  /**
   * @param c1
   * @param c2
   * @return
   */
  public static double similarity(List<DpDimension> coor1, List<DpDimension> coor2) {
    Comparator<DpDimension> c = (DpDimension a, DpDimension b) -> (a.term.compareTo(b.term));
    Collections.sort(coor1, c);
    Collections.sort(coor2, c);

    double dataA[] = new double[coor1.size() + coor2.size()],
        dataB[] = new double[coor1.size() + coor2.size()];

    int i = 0, j = 0, idx = 0;
    while (i < coor1.size() && j < coor2.size()) {
      if (coor1.get(i).term.compareTo(coor2.get(j).term) == 0) {
        dataA[idx] = coor1.get(i++).value;
        dataB[idx] = coor2.get(j++).value;
      } else if (coor1.get(i).term.compareTo(coor2.get(j).term) < 0) {
        dataA[idx] = coor1.get(i++).value;
        dataB[idx] = 0.0;
      } else {
        dataA[idx] = 0.0;
        dataB[idx] = coor2.get(j++).value;
      }
      idx++;
    }

    while (i < coor1.size()) {
      dataA[idx] = coor1.get(i++).value;
      dataB[idx] = 0.0;
      idx++;
    }

    while (j < coor2.size()) {
      dataA[idx] = 0.0;
      dataB[idx] = coor2.get(j++).value;
      idx++;
    }

    return (new Vector(dataA, 0, idx)).cosine(new Vector(dataB, 0, idx));
  }

  @Override
  public String toString() {
    return term.toString() + " " + coordinatesToString(dpDimensions);
  }

  /**
   * @param dpDimensions
   * @return
   */
  protected static String coordinatesToString(List<DpDimension> dpDimensions) {
    Comparator<DpDimension> c = (DpDimension a, DpDimension b) -> (Double.compare(b.value, a.value));
    Collections.sort(dpDimensions, c);
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    int i = 0;
    for (int t = dpDimensions.size() - 1; i < t; i++)
      sb.append(dpDimensions.get(i).toString() + "; ");
    if (!dpDimensions.isEmpty())
      sb.append(dpDimensions.get(i).toString());
    sb.append(']');
    return sb.toString();
  }

  /**
   * Applies an {@link DPWOptimization} to the profile.
   *
   * @param dpOptimizer instance of{@link DPWOptimization}
   */
  public void optimize(DPWOptimization dpOptimizer) {
    dpDimensions = dpOptimizer.optimize(dpDimensions);
  }

  /**
   *
   */
  public static class DpDimension {
    public final NGram term;
    public final NGram stemm;
    public final double value;

    /**
     * @param term
     * @param stemm
     * @param value
     */
    public DpDimension(final NGram term, final NGram stemm, final double value) {
      this.term = term;
      this.stemm = stemm;
      this.value = value;
    }

    @Override
    public String toString() {
      return "(" + term.toString() + "[" + stemm + "]; " + value + ")";
    }

    @Override
    public int hashCode() {
      return term.hashCode() ^ stemm.hashCode() ^ Double.valueOf(value).hashCode();
    }

    @Override
    public boolean equals(Object o) {
      boolean rv = false;
      if (o != null) {
        if (o == this)
          rv = true;
        else if (o instanceof DpDimension) {
          DpDimension c = (DpDimension) o;
          rv = this.term.equals(c.term) &&
              this.stemm.equals(c.stemm) &&
              this.value == c.value;
        }
      }
      return rv;
    }
  }
}
