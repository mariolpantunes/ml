package pt.it.av.tnav.ml.tm.dp;

import pt.it.av.tnav.ml.tm.StopWords.EnglishStopWords;
import pt.it.av.tnav.ml.tm.StopWords.StopWords;
import pt.it.av.tnav.ml.tm.TM;
import pt.it.av.tnav.ml.tm.corpus.Corpus;
import pt.it.av.tnav.ml.tm.dp.dpwOpt.DPWOpt;
import pt.it.av.tnav.ml.tm.dp.dpwOpt.DPWStatisticOpt;
import pt.it.av.tnav.ml.tm.dp.dpwOpt.DPWStemmOpt;
import pt.it.av.tnav.ml.tm.stemmer.PorterStemmer;
import pt.it.av.tnav.ml.tm.stemmer.Stemmer;
import pt.it.av.tnav.ml.tm.tokenizer.TextTokenizer;
import pt.it.av.tnav.utils.bla.Vector;
import pt.it.av.tnav.utils.json.JSONArray;
import pt.it.av.tnav.utils.json.JSONObject;
import pt.it.av.tnav.utils.json.JSONValue;
import pt.it.av.tnav.utils.parallel.pipeline.Pipeline;
import pt.it.av.tnav.utils.structures.Similarity;
import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.structures.mutableNumber.MutableDouble;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Distributional Profile of a Word ({@link NGram}).
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPW implements Similarity<DPW>, Comparable<DPW>{
  protected static final int ESW = 3, ELW = 14, N=5, NG=1;
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
    dpDimensions.sort(c);
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
   * @param coor1
   * @param coor2
   * @return
   */
  public static double similarity(List<DpDimension> coor1, List<DpDimension> coor2) {
    Comparator<DpDimension> c = (DpDimension a, DpDimension b) -> (a.term.compareTo(b.term));
    coor1.sort(c);
    coor2.sort(c);

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
    dpDimensions.sort(c);
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
   * Applies an {@link DPWOpt} to the profile.
   *
   * @param dpOptimizer instance of{@link DPWOpt}
   */
  public void optimize(DPWOpt dpOptimizer) {
    dpDimensions = dpOptimizer.optimize(term, dpDimensions);
  }

  @Override
  public int compareTo(DPW o) {
    return term.compareTo(o.term);
  }

  /**
   *
   * @author Mário Antunes
   * @version 1.0
   */
  public static class DpDimension implements Comparable<DpDimension>{
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

    @Override
    public int compareTo(DpDimension o) {
      return Double.compare(this.value, o.value);
    }
  }

  /**
   *
   * @param ngram
   * @param corpus
   * @return
   */
  public static DPW learn(final NGram ngram, final Corpus corpus) {
    return learn(ngram, corpus, EnglishStopWords.build(), PorterStemmer.build(), ESW, ELW, N, NG);
  }

  /**
   *
   * @param ngram
   * @param corpus
   * @param stw
   * @param st
   * @param sw
   * @param lw
   * @param n
   * @param ng
   * @return
   */
  public static DPW learn(final NGram ngram, final Corpus corpus, final StopWords stw,
                          final Stemmer st, final int sw, final int lw, final int n, final int ng) {
    Iterator<String> aIt = corpus.iterator(ngram);
    Map<NGram, MutableDouble> ma = new HashMap<>();
    Pipeline pipeline = TM.DPPipeline(ngram, TextTokenizer.build(), stw
        , sw , lw, n, ng, st, ma);
    BlockingQueue<Object> sink = pipeline.sink();
    pipeline.start();

    while (aIt.hasNext()) {
      String txt;
      try {
        txt = aIt.next();
      } catch (Exception e) {
        e.printStackTrace();
        txt = null;
      }
      if (!txt.isEmpty())
        sink.add(txt);
    }

    try {
      pipeline.join();
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<DPW.DpDimension> profile = new ArrayList<>();
    Iterator<Map.Entry<NGram, MutableDouble>> mIt = ma.entrySet().iterator();
    while (mIt.hasNext()) {
      Map.Entry<NGram, MutableDouble> entry = mIt.next();
      NGram nGramP = entry.getKey();
      double d = entry.getValue().doubleValue();
      profile.add(new DPW.DpDimension(nGramP, PorterStemmer.build().stem(nGramP), d));
    }

    DPW dpw = new DPW(ngram, profile);

    dpw.optimize(DPWStemmOpt.build());
    dpw.optimize(DPWStatisticOpt.build());

    return dpw;
  }

  public static DPW load(Reader in) throws IOException {
    JSONObject json = JSONObject.read(in);

    NGram term = NGram.Unigram(json.get("ngram").asString());
    List<DPW.DpDimension> dimensions = new ArrayList<>();

    for(JSONValue d: json.asObject().get("dimentions").asArray()) {
        JSONObject jd = d.asObject();
        dimensions.add(new DPW.DpDimension(NGram.Unigram(jd.get("term").asString()),
            NGram.Unigram(jd.get("stemm").asString()), jd.get("value").asDouble()));
    }

    return new DPW(term, dimensions);
  }

  public static void store(DPW dpw, Writer out) throws IOException {
    JSONObject json = new JSONObject();

    JSONArray dimentions = new JSONArray();
    for(DPW.DpDimension d : dpw.dpDimensions) {
        JSONObject dimention = new JSONObject();
        dimention.put("stemm", d.stemm.toString());
        dimention.put("term",d.term.toString());
        dimention.put("value",d.value);
        dimentions.add(dimention);
    }
    json.put("dimentions", dimentions);
    json.put("term", dpw.term.toString());

    json.write(out);
  }
}
