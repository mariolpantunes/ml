package pt.ua.it.atnog.ml.clustering;

public abstract class Element {
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