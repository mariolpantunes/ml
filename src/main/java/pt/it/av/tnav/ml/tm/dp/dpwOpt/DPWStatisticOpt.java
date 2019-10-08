package pt.it.av.tnav.ml.tm.dp.dpwOpt;

import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.MathUtils;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements a Distributional Profile optimizer based on P-value statistical significance.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWStatisticOpt implements DPWOpt {
  private static WeakReference<DPWOpt> wro = null;
  protected static final int MD = 10;
  private static final double CP = 0.01;

  private final int min, neighborhood;
  private final double alpha;

  public DPWStatisticOpt(final int min, final int neighborhood, final double alpha) {
    this.min = min;
    this.neighborhood = neighborhood;
    this.alpha = alpha;
  }

  @Override
  public List<DPW.DpDimension> optimize(final NGram term,
                                        final NGram stemm,
                                        final List<DPW.DpDimension> dpDimensions) {
    List<DPW.DpDimension> rv = dpDimensions;
    if (dpDimensions.size() > min) {
      int vocabulary = dpDimensions.size(), total = 0;
      for (DPW.DpDimension c : dpDimensions)
        total += c.value - c.term.size() + 1;
      int partitions = total;
      //dpDimensions.removeIf(p -> probs((int) p.value, partitions, vocabulary) >= alpha);
      rv = dpDimensions.parallelStream().
          filter(p -> probs((int) p.value, partitions, vocabulary) < alpha).
          collect(Collectors.toList());
    }
    return rv;
  }

  private double probs(int freq, int partitions, int vocabulary) {
    double pr = 0.0;
    //partitions = partitions < freq ? freq : partitions;
    //System.out.print("("+freq+"; "+partitions+"; "+vocabulary+")");
    for (int i = 0; i < freq; i++)
      pr += tprobs(i, partitions, vocabulary);
    //System.out.println(" = "+(1.0 - pr));
    return 1.0 - pr;
  }

  private double tprobs(int freq, int partitions, int vocabulary) {
    double pr = 0.0;
    try {
      //System.err.println("("+freq+"; "+partitions+"; "+vocabulary+")");
      BigDecimal C = MathUtils.binomialBD(partitions, freq);
      //System.err.println("C = "+C);

      BigDecimal bd1 = new BigDecimal(vocabulary - 1);
      bd1 = bd1.pow(partitions - freq);
      //System.err.println("(V-1)^(P-F) = "+bd1);

      BigDecimal bd2 = new BigDecimal(vocabulary);
      bd2 = bd2.pow(partitions);
      //System.err.println("V^P = "+bd2);

      bd1 = bd1.divide(bd2, 100, RoundingMode.HALF_UP);
      //System.err.println("(V-1)^(P-F) / V^P = "+bd1);

      pr = C.multiply(bd1).doubleValue();
      //System.err.println("Pr = "+pr);
    } catch (Exception e) {
      System.out.print("(" + freq + "; " + partitions + "; " + vocabulary + ")");
      throw e;
    }

    return pr;
  }

  /**
   * Builds a static {@link WeakReference} to a {@link DPWOpt} class.
   * <p>
   *   This method should be used whenever the {@link DPWOpt} will be built and destroy multiple times.
   *   It will also share a single stemmer through several process/threads.
   * </p>
   *
   * @return {@link DPWOpt} reference that points to a {@link DPWStatisticOpt}.
   */
  public synchronized static DPWOpt build() {
    DPWOpt rv = null;
    if (wro == null) {
      rv = new DPWStatisticOpt(MD,DPW.N,CP);
      wro = new WeakReference<>(rv);
    } else {
      rv = wro.get();
      if(rv == null) {
        rv = new DPWStatisticOpt(MD,DPW.N,CP);
        wro = new WeakReference<>(rv);
      }
    }
    return rv;
  }
}
