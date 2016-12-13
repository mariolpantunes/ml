package pt.it.av.atnog.ml.clustering;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: MinDist uses a KD-Tree, this algorithm requires the position of the object and not only the Distance between elements
 *
 * @param <E>
 */
/*public class MinDistCluster<E extends Element> extends Cluster<E> {
    private double icd;

    public MinDistCluster(E e) {
        super(e);
        icd = 0.0;
    }

    @Override
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
    }
}*/
