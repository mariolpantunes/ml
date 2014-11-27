package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.linearAlgebra.Vector;

public class Element extends Vector {
    protected Cluster<?> cluster;
    protected int votes;

    public Element(double coor[]) {
        super(coor);
        cluster = null;
    }

    public Cluster<?> cluster() {
	    return cluster;
    }

    public boolean used() {
	    return cluster != null;
    }

    public double distance(Element e) {
        return euclideanDistance(e);
    }
}