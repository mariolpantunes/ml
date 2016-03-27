package pt.it.av.atnog.ml.clustering;

/**
 * Thin wrapper for elements used in clustering algorithms.
 */
public class Element<T extends Distance> implements Distance<Element<T>> {
    private final T d;
    private Cluster c = null;

    /**
     *
     * @param d
     */
    public Element(T d) {
        this.d = d;
    }

    /**
     *
     * @param c
     */
    public void setCluster(Cluster c) {
        this.c = c;
    }

    /**
     *
     * @return
     */
    public boolean used() {
        return c != null;
    }

    /**
     *
     * @return
     */
    public Cluster cluster() {
        return c;
    }

    public T element() {
        return d;
    }

    @Override
    public double distance(Element<T> e) {
        return d.distance(e.d);
    }

    @Override
    public String toString() {
        return d.toString();
    }

    @Override
    public final boolean equals (Object o) {
        boolean rv = false;
        if (o != null)
            if (o == this)
                rv = true;
        return rv;
    }

    @Override
    public final int hashCode() {
        return System.identityHashCode(this);
    }
}
