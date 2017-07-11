package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.utils.PrintUtils;
import pt.it.av.atnog.utils.structures.Similarity;

import java.util.List;

/**
 * Distributional Profile of multiple Word Categories.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWC implements Similarity<DPWC> {
  private final NGram term;
  private List<Category> categories;

  /**
   * @param term
   * @param categories
   */
  public DPWC(final NGram term, List<Category> categories) {
    this.term = term;
    this.categories = categories;
  }

  /**
   * @return
   */
  public NGram term() {
    return term;
  }

  /**
   * @return
   */
  public int size() {
    return categories.size();
  }

  /**
   * @param c
   * @return
   */
  public int size(int c) {
    return categories.get(c).size();
  }

  @Override
  public double similarityTo(DPWC dpc) {
    double rv = 0.0;

    for (Category c1 : categories) {
      for (Category c2 : dpc.categories) {
        double s = DPW.similarity(c1.dpDimensions, c2.dpDimensions) * ((c1.bias + c2.bias) / 2.0);
        if (s > rv)
          rv = s;
      }
    }

    return rv;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(term.toString() + System.getProperty("line.separator"));
    for (Category category : categories)
      sb.append(category.toString() + System.getProperty("line.separator"));
    return sb.toString();
  }

  /**
   *
   */
  public static class Category {
    public final List<DPW.DpDimension> dpDimensions;
    public final double bias;


    public Category(final List<DPW.DpDimension> dpDimensions, final double bias) {
      this.dpDimensions = dpDimensions;
      this.bias = bias;
    }


    /**
     * Returns the number of dimentions in the category.
     *
     * @return the number of dimentions in the category.
     */
    public int size() {
      return dpDimensions.size();
    }

    @Override
    public String toString() {
      return "{" + bias + "; " + PrintUtils.list(dpDimensions) + "}";
    }

    @Override
    public int hashCode() {
      return dpDimensions.hashCode() ^ Double.valueOf(bias).hashCode();
    }

    @Override
    public boolean equals(Object o) {
      boolean rv = false;
      if (o != null) {
        if (o == this)
          rv = true;
        else if (o instanceof Category) {
          Category c = (Category) o;
          rv = this.dpDimensions.equals(c.dpDimensions) && this.bias == c.bias;
        }
      }
      return rv;
    }
  }
}
