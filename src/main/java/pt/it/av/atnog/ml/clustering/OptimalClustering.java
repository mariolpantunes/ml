package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.PrintUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO:
 */
public class OptimalClustering {

    /**
     * @param alg
     * @param min
     * @param max
     */
    public static <T extends Distance<T>> void optimalClustering(Kmeans alg, List<T> objs, int min, int max, int Nd) {
        System.out.println("Size: "+((max-min)+1));

        double fk[] = new double[(max - min) + 1];
        double Sk[] = new double[(max - min) + 1];
        double ak[] = new double[(max - min) + 1];
        List<List<? extends Cluster<Element<T>>>> allClusters = new ArrayList<>();


        for (int k = min, i = 0; k <= max; k++, i++) {
            List<? extends Cluster<Element<T>>> clusters = alg.clustering(objs, i);
            Sk[i] = distortion(clusters);
            fk[i] = f(i, Sk[i], Sk, ak, Nd);
        }

        System.out.println("SK: " + PrintUtils.array(Sk));
        System.out.println("AK: " + PrintUtils.array(ak));
        System.out.println("FK: " + PrintUtils.array(fk));

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
        System.out.println("K: "+(k+1));
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
