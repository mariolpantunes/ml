package pt.it.av.tnav.ml.clustering;

import pt.it.av.tnav.utils.ArrayUtils;
import pt.it.av.tnav.utils.Utils;
import pt.it.av.tnav.utils.structures.Distance;
import pt.it.av.tnav.ml.clustering.cluster.Cluster;
import pt.it.av.tnav.ml.clustering.cluster.ClusterUtils;
import pt.it.av.tnav.ml.clustering.curvature.Curvature;
import pt.it.av.tnav.ml.clustering.curvature.Lmethod;

import java.util.List;

/**
 * Implements several methods for optimal clustering.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 2.0
 */
public class AutoK {
  /**
   * Private constructor.
   * This code is a static library.
   */
  private AutoK() { }

  public static <D extends Distance<D>> List<Cluster<D>> silhouette(final Kmeans alg,
                                                                    final List<D> dps, final int min, final int max){
    return silhouette(alg, dps, min, max, 10);
  }

  /**
   *
   * @param alg
   * @param dps
   * @param min
   * @param max
   * @param reps
   * @param <D>
   * @return
   */
  public static <D extends Distance<D>> List<Cluster<D>> silhouette(final Kmeans alg,
  final List<D> dps, final int min, final int max, final int reps){
    final int kmax = (max < dps.size())?max:dps.size()-1;
    double sil[] = new double[(kmax - min) + 1];
    List<Cluster<D>> allClusters[] = Utils.cast(new List[(kmax - min) + 1]);

    int i = 0;
    for (int k = min; k <= kmax; k++, i++) {
      List<Cluster<D>> clusters = alg.clustering(dps, k);
      double wss = ClusterUtils.avgDistortion(clusters);
      for (int j = 1; j < reps; j++) {
        List<Cluster<D>> currentClusters = alg.clustering(dps, k);
        double currentWSS = ClusterUtils.avgDistortion(currentClusters);
        if (currentWSS > wss && !ClusterUtils.emptyClusters(currentClusters)) {
          clusters = currentClusters;
          wss = currentWSS;
        }
      }
      allClusters[i] = clusters;
      sil[i] = ClusterUtils.avgSilhouette(clusters);
    }
    return allClusters[ArrayUtils.max(sil)];
  }

  /**
   *
   * @param alg
   * @param dps
   * @param min
   * @param max
   * @param <D>
   * @return
   */
  public static <D extends Distance<D>> List<Cluster<D>> elbow(final Kmeans alg,
    final List<D> dps, final int min, final int max) {
    return elbow(alg, dps, min, max, 10, new Lmethod());
  }

  /**
   *
   * @param alg
   * @param dps
   * @param min
   * @param max
   * @param <D>
   * @return
   */
  public static <D extends Distance<D>> List<Cluster<D>> elbow(final Kmeans alg,
  final List<D> dps, final int min, final int max, final Curvature cur) {
    return elbow(alg, dps, min, max, 3, cur);
  }

  /**
   *
   * @param alg
   * @param dps
   * @param min
   * @param max
   * @param reps
   * @param <D>
   * @return
   */
  public static <D extends Distance<D>> List<Cluster<D>> elbow(final Kmeans alg,
   final List<D> dps, final int min, final int max, final int reps, final Curvature cur) {
    final int kmax = (max < dps.size())?max:dps.size()-1;
    double wsss[] = new double[(kmax - min) + 1],
        x[] = new double[(kmax - min) + 1];
    List<Cluster<D>> allClusters[] = Utils.cast(new List[(kmax - min) + 1]);

    int i = 0;
    for (int k = min; k <= kmax; k++, i++) {
      x[i] = k;
      List<Cluster<D>> clusters = alg.clustering(dps, k);
      double wss = ClusterUtils.avgDistortion(clusters);
      for (int j = 1; j < reps; j++) {
        List<Cluster<D>> currentClusters = alg.clustering(dps, k);
        double cwss = ClusterUtils.avgDistortion(currentClusters);
        if (cwss > wss && !ClusterUtils.emptyClusters(currentClusters)) {
          clusters = currentClusters;
          wss = cwss;
        }
      }
      allClusters[i] = clusters;
      wsss[i] = wss;
    }

    int idx = 0;
    if (wsss.length > 1) {
      idx = cur.elbow(x, wsss);
    }

    //System.out.println(PrintUtils.array(wsss));

    return allClusters[idx];
  }

  /**
   * @param alg
   * @param dps
   * @param min
   * @param max
   * @param Nd
   * @param <D>
   * @return
   */
  public static <D extends Distance<D>> List<Cluster<D>> clustering(final Kmeans alg,
    final List<D> dps, final int min, final int max, final int Nd) {
    return clustering(alg, dps, min, max, Nd, 10);
  }

  /**
   * @param alg
   * @param dps
   * @param min
   * @param max
   * @param Nd
   * @param reps
   * @param <D>
   * @return
   */
  public static <D extends Distance<D>> List<Cluster<D>> clustering(final Kmeans alg,
    final List<D> dps, int min,int max, int Nd, int reps) {
    final int kmax = (max < dps.size())?max:dps.size()-1;
    double fk[] = new double[(kmax - min) + 1];
    double Sk[] = new double[(kmax - min) + 1];
    double ak[] = new double[(kmax - min) + 1];
    List<Cluster<D>> allClusters[] = Utils.cast(new List[(kmax - min) + 1]);

    int i = 0;
    for (int k = min; k <= kmax; k++, i++) {
      List<Cluster<D>> clusters = alg.clustering(dps, k);
      double wss = ClusterUtils.avgDistortion(clusters);
      for (int j = 1; j < reps; j++) {
        List<Cluster<D>> currentClusters = alg.clustering(dps, k);
        double cwss = ClusterUtils.avgDistortion(currentClusters);
        if (cwss > wss && !ClusterUtils.emptyClusters(currentClusters)) {
          clusters = currentClusters;
          wss = cwss;
        }
      }
      Sk[i] = ClusterUtils.avgSilhouette(clusters);
      fk[i] = f(i, Sk[i], Sk, ak, Nd);
      allClusters[i] = clusters;
    }
    //System.out.println("SK: " + PrintUtils.array(Sk));
    //System.out.println("AK: " + PrintUtils.array(ak));
    //System.out.println("FK: " + PrintUtils.array(fk));
    int idx = ArrayUtils.min(fk, 0, i);
    return allClusters[idx];
  }

  /**
   * @param k
   * @param S
   * @param Sk
   * @param ak
   * @return
   */
  private static double f(int k, double S, double Sk[], double ak[], int Nd) {
    double rv = 0.0;

    if (k == 0)
      rv = 1.0;
    else if (Sk[k - 1] != 0) {
      ak[k] = a(k, Nd, ak[k - 1]);
      rv = S / (ak[k] * Sk[k - 1]);
    } else
      rv = 1.0;

    return rv;
  }

  /**
   * @param k
   * @param Nd
   * @param ak
   * @return
   */
  private static double a(int k, int Nd, double ak) {
    double rv = 0.0;
    if (k == 1)
      rv = 1.0 - (3 / (4 * Nd));
    else
      rv = ak + ((1.0 - ak) / 6.0);
    return rv;
  }
}
