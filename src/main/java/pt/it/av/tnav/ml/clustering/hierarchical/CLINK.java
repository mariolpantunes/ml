package pt.it.av.tnav.ml.clustering.hierarchical;

import pt.it.av.tnav.ml.clustering.cluster.Cluster;
import pt.it.av.tnav.ml.clustering.curvature.Lmethod;
import pt.it.av.tnav.utils.ArrayUtils;
import pt.it.av.tnav.utils.structures.Distance;
import java.util.List;

/**
 * Complete-linkage clustering implementation.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 * @see <a href="https://en.wikipedia.org/wiki/Complete-linkage clustering">
 *      Complete-linkage_clustering </a>
 */
public class CLINK {
  /** Static library */
  private CLINK() {
  }

  public static <D extends Distance<D>> int[][] dendogram(final List<D> dps) {
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
        if (height[j] < mus[j]) {
          mus[parent[j]] = Math.max(mus[parent[j]], mus[j]);
          mus[j] = Double.POSITIVE_INFINITY;
        }
      }

      int a = i - 1;
      for (int j = i - 1; j >= 0; j--) {
        if (height[j] >= mus[parent[j]]) {
          if (mus[j] < mus[a]) {
            a = j;
          }
        } else {
          mus[j] = Double.POSITIVE_INFINITY;
        }
      }

      int b = parent[a];
      double c = height[a];
      parent[a] = i;
      height[a] = mus[a];

      if (a < i - 1) {
        while (b != i) {
          if (b == i - 1) {
            parent[b] = i;
            height[b] = c;
            break;
          }
          int d = parent[b];
          parent[b] = i;
          double e = height[b];
          height[b] = c;
          b = d;
          c = e;
        }
      }

      for (int j = 0; j < i; j++) {
        if (parent[parent[j]] == i && height[j] >= height[parent[j]]) {
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

  public static <D extends Distance<D>> List<Cluster<D>> fit(final List<D> dps, final int k) {
    int dendogram[][] = dendogram(dps);
    return HierarchicalUtils.d2c(dps, dendogram, k);
  }

  public static <D extends Distance<D>> List<Cluster<D>> fit(final List<D> dps) {
    int dendogram[][] = dendogram(dps);
    return HierarchicalUtils.d2c(dps, dendogram, new Lmethod());
  }
}
