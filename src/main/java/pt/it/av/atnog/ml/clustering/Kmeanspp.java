package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.PrintUtils;
import pt.it.av.atnog.utils.structures.Distance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Kmeans++ implementation.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Kmeanspp extends Kmedoids {
    @Override
    protected <D extends Distance> List<KmedoidCluster<Element<D>>> init(final List<Element<D>> elements, int k) {
        List<KmedoidCluster<Element<D>>> clusters = new ArrayList<>(k);

        if (elements.size() > 0) {
            //Choose an initial center uniformly at random
            Collections.shuffle(elements);
            clusters.add(new KmedoidCluster(elements.get(0)));

            double array[] = new double[elements.size()];

            for (int i = 1; i < k; i++) {
                int j = 0;
                double total = 0.0;
                for (Element<D> e : elements) {
                    if (e.used())
                        array[j] = 0.0;
                    else
                        array[j] = distanceClosestCluster(e, clusters);
                    total += array[j++];
                }

                double rnd = Math.random() * total;

                int idx = -1;
                double v = rnd;
                for (j = 0; j < array.length && idx < 0; j++) {
                    rnd -= array[j];
                    if (rnd < array[j])
                        idx = j;
                }

                if (idx == -1) {
                    System.err.println("TOT " + total);
                    System.err.println("RND " + v);
                    System.err.println("CUR " + rnd);
                    System.err.println(PrintUtils.array(array));
                }

                clusters.add(new KmedoidCluster<Element<D>>(elements.get(idx)));
            }
        }

        return clusters;
    }

    /**
     * This method may suffer from overflow
     *
     * @param e
     * @param clusters
     * @param <D>
     * @return
     */
    private <D extends Distance> double distanceClosestCluster(Element<D> e, List<KmedoidCluster<Element<D>>> clusters) {
        double rv = 0.0;

        Iterator<KmedoidCluster<Element<D>>> it = clusters.iterator();
        if (it.hasNext())
            rv = it.next().medoid().distanceTo(e);

        while (it.hasNext()) {
            KmedoidCluster c = it.next();
            double tmp = c.medoid().distanceTo(e);
            if (tmp < rv)
                rv = tmp;
        }

        return Math.pow(rv, 2.0);
    }
}
