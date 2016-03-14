package pt.it.av.atnog.ml.clustering;

/**
 * Thin wrapper for elements used in clustering algorithms.
 */
public class Element<T extends Distance> implements Distance {
    private final Distance d;
    private Cluster c = null;

    public Element(Distance d) {
        this.d = d;
    }

    public void setCluster(Cluster c) {
        this.c = c;
    }

    public boolean used() {
        return c != null;
    }

    public Cluster cluster() {
        return c;
    }

    @Override
    public double distance(Distance d) {
        double rv = 0;
        if(d instanceof Element)
            rv = this.d.distance(((Element)d).d);
        return rv;
    }

    @Override
    public String toString() {
        return d.toString();
    }
}
