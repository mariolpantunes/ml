package pt.ua.it.atnog.ml.clustering;

import pt.ua.it.atnog.utils.structures.Point;

public abstract class Element extends Point{
    protected Cluster<?> cluster;
    protected int votes;

    public Element() {
	cluster = null;
    }

    public Cluster<?> cluster() {
	return cluster;
    }

    public boolean used() {
	return cluster != null;
    }

    public abstract double distance(Element e);
}