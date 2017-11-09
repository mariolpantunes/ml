package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.ArrayUtils;
import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.structures.Distance;
import pt.it.av.atnog.utils.structures.KDTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class FGClustering {

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

  public static <D extends Distance> List<Cluster<D>> clustering(final List<D> dps, final double eps,
                                                                 final double radius) {
    List<Cluster<D>> clusters = new ArrayList<>();
    int clusterCount = 0;
    int mapping[] = new int[dps.size()];
    Arrays.fill(mapping, -1);

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
          double cRadius = c1.radius(dp2);
          if (cRadius <= radius) {
            c1.add(dp2);
            mapping[neighbors.get(j)] = mapping[i];
          }
        } else {
          //Lazy clean, mark the cluster as empty
          if(mapping[i] != mapping[neighbors.get(j)]){
            Cluster<D> c2 = clusters.get(mapping[neighbors.get(j)]);
            double cRadius = c1.radius(c2);
            if(cRadius < radius) {
              c1.addAll(c2);
              int clusterIdx = mapping[neighbors.get(j)];
              ArrayUtils.replace(mapping, clusterIdx, mapping[i]);
              clusters.get(clusterIdx).clear();
            }
          }
        }
      }
    }

    List<Cluster<D>> rv = new ArrayList<>();
    for(int i = 0; i < clusters.size(); i++) {
      Cluster<D> cluster = clusters.get(i);
      if(cluster.size() > 0) {
        rv.add(cluster);
      }
    }

    return rv;
  }


  private <V extends Vector> List<Cluster<V>> FastGreedyClustering(List<V> elements, double d, double t) {
    List<Cluster<V>> clusters = new ArrayList<>();
    KDTree<V> tree = KDTree.build(elements);

    HashMap<V, Cluster> mappings = new HashMap<V, Cluster>();

    for (int i = 0; i < elements.size() - 1; i++) {
      V e1 = elements.get(i);
      tree.remove(elements.get(i));
      List<V> closer = tree.atMaxDist(e1, d);
      if (mappings.containsKey(e1) && closer.size() == 0) {
        Cluster<V> cluster = new Cluster<V>(e1);
        clusters.add(cluster);
        mappings.put(e1, cluster);
      } else {
        for (int j = 0; j < closer.size(); j++) {
          V e2 = closer.get(j);
          Cluster c1 = mappings.get(e1), c2 = mappings.get(e2);
          if (c1 == null && c2 == null) {
            Cluster<V> cluster = new Cluster<V>(e1);
            cluster.add(e2);
            clusters.add(cluster);
            mappings.put(e1, cluster);
            mappings.put(e2, cluster);
          } else if (c1 == null && c2 != null) {
            double ICD = c2.distortion(e1);
            if (ICD < t)
              c2.add(e1);
          } else if (c1 != null && c2 == null) {
            double ICD = c1.distortion(e2);
            if (ICD < t)
              c1.add(e2);
          } else if (c1 != c2) {
            double ICD = c1.distortion(c2);
            if (ICD < t) {
              clusters.remove(c2);
              c1.addAll(c2);
            }
          }
        }
      }
    }

    mappings.clear();
    return clusters;
  }

    /*@Override
    public boolean add(E e) {
        super.add(e);
        icd = icdWith(e);
        return false;
    }

    public void addAll(MinDistCluster<E> c) {
        for (E e : c.elements)
            super.add(e);

        icd = icdWith(c);
    }

    public double icdWith(E e) {
        double rv = 0.0;

        for (E el : elements)
            rv += el.distance(e);

        rv = (icd * t(elements.size()) + rv) / t(elements.size() + 1);

        return rv;
    }

    public double icdWith(Cluster<E> c) {
        List<E> all = new ArrayList<E>(elements.size() + c.elements.size());
        all.addAll(elements);
        all.addAll(c.elements);
        //return distortion(all);
        return 0.0;
    }

    public double icdWithout(E e) {
        double rv = 0.0;

        for (E el : elements)
            if (!el.equals(e))
                rv += el.distance(e);

        rv = (icd * t(elements.size()) + rv) / t(elements.size() + 1);

        return rv;
    }

    public double icd() {
        return icd;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Cluster with " + elements.size() + " elements:\n");
        for (Distance d : elements)
            sb.append(" -> " + d + "\n");

        return sb.toString();
    }

    protected double t(int n) {
        return (n * n - 1) / 2.0;
    }*/
}
