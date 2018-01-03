package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.ml.clustering.Cluster;
import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.utils.ArrayUtils;
import pt.it.av.atnog.utils.PrintUtils;
import pt.it.av.atnog.utils.structures.Similarity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//TODO: until find a better solution -> Build DPWC with single clusters
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
        double s = DPW.similarity(c1.dpDimensions, c2.dpDimensions) * ((c1.affinity + c2.affinity) / 2.0);
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
   * Returns a {@link DPWC} with a single category from a {@link DPW}.
   * In other words, converts a {@link DPW} into a {@link DPWC}.
   *
   * @param dpw
   * @param <P>
   * @return Returns a {@link DPWC} with a single category from a {@link DPW}.
   */
  public static <P extends DPPoint<P>> DPWC buildDPWC(final DPW dpw) {
    List<DPWC.Category> categories = new ArrayList<>();
    categories.add(new DPWC.Category(dpw.dimentions(), 1.0));
    return new DPWC(dpw.term(), categories);
  }

  /**
   * Returns a {@link DPWC} built using a {@link DPW} and a set of clusters.
   * Assumes equal affinity for each word category.
   *
   * @param dpw
   * @param clusters
   * @param <P>
   * @return
   */
  public static <P extends DPPoint<P>> DPWC buildDPWC_no_affinity(final DPW dpw, final List<Cluster<P>> clusters) {
    List<Cluster<P>> validClusters = new ArrayList<>();

    // Remove empyt and single element clusters
    for (Iterator<Cluster<P>> it = clusters.iterator(); it.hasNext(); ) {
      Cluster<P> cluster = it.next();
      if (cluster.size() > 0) {
        validClusters.add(cluster);
      }
    }

    // Build the DPWC
    List<DPWC.Category> categories = new ArrayList<>();
    for (Cluster<P> cluster : validClusters) {
      List<DPW.DpDimension> dpDimensions = new ArrayList<>();
      for (P point : cluster) {
        dpDimensions.add(filter(dpw, point));
      }
      categories.add(new DPWC.Category(dpDimensions, 1.0));
    }

    return new DPWC(dpw.term(), categories);
  }

  /**
   * Returns a {@link DPWC} built using a {@link DPW} and a set of clusters.
   * Uses the average affinity of each cluster as a weight to the similarity metric.
   *
   * @param dpw
   * @param clusters
   * @return
   */
  public static <P extends DPPoint<P>> DPWC buildDPWC(final DPW dpw, final List<Cluster<P>> clusters) {
    List<Cluster<P>> validClusters = new ArrayList<>();
    double a[] = new double[clusters.size()];

    // Remove empyt and single element clusters
    for (Iterator<Cluster<P>> it = clusters.iterator(); it.hasNext(); ) {
      Cluster<P> cluster = it.next();
      if (cluster.size() > 0) {
        validClusters.add(cluster);
      }
    }

    // Pre-compute affinity
    // Average affinity between data points
    int i = 0;
    for (Cluster<P> cluster : validClusters) {
      for (P point : cluster) {
        a[i] += point.affinity(dpw);
      }
      a[i++] /= (double) cluster.size();
    }

    // Scale affinity
    double max = a[ArrayUtils.max(a)];
    for (i = 0; i < a.length; i++)
      a[i] /= max;

    // Build the DPWC
    List<DPWC.Category> categories = new ArrayList<>();
    i = 0;
    for (Cluster<P> cluster : validClusters) {
      List<DPW.DpDimension> dpDimensions = new ArrayList<>();
      for (P point : cluster) {
        dpDimensions.add(filter(dpw, point));
      }
      categories.add(new DPWC.Category(dpDimensions, a[i++]));
    }

    return new DPWC(dpw.term(), categories);
  }

  /**
   * @param dpw
   * @param point
   * @return
   */
  public static <P extends DPPoint> DPW.DpDimension filter(DPW dpw, P point) {
    DPW.DpDimension rv = null;
    for (DPW.DpDimension dimension : dpw.dimentions())
      if (dimension.term.equals(point.term())) {
        rv = dimension;
        break;
      }
    if (rv == null)
      System.err.println("NULL -> " + point);
    return rv;
  }

  /**
   *
   */
  public static class Category {
    public final List<DPW.DpDimension> dpDimensions;
    public final double affinity;


    public Category(final List<DPW.DpDimension> dpDimensions, final double affinity) {
      this.dpDimensions = dpDimensions;
      this.affinity = affinity;
    }

    /**
     * Returns the number of dimentions in the {@link Category}.
     *
     * @return the number of dimentions in the {@link Category}.
     */
    public int size() {
      return dpDimensions.size();
    }

    @Override
    public String toString() {
      return "{" + affinity + "; " + PrintUtils.list(dpDimensions) + "}";
    }

    @Override
    public int hashCode() {
      return dpDimensions.hashCode() ^ Double.valueOf(affinity).hashCode();
    }

    @Override
    public boolean equals(Object o) {
      boolean rv = false;
      if (o != null) {
        if (o == this)
          rv = true;
        else if (o instanceof Category) {
          Category c = (Category) o;
          rv = this.dpDimensions.equals(c.dpDimensions) && this.affinity == c.affinity;
        }
      }
      return rv;
    }
  }
}
