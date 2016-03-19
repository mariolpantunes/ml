package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.PrintUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * K-medoid algorithm implementation
 */
public class Kmedoids implements Kmeans {
    /**
     * @param elements
     * @param k
     * @param <E>
     * @return
     */
    protected <D extends Distance> List<KmedoidCluster<Element<D>>> init(final List<Element<D>> elements, int k) {
        List<KmedoidCluster<Element<D>>> clusters = new ArrayList<>(k);
        Collections.shuffle(elements);
        for (int i = 0; i < k; i++)
            clusters.add(new KmedoidCluster(elements.get(i)));
        return clusters;
    }

    /**
     * @param elements
     * @param clusters
     * @param <T>
     * @return
     */
    protected <D extends Distance> boolean assignment(final List<Element<D>> elements, List<KmedoidCluster<Element<D>>> clusters) {
        boolean rv = false;
        for (Element e : elements) {
            KmedoidCluster c = closestCluster(e, clusters);
            if (c != e.cluster()) {
                rv = true;
                if (e.used())
                    e.cluster().remove(e);
                c.add(e);
            }
        }
        return rv;
    }

    @Override
    public <D extends Distance> List<? extends Cluster<Element<D>>> clustering(List<D> objects, int k) {
        // convert list of Distance objects into Elements
        List<Element<D>> elements = objects.stream().map(u -> new Element<D>(u)).collect(Collectors.toList());
        //System.err.println("Convert list of Distance objects into Elements");

        // init step
        List<KmedoidCluster<Element<D>>> clusters = init(elements, k);
        //System.err.println("Init Step");
        //System.err.println("Clusters: "+ PrintUtils.list(clusters));

        // assignment step
        assignment(elements, clusters);
        //System.err.println("Assignemnt Step");
        //System.err.println("Clusters: "+ PrintUtils.list(clusters));

        boolean done = false;
        while (!done) {
            // update step
            for(KmedoidCluster c : clusters)
                c.updateMedoid();
            //System.err.println("Update Step");
            //System.err.println("Clusters: "+ PrintUtils.list(clusters));
            // assignment step
            done = !assignment(elements, clusters);
            //System.err.println("Assignemnt Step -> "+done);
            //System.err.println("Clusters: "+ PrintUtils.list(clusters));
        }

        return clusters;
    }

    /**
     * @param o
     * @param clusters
     * @param <E>
     * @return
     */
    private <E extends Element> KmedoidCluster closestCluster(E o, List<KmedoidCluster<E>> clusters) {
        double d = 0.0;
        KmedoidCluster rv = null;

        Iterator<KmedoidCluster<E>> it = clusters.iterator();
        if (it.hasNext()) {
            rv = it.next();
            d = rv.medoid().distance(o);
        }

        while (it.hasNext()) {
            KmedoidCluster c = it.next();
            double tmp = c.medoid().distance(o);
            if (tmp < d) {
                d = tmp;
                rv = c;
            }
        }

        return rv;
    }
}
