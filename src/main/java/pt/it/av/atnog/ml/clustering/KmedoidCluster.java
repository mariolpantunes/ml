package pt.it.av.atnog.ml.clustering;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @param <E>
 */
public class KmedoidCluster<E extends Element> extends ArrayList<E> implements Cluster<E> {
    private E medoid;

    /**
     * Constructs a new K-medoid cluster with the first element.
     *
     * @param e the first element of the K-medoid cluster
     */
    public KmedoidCluster(E e) {
        medoid = e;
        add(e);
    }

    @Override
    public boolean add(E e) {
        boolean rv = super.add(e);
        if (rv)
            e.setCluster(this);
        return rv;
    }

    @Override
    public boolean remove(Object o) {
        boolean rv = super.remove(o);
        if (rv)
            ((E) o).setCluster(null);
        return rv;
    }


    /**
     * @return
     */
    public E medoid() {
        return medoid;
    }

    /**
     *
     */
    public void updateMedoid() {
        if (size() > 2) {
            E optimalMedoid = get(0);
            medoid = optimalMedoid;
            double distortion = distortion();

            for (int i = 1; i < size(); i++) {
                medoid = get(i);
                double tmp = distortion();
                if (tmp < distortion) {
                    optimalMedoid = medoid;
                    distortion = tmp;
                }
            }

            medoid = optimalMedoid;
        }
    }

    @Override
    public double distortion() {
        double rv = 0.0;
        for (int i = 0; i < size(); i++)
            rv += Math.pow(medoid.distance(get(i)), 2.0);
        return rv;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<E> it = iterator();

        sb.append("{");
        for (int i = 0; i < size() - 1; i++) {
            E e = get(i);
            if (e == medoid)
                sb.append("*");
            sb.append(e.toString() + "; ");
        }

        E e = get(size() - 1);
        if (e == medoid)
            sb.append("*");
        sb.append(e.toString() + "}");


        return sb.toString();
    }
}
