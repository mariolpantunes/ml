package pt.it.av.atnog.ml.clustering;

import java.util.*;

/**
 * Created by mantunes on 3/10/16.
 */
public class KmedoidCluster<E extends Element> extends ArrayList<E> implements Cluster<E>  {
    private E medoid;

    /**
     * Constructs a new K-medoid cluster with the first element.
     *
     * @param e the first element of the K-medoid cluster
     */
    public KmedoidCluster(E e) {
        medoid = e;
    }

    @Override
    public boolean add(E e) {
        boolean rv = super.add(e);
        if(rv)
            e.setCluster(this);
        return rv;
    }

    @Override
    public boolean remove(Object o) {
        boolean rv = super.remove(o);
        if(rv)
            ((E)o).setCluster(null);
        return rv;
    }


    /**
     *
     * @return
     */
    public E medoid() {
        return medoid;
    }

    /**
     *
     */
    public void updateMedoid() {
        E optimalMedoid = null;
        double distortion = 0.0;

        Iterator<E> it = iterator();
        if (it.hasNext()) {
            optimalMedoid = it.next();
            medoid = optimalMedoid;
            distortion = 0;
        }

        while (it.hasNext()) {
            medoid = it.next();
            double tmp = 0;
            if (tmp < distortion) {
                optimalMedoid = medoid;
                distortion = tmp;
            }
        }
    }

    @Override
    public double distortion(){
        double rv = 0.0;
        for(Element e : this)
            rv += Math.pow(medoid.distance(e), 2.0);
        return rv;
    }

    @Override
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
    }
}
