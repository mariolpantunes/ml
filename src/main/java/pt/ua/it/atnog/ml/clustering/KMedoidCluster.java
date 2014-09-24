package pt.ua.it.atnog.ml.clustering;

public class KMedoidCluster<T extends Element> extends Cluster<T> {
	protected T medoid;

	public KMedoidCluster(T e) {
		super(e);
		medoid = e;
	}

	public double distance(T e) {
		return medoid.euclideanDistance(e);
	}

	public T medoid() {
		return medoid;
	}

	public void updateMedoid() {
		medoid = center();
	}
}
