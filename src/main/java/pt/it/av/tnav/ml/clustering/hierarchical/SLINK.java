package pt.it.av.tnav.ml.clustering.hierarchical;

import pt.it.av.tnav.utils.ArrayUtils;
import pt.it.av.tnav.utils.structures.Distance;
import java.lang.ref.WeakReference;
import java.util.List;

public class SLINK implements Hierarchical {
  private static WeakReference<Hierarchical> wrh;

  @Override
  public <D extends Distance<D>> int[][] clustering(final List<D> dps) {
    double[] height = new double[dps.size()], mus = new double[dps.size()];
    int[] parent = new int[dps.size()];

    parent[0] = 0;
    height[0] = Double.POSITIVE_INFINITY;
    for (int i = 1; i < dps.size(); i++) {
      parent[i] = i;
      height[i] = Double.POSITIVE_INFINITY;

      for (int j = 0; j < i; j++) {
        mus[j] = dps.get(i).distanceTo(dps.get(j));
      }

      for (int j = 0; j < i; j++) {
        if (height[j] >= mus[j]) {
          mus[parent[j]] = Math.min(mus[parent[j]], height[j]);
          height[j] = mus[j];
          parent[j] = i;
        } else {
          mus[parent[j]] = Math.min(mus[parent[j]], mus[j]);
        }
      }

      for (int j = 0; j < i; j++) {
        if (height[j] >= height[parent[j]]) {
          parent[j] = i;
        }
      }
    }

    // System.out.println(PrintUtils.array(height));
    // System.out.println(PrintUtils.array(parent));
    // System.out.println(PrintUtils.array(mus));

    int[][] d = new int[dps.size() - 1][2];

    for (int i = 0; i < dps.size() - 1; i++) {
      // find minimum level
      int idx = ArrayUtils.min(height);
      d[i][0] = idx;
      d[i][1] = parent[idx];
      height[idx] = Double.POSITIVE_INFINITY;
    }

    /*
     * System.out.println("Merge elements"); for(int i = 0; i < d.length; i++) {
     * //System.out.println(dps.get(d[i][0])+" + "+dps.get(d[i][1]));
     * System.out.println(d[i][0]+" + "+d[i][1]); }
     */

    return d;
  }

  /**
   * Builds a static {@link WeakReference} to a {@link Hierarchical} class.
   * <p>
   * This method should be used whenever the {@link Hierarchical} will be built
   * and destroy multiple times. It will also share a single stemmer through
   * several process/threads.
   * </p>
   *
   * @return {@link Hierarchical} reference that points to a {@link SLINK}
   */
  public synchronized static Hierarchical build() {
    Hierarchical rv = null;
    if (wrh == null) {
      rv = new SLINK();
      wrh = new WeakReference<>(rv);
    } else {
      rv = wrh.get();
      if (rv == null) {
        rv = new SLINK();
        wrh = new WeakReference<>(rv);
      }
    }
    return rv;
  }
}
