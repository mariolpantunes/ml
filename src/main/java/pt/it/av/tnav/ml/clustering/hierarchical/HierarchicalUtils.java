package pt.it.av.tnav.ml.clustering.hierarchical;

import pt.it.av.tnav.ml.clustering.cluster.Cluster;
import pt.it.av.tnav.ml.clustering.cluster.ClusterUtils;
import pt.it.av.tnav.ml.clustering.curvature.Curvature;
import pt.it.av.tnav.utils.ArrayUtils;
import pt.it.av.tnav.utils.MathUtils;
import pt.it.av.tnav.utils.structures.Distance;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HierarchicalUtils {
  /** Static library */
  private HierarchicalUtils() {
  }

  /**
   * 
   */
  public static <D extends Distance<D>> List<Cluster<D>> d2c(final List<D> dps, final int[][] dendrogram,
      final Curvature curvature) {
    int min = 2, max = dps.size(), size = max - min + 1;
    double[] x = new double[size], y = new double[size];

    // Create individual clusters
    List<Cluster<D>> clusters = new ArrayList<>(dps.size());
    for (int i = 0; i < dps.size(); i++) {
      clusters.add(new Cluster<D>(dps.get(i)));
    }

    // Merge the clusters based on the d matrix
    int i = 0, cSize = dps.size();
    for (; cSize > max; i++, cSize--) {
      clusters.get(dendrogram[i][1]).addAll(clusters.get(dendrogram[i][0]));
      clusters.set(dendrogram[i][0], null);
    }

    int j = 0;
    for (; cSize >= min; i++, cSize--) {
      clusters.get(dendrogram[i][1]).addAll(clusters.get(dendrogram[i][0]));
      clusters.set(dendrogram[i][0], null);
      x[size - j - 1] = dps.size() - i;
      y[size - j - 1] = ClusterUtils.avgDistortion(clusters);
      j++;
    }
    ArrayUtils.replace(y, 0, MathUtils.eps());
    int idx = curvature.elbow(x, y);

    // Rebuild the cluster to the ideal number
    clusters.clear();
    for (i = 0; i < dps.size(); i++) {
      clusters.add(new Cluster<D>(dps.get(i)));
    }
    i = 0;
    cSize = dps.size();
    for (; cSize > x[idx]; i++, cSize--) {
      clusters.get(dendrogram[i][1]).addAll(clusters.get(dendrogram[i][0]));
      clusters.set(dendrogram[i][0], null);
    }

    clusters.removeIf(Objects::isNull);

    return clusters;
  }

  public static <D extends Distance<D>> List<Cluster<D>> d2c(final List<D> dps, final int[][] dendrogram, final int k) {

    // Create individual clusters
    List<Cluster<D>> clusters = new ArrayList<>(dps.size());
    int i = 0;
    for (i = 0; i < dps.size(); i++) {
      clusters.add(new Cluster<D>(dps.get(i)));
    }
    // Merge the clusters based on the dendrogram matrix
    i = 0;
    int cSize = dps.size();
    for (; cSize > k; i++, cSize--) {
      clusters.get(dendrogram[i][1]).addAll(clusters.get(dendrogram[i][0]));
      clusters.set(dendrogram[i][0], null);
    }

    clusters.removeIf(Objects::isNull);

    return clusters;
  }
}
