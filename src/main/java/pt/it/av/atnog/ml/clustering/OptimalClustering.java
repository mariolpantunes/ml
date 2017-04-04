package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.Utils;
import pt.it.av.atnog.utils.structures.Distance;

import java.util.List;

/**
 * Implements Optimal Clustering based on a method present on...
 * TODO: Add paper reference to the documentation...
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class OptimalClustering {

    /**
     *
     * @param alg
     * @param objs
     * @param min
     * @param max
     * @param Nd
     * @param <T>
     * @return
     */
    public static <T extends Distance<T>> List<? extends Cluster<Element<T>>> clustering
            (Kmeans alg, List<T> objs, int min, int max, int Nd) {
        return clustering(alg, objs, min, max, Nd, 5);
    }

    /**
     *
     * @param alg
     * @param objs
     * @param min
     * @param max
     * @param Nd
     * @param reps
     * @param <T>
     * @return
     */
    public static <T extends Distance<T>> List<? extends Cluster<Element<T>>> clustering
    (Kmeans alg, List<T> objs, int min, int max, int Nd, int reps) {

        double fk[] = new double[(max - min) + 1];
        double Sk[] = new double[(max - min) + 1];
        double ak[] = new double[(max - min) + 1];
        List<? extends Cluster<Element<T>>> allClusters[] = new List[(max - min) + 1];


        for (int k = min, i = 0; k <= max; k++, i++) {
            List<? extends Cluster<Element<T>>> clusters = null;
            double distortion = Double.MAX_VALUE;

            if(k < objs.size()) {
                clusters = alg.clustering(objs, k);
                distortion = distortion(clusters);

                for (int j = 1; j < reps; j++) {
                    List<? extends Cluster<Element<T>>> currentClusters = alg.clustering(objs, k);
                    double currentDistortion = distortion(currentClusters);
                    if (currentDistortion < distortion) {
                        clusters = currentClusters;
                        distortion = currentDistortion;
                    }
                }
            }

            Sk[i] = distortion;
            fk[i] = f(i, Sk[i], Sk, ak, Nd);
            allClusters[i] = clusters;
        }

        //System.out.println("SK: " + PrintUtils.array(Sk));
        //System.out.println("AK: " + PrintUtils.array(ak));
        //System.out.println("FK: " + PrintUtils.array(fk));

        int idx = Utils.min(fk);
        return allClusters[idx];
    }

    /**
     * @param clusters
     * @param <T>
     * @return
     */
    private static <T extends Distance<T>> double distortion(List<? extends Cluster<Element<T>>> clusters) {
        double distortion = 0.0;
        for (Cluster c : clusters)
            distortion += c.distortion();
        return distortion;
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
