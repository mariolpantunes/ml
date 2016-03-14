package pt.it.av.atnog.ml.clustering;

import java.util.*;


/**
 * Cluster represents a set of elements that are similiar in some way.
 * TODO: Make this class abstract, let each implementation choose a data structure that suit their needs
 */
public interface Cluster<T extends Element> extends Collection<T> {

    /*/**
     * Constructs a new cluster with the first element.
     *
     * @param e the first element of the cluster
     */
   /* public Cluster(T e) {
        add(e);
    }*/

/**


    protected double icd(List<T> l) {
        double rv = 0.0;

        for (int i = 0; i < l.size() - 1; i++)
            for (int j = i + 1; j < l.size(); j++)
                rv += l.get(i).distance(l.get(j));

        if (rv > 0.0)
            rv = rv / t(l.size());

        return rv;
    }

    public double icd() {
        return icd(elements);
    }


    public T center() {
        Double[] dist = new Double[elements.size()];
        Arrays.fill(dist, 0.0);

        for (int i = 0; i < elements.size() - 1; i++)
            for (int j = i + 1; j < elements.size(); j++) {
                double d = elements.get(i).distance(elements.get(j));
                dist[i] += d;
                dist[j] += d;
            }

        List<Double> values = Arrays.asList(dist);
        int minIndex = values.indexOf(Collections.min(values));
        return elements.get(minIndex);
    }*/


    /**
     * TODO:
     * @return
     */
    double distortion();

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator it = iterator();

        sb.append("{");
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext())
                sb.append("; ");
        }
        sb.append("}");

        return sb.toString();
    }*/
}