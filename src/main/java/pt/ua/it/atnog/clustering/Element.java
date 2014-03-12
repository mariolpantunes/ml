package pt.ua.it.atnog.clustering;

public abstract class Element {
	protected Cluster cluster;
	protected int votes;

	public Element() {
		cluster = null;
	}

	public Cluster cluster() {
		return cluster;
	}

	public void cluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public boolean used() {
		return cluster != null;
	}

	public abstract double distance(Element e);
}