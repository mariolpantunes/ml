package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.ml.clustering.curvature.Kneedle;
import pt.it.av.atnog.utils.ArrayUtils;
import pt.it.av.atnog.utils.structures.Distance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class FGClustering {
  private static final int EMPTY = -1;
  /**
   * Use the same technique as in DBSCAN.
   *
   * @param dps
   * @param minPts
   * @param <D>
   * @return
   */
  public <D extends Distance> List<Cluster<D>> clustering(final List<D> dps, final int minPts) {
    // array with average distance to closest minPts
    double dist[] = new double[dps.size()];

    // Find minPits closer points
    for (int i = 0; i < dps.size(); i++) {
      dist[i] = ArrayUtils.mean(DBSCAN.kCloserPoints(dps, i, minPts));
    }
    // Sort distances
    Arrays.sort(dist);

    // Find curvature and use it as EPS
    double eps = dist[Kneedle.elbow(dist)];
    return clustering(dps, eps);
  }

  /**
   *
   * @param dps
   * @param idx
   * @param eps
   * @param <D>
   * @return
   */
  private static <D extends Distance> List<Integer> neighbors(final List<D> dps, final int idx, final double eps) {
    List<Integer> rv = new ArrayList<>();
    D dp = dps.get(idx);

    for(int i = 0;  i < dps.size(); i++) {
      if(i != idx && dp.distanceTo(dps.get(i)) <= eps) {
        rv.add(i);
      }
    }

    return rv;
  }

  /**
   *
   * @param dps
   * @param eps
   * @param <D>
   * @return
   */
  private static <D extends Distance> List<Candidate> candidates(final List<D> dps, final double eps) {
    List<Candidate> rv = new ArrayList<>();

    for(int i = 0; i < dps.size() - 1; i++) {
      for(int j = i+1; j < dps.size(); j++) {
        double d = dps.get(i).distanceTo(dps.get(j));
        if(d <= eps) {
          rv.add(new Candidate(i,j,d));
        }
      }
    }

    return rv;
  }

  /**
   *
   * @param dps
   * @param radius
   * @param <D>
   * @return
   */
  public static <D extends Distance> List<Cluster<D>> clustering(final List<D> dps,
                                                                 final double radius) {
    List<Cluster<D>> clusters = new ArrayList<>();
    int clusterCount = 0;
    int mapping[] = new int[dps.size()];
    Arrays.fill(mapping, EMPTY);

    // 1. Filter all the dps into candites (pairs with less than radius distance)
    List<Candidate> candidates = candidates(dps, radius);

    // 2. Sort candidates by distance
    Collections.sort(candidates);

    // 3. Build cluster by maitaining the average radius bellow the threshold
    for(int i = 0; i < candidates.size(); i++) {
      Candidate c = candidates.get(i);
      D dp1 = dps.get(c.i), dp2 = dps.get(c.j);
      Cluster<D> c1 = null;

      if (mapping[c.i] == EMPTY) {
        c1 = new Cluster<D>(dp1);
        clusters.add(c1);
        mapping[c.i] = clusterCount++;
      } else {
        c1 = clusters.get(mapping[c.i]);
      }

      if (mapping[c.j] == EMPTY) {
        double cRadius = c1.avgRadius(dp2);
        if (cRadius <= radius) {
          c1.add(dp2);
          mapping[c.j] = mapping[c.i];
        }
      } else {
        //Lazy clean, mark the cluster as empty
        if (mapping[c.i] != mapping[c.j]) {
          Cluster<D> c2 = clusters.get(mapping[c.j]);
          double cRadius = c1.avgRadius(c2);
          if (cRadius < radius) {
            c1.addAll(c2);
            int clusterIdx = mapping[c.j];
            ArrayUtils.replace(mapping, clusterIdx, mapping[c.i]);
            clusters.get(clusterIdx).clear();
          }
        }
      }
    }

    // 4. Drop clusters that are empty
    List<Cluster<D>> rv = new ArrayList<>();
    for(int i = 0; i < clusters.size(); i++) {
      Cluster<D> cluster = clusters.get(i);
      if(cluster.size() > 0) {
        rv.add(cluster);
      }
    }

    return rv;
  }
/*
  for (int i = 0; i < dps.size() - 1; i++) {
    D dp1 = dps.get(i);
    Cluster<D> c1 = null;

    if (mapping[i] == -1) {
      c1 = new Cluster<D>(dp1);
      clusters.add(c1);
      mapping[i] = clusterCount++;
    } else {
      c1 = clusters.get(mapping[i]);
    }

    List<Integer> neighbors = neighbors(dps, i, eps);

    for (int j = 0; j < neighbors.size(); j++) {
      D dp2 = dps.get(neighbors.get(j));
      if (mapping[neighbors.get(j)] == -1) {
        double cRadius = c1.maxRadius(dp2);
        if (cRadius <= radius) {
          c1.add(dp2);
          mapping[neighbors.get(j)] = mapping[i];
        }
      } else {
        //Lazy clean, mark the cluster as empty
        if(mapping[i] != mapping[neighbors.get(j)]){
          Cluster<D> c2 = clusters.get(mapping[neighbors.get(j)]);
          double cRadius = c1.maxRadius(c2);
          if(cRadius < radius) {
            c1.addAll(c2);
            int clusterIdx = mapping[neighbors.get(j)];
            ArrayUtils.replace(mapping, clusterIdx, mapping[i]);
            clusters.get(clusterIdx).clear();
          }
        }
      }
    }
  }*/


  /**
   *
   */
  private static class Candidate implements Comparable<Candidate>{
    protected final int i, j;
    protected final double d;

    /**
     *
     * @param i
     * @param j
     * @param d
     */
    public Candidate(final int i, final int j, final double d) {
      this.i = i;
      this.j = j;
      this.d = d;
    }

    @Override
    public int compareTo(Candidate o) {
      return Double.compare(d, o.d);
    }

    @Override
    public boolean equals(Object o) {
      boolean rv = false;

      if (this == o) {
        rv = true;
      } else if (o == null) {
        rv = false;
      } else if (getClass() != o.getClass())
        rv = false;
      else {
        Candidate c = (Candidate) o;
        rv = i==c.i && j == c.j && d == c.d;
      }

      return rv;
    }

    @Override
    public int hashCode() {
      long ld = Double.doubleToLongBits(d);
      int rv = 3, prime = 97, c = i + j + (int)(ld ^ (ld >>> 32));
      return prime * rv + c;
    }
  }
}
