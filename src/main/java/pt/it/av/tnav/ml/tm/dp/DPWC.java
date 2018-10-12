package pt.it.av.tnav.ml.tm.dp;

import pt.it.av.tnav.ml.clustering.hierarchical.Hierarchical;
import pt.it.av.tnav.ml.clustering.hierarchical.SLINK;
import pt.it.av.tnav.ml.tm.dp.cache.DPWPCache;
import pt.it.av.tnav.ml.tm.dp.dppoint.CachePoint;
import pt.it.av.tnav.ml.tm.dp.dppoint.CoOccPoint;
import pt.it.av.tnav.ml.tm.dp.dppoint.DPPoint;
import pt.it.av.tnav.ml.tm.dp.dppoint.MatrixPoint;
import pt.it.av.tnav.ml.tm.dp.dpwOpt.DPWNMFOpt;
import pt.it.av.tnav.ml.tm.dp.dpwOpt.DPWOpt;
import pt.it.av.tnav.utils.ArrayUtils;
import pt.it.av.tnav.utils.CollectionsUtils;
import pt.it.av.tnav.utils.MathUtils;
import pt.it.av.tnav.utils.PrintUtils;
import pt.it.av.tnav.utils.Utils;
import pt.it.av.tnav.utils.bla.Matrix;
import pt.it.av.tnav.utils.bla.Vector;
import pt.it.av.tnav.utils.json.JSONArray;
import pt.it.av.tnav.utils.json.JSONObject;
import pt.it.av.tnav.utils.json.JSONValue;
import pt.it.av.tnav.utils.structures.Distance;
import pt.it.av.tnav.utils.structures.Similarity;
import pt.it.av.tnav.ml.clustering.cluster.Cluster;
import pt.it.av.tnav.ml.tm.ngrams.NGram;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//TODO: until find a better solution -> Build DPWC with single clusters
/**
 * Distributional Profile of multiple Word Categories.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWC implements Similarity<DPWC>, Distance<DPWC>, Comparable<DPWC> {
  private static final int MIN_LAT=2, MAX_LAT=5, NMF_REPS=5;
  private static final double RATIO = 1.5;
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
  public double similarityTo(DPWC dpwc) {
    double rv = 0.0;

    if(term.equals(dpwc.term)) {
      rv = 1.0;
    } else {
      for (Category c1 : categories) {
        for (Category c2 : dpwc.categories) {
          double s = DPW.similarity(c1.dpDimensions, c2.dpDimensions)
              * ((c1.affinity + c2.affinity) / 2.0);
          if (s > rv) {
            rv = s;
          }
        }
      }
    }

    return rv;
  }

  @Override
  public double distanceTo(DPWC dpwc) {
    return 1.0-similarityTo(dpwc);
  }

  @Override
  public int compareTo(DPWC o) {
    return term.compareTo(o.term);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(term.toString() + System.getProperty("line.separator"));
    for (Category category : categories)
      sb.append(category.toString() + System.getProperty("line.separator"));
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    boolean rv = true;
    if (this == o) {
      rv = true;
    } else if (o == null) {
      rv = false;
    } else if (getClass() != o.getClass()) {
      rv = false;
    } else {
      DPWC dpwc = (DPWC) o;
      rv = term.equals(dpwc.term) &&
          CollectionsUtils.equals(categories, dpwc.categories);
    }
    return rv;
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

    // Remove empyt clusters
    for (Cluster<P> cluster : clusters){
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

    // Remove empyt clusters
    for (Cluster<P> cluster : clusters){
      if (cluster.size() > 0) {
        validClusters.add(cluster);
      }
    }

    double a[] = new double[validClusters.size()];

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
    Collections.sort(categories, Collections.reverseOrder());
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
    if (rv == null) {
      System.err.println("NULL -> " + point);
    }
    return rv;
  }

  /**
   *
   */
  public static class Category implements Comparable<Category> {
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
          rv = CollectionsUtils.equals(this.dpDimensions, c.dpDimensions) &&
              MathUtils.equals(this.affinity,c.affinity, 0.01);
        }
      }
      return rv;
    }

    @Override
    public int compareTo(Category o) {
      return Double.compare(affinity, o.affinity);
    }
  }

  /**
   *
   * @param file
   * @return
   */
  public static DPWC load(Reader in) throws IOException {
    JSONObject json = JSONObject.read(in);

    List<Category> categories = new ArrayList<>();
    NGram term = NGram.Unigram(json.get("ngram").asString());

    JSONArray cats = json.get("categories").asArray();
    for(JSONValue c : cats) {
      List<DPW.DpDimension> dimensions = new ArrayList<>();
      for(JSONValue d: c.asObject().get("dimentions").asArray()) {
        JSONObject jd = d.asObject();
        dimensions.add(new DPW.DpDimension(NGram.Unigram(jd.get("term").asString()),
            NGram.Unigram(jd.get("stemm").asString()), jd.get("value").asDouble()));
      }
      categories.add(new Category(dimensions, c.asObject().get("affinity").asDouble()));
    }

    return new DPWC(term, categories);
  }

  public static DPWC learn(final NGram ngram, final DPWPCache dpwpCache) {
    return learn(ngram, dpwpCache, MIN_LAT, MAX_LAT, NMF_REPS, RATIO);
  }

  public static DPWC learn(final NGram ngram, final DPWPCache dpwpCache,
                           final int minLat, final int maxLat, final int reps, final double ratio) {
    // Learn basic DPW
    DPW dpw = dpwpCache.fetch(ngram);

    // Learn Context DPWs
    List<DPW> context = new ArrayList<>(dpw.size()+1);
    for (DPW.DpDimension dimension : dpw.dimentions()) {
      context.add(dpwpCache.fetch(dimension.term));
    }
    if(!context.contains(dpw)) {
      context.add(dpw);
    }
    Collections.sort(context);

    // Build co-occurence points to learn latent information
    double maxCo[] = new double[context.size()];
    for (int i = 0; i < context.size(); i++) {
      maxCo[i] = Collections.max(context.get(i).dimentions()).value;
    }

    int maxCoIdx = ArrayUtils.max(maxCo);
    List<CachePoint<CoOccPoint>> points = new ArrayList<>();
    // Initialize the co-occurrence points for clustering
    for (int i = 0; i < context.size(); i++) {
      if (maxCo[i] > 0.0) {
        points.add(new CachePoint<>(new CoOccPoint(context.get(i), maxCo[maxCoIdx])));
      }
    }

    // Map NGrams to Indexes
    List<String> map = new ArrayList<>(points.size());
    for(int i = 0; i < points.size(); i++) {
      map.add(points.get(i).term().toString());
    }

    // Learn latent information with NMF
    Matrix sim = Matrix.identity(points.size());

    for (int i = 0; i < points.size() - 1; i++) {
      for (int j = i + 1; j < points.size(); j++) {
        double similarity = points.get(i).similarityTo(points.get(j));
        sim.set(i, j, similarity);
        sim.set(j, i, similarity);
      }
    }

    Matrix wh[] = sim.nmf((int)Math.round(points.size() / (double)minLat));
    Matrix nf = wh[0].mul(wh[1]), tnf = null;
    double bcost = sim.euclideanDistance(nf);
    for (int i = 1; i < reps; i++) {
      wh = sim.nmf((int)Math.round(points.size() / (double)minLat));
      tnf = wh[0].mul(wh[1]);
      double cost = sim.euclideanDistance(tnf);
      if (cost < bcost) {
        nf = tnf;
        bcost = cost;
      }
    }

    for (int d = minLat+1; d <= maxLat && d < points.size(); d++) {
      for (int i = 0; i < reps; i++) {
        wh = sim.nmf((int)Math.round(points.size() / (double)d));
        tnf = wh[0].mul(wh[1]);
        double cost = sim.euclideanDistance(tnf);
        if (cost < bcost) {
          nf = tnf;
          bcost = cost;
        }
      }
    }

    // Optimize DPW profile with latent information
    DPWOpt opt = new DPWNMFOpt(nf, map);
    dpw.optimize(opt);

    // Generate new points with latent information
    List<MatrixPoint> mpoints = new ArrayList<>();
    for(int i = 0; i < points.size(); i++) {
      DPPoint p = points.get(i);
      mpoints.add(new MatrixPoint(p.dpw(), nf, map));
    }

    // Cluster DP Points into categories
    Hierarchical hc = SLINK.build();
    int max = (int)Math.round(mpoints.size()/ratio),
        kmax = (max < mpoints.size())?max:mpoints.size()-1;
    List<Cluster<MatrixPoint>> clusters = hc.clustering(mpoints, 2, kmax);
    DPWC dpwc = DPWC.buildDPWC(dpw, clusters);
    return dpwc;
  }

  //private static List<>

  /**
   * Stores a {@link DPWC} in JSON format inside a compressed file.
   *
   * @param dpwc
   * @param out
   */
  public static void store(DPWC dpwc, Writer out) throws IOException {
    JSONObject json = new JSONObject();
    JSONArray categories = new JSONArray();

    for(Category c :dpwc.categories) {
      JSONObject category = new JSONObject();
      JSONArray dimentions = new JSONArray();
      for(DPW.DpDimension d : c.dpDimensions) {
        JSONObject dimention = new JSONObject();
        dimention.put("stemm", d.stemm.toString());
        dimention.put("term",d.term.toString());
        dimention.put("value",d.value);
        dimentions.add(dimention);
      }
      category.put("afinity", c.affinity);
      category.put("dimentions", dimentions);
    }

    json.put("categories", categories);
    json.put("term", dpwc.term.toString());

    json.write(out);
  }
}