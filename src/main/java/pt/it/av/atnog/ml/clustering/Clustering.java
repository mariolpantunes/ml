package pt.it.av.atnog.ml.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.it.av.atnog.utils.Utils;
import pt.it.av.atnog.utils.structures.KDTree;

public class Clustering {
    public static <T extends Element> List<Cluster<T>> fastMinDist(
	    List<T> elements, double d, double t) {
	List<Cluster<T>> clusters = new ArrayList<Cluster<T>>();
	KDTree<T> tree = KDTree.build(elements);

	for (int i = 0; i < elements.size() - 1; i++) {
	    T e1 = elements.get(i);
	    tree.remove(elements.get(i));
	    List<T> closer = tree.atMaxDist(e1, d);
	    if (e1.cluster == null && closer.size() == 0) {
		MinDistCluster<T> cluster = new MinDistCluster<T>(e1);
		clusters.add(cluster);
	    } else {

		for (int j = 0; j < closer.size(); j++) {
		    T e2 = closer.get(j);
		    MinDistCluster<T> c1 = Utils.cast(e1.cluster), c2 = Utils
			    .cast(e2.cluster);
		    if (c1 == null && c2 == null) {
			MinDistCluster<T> cluster = new MinDistCluster<T>(e1);
			cluster.add(e2);
			clusters.add(cluster);
		    } else if (c1 == null && c2 != null) {
			double ICD = c2.icdWith(e1);
			if (ICD < t)
			    c2.add(e1);
		    } else if (c1 != null && c2 == null) {
			double ICD = c1.icdWith(e2);
			if (ICD < t)
			    c1.add(e2);
		    } else if (c1 != c2) {
			double ICD = c1.icdWith(c2);
			if (ICD < t) {
			    clusters.remove(c2);
			    c1.addAll(c2);
			}
		    }

		}
	    }
	}
	return clusters;
    }

    public static <T extends Element> List<Cluster<T>> minDist(
	    List<T> elements, double d, double t) {

	List<Cluster<T>> clusters = new ArrayList<Cluster<T>>();

	for (int i = 0; i < elements.size() - 1; i++) {
	    for (int j = i + 1; j < elements.size(); j++) {
		double distance = elements.get(i).distance(elements.get(j));
		if (distance < d) {
		    T e1 = elements.get(i), e2 = elements.get(j);
		    MinDistCluster<T> c1 = Utils.cast(e1.cluster), c2 = Utils
			    .cast(e2.cluster);

		    if (c1 == null && c2 == null) {
			MinDistCluster<T> cluster = new MinDistCluster<T>(e1);
			cluster.add(e2);
			clusters.add(cluster);
		    } else if (c1 == null && c2 != null) {
			double ICD = c2.icdWith(e1);
			if (ICD < t)
			    c2.add(e1);
		    } else if (c1 != null && c2 == null) {
			double ICD = c1.icdWith(e2);
			if (ICD < t)
			    c1.add(e2);
		    } else if (c1 != c2) {
			double ICD = c1.icdWith(c2);
			if (ICD < t) {
			    clusters.remove(c2);
			    c1.addAll(c2);
			}
		    }
		}

	    }
	}

	for (T e : elements) {
	    if (!e.used()) {
		clusters.add(new MinDistCluster<T>(e));
	    }
	}

	return clusters;
    }

    public static <T extends Element> List<Cluster<T>> kMedoid(
	    List<T> elements, int k) {
	List<Cluster<T>> clusters = initKMedoidsClusters(elements, k);
	boolean done = false;
	while (!done) {
	    done = true;
	    for (T e : elements) {
		Cluster<T> original = Utils.cast(e.cluster());
		Cluster<T> closest = closestCluster(e, clusters);
		if (original != closest) {
		    original.remove(e);
		    closest.add(e);
		    e.cluster = closest;
		    done = false;
		}
	    }

	    for (Cluster<T> c : clusters)
		((KMedoidCluster<T>) c).updateMedoid();
	}

	return clusters;
    }

    private static <T extends Element> Cluster<T> closestCluster(T element,
	    List<Cluster<T>> clusters) {
	double minDist;
	Cluster<T> rv;

	if (element.cluster() != null && clusters.contains(element.cluster())) {
	    KMedoidCluster<T> c = Utils.cast(element.cluster());
	    minDist = c.distance(element);
	    rv = Utils.cast(element.cluster());
	} else {
	    minDist = ((KMedoidCluster<T>) clusters.get(0)).distance(element);
	    rv = clusters.get(0);
	}

	for (Cluster<T> c : clusters) {
	    double tmp = ((KMedoidCluster<T>) c).distance(element);
	    if (tmp < minDist) {
		minDist = tmp;
		rv = c;
	    }
	}

	return rv;
    }

    private static <T extends Element> List<Cluster<T>> initKMedoidsClusters(
	    List<T> elements, int k) {
	List<Cluster<T>> clusters = new ArrayList<Cluster<T>>(k);
	Collections.shuffle(elements);

	for (int i = 0; i < k; i++) {
	    KMedoidCluster<T> cluster = new KMedoidCluster<T>(elements.get(i));
	    clusters.add(cluster);
	}

	for (T e : elements) {
	    Cluster<T> cluster = closestCluster(e, clusters);
	    cluster.add(e);
	}

	for (Cluster<T> c : clusters)
	    ((KMedoidCluster<T>) c).updateMedoid();

	return clusters;
    }
}
