package pt.ua.it.atnog.ml.clustering;

public class KMedoidCluster extends Cluster {
	protected Element medoid;

	public KMedoidCluster(Element e) {
		super(e);
		medoid = e;
	}

	public double distance(Element e) {
		return medoid.distance(e);
	}

	public Element medoid() {
		return medoid;
	}

	public void updateMedoid() {
		medoid = center();
	}
}
