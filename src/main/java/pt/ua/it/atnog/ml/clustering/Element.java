package pt.ua.it.atnog.ml.clustering;

import pt.ua.it.atnog.utils.structures.Point;

public class Element extends Point {
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