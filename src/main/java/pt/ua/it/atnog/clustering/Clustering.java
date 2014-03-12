package pt.ua.it.atnog.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * else if (c1 != c2) { logger.info("Both points are in a cluster");
 * logger.info(c1.toString()); logger.info(c2.toString());
 * 
 * double ICD = c1.icdWith(c2); logger.info("After ICD...");
 * logger.info(c1.toString()); logger.info(c2.toString());
 * 
 * 
 * if (ICD < t) { logger.info("Merge clusters...");
 * clusters.remove(c2); logger.info("Remove..."); c1.addAll(c2); }
 * 
 * 
 * 
 * else { double ICD_C1 = c1.icd(), ICD_C2 = c2.icd(), ICD_C1_M_E1 =
 * c1 .icdWithout(e1), ICD_C2_M_E2 = c2.icdWithout(e2), ICD_C1_P_E2
 * = c1 .icdWith(e2), ICD_C2_P_E1 = c2.icdWith(e1);
 * 
 * double gainC1 = ICD_C1 - ICD_C1_M_E1 + ICD_C2 - ICD_C2_P_E1,
 * gainC2 = ICD_C2 - ICD_C2_M_E2 + ICD_C1 - ICD_C1_P_E2;
 * 
 * if (gainC1 > gainC2 && gainC1 > 0.0) { c1.remove(e1); c2.add(e1);
 * } else if (gainC2 > 0.0) { c2.remove(e2); c1.add(e2); } }
 */

public class Clustering {
	public static List<Cluster> minDist(List<? extends Element> elements,
			double d, double t) {
		List<Cluster> clusters = new ArrayList<Cluster>();
		List<MinDistPair> pairs = minDistPairs(elements, d);
		Collections.sort(pairs);

		for (MinDistPair p : pairs) {
			Element e1 = p.e1, e2 = p.e2;
			MinDistCluster c1 = (MinDistCluster) e1.cluster, c2 = (MinDistCluster) e2.cluster;

			if (c1 == null && c2 == null) {
				MinDistCluster cluster = new MinDistCluster(e1);
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
			} else if(c1 != c2) {
				double ICD = c1.icdWith(c2);
				if(ICD < t) {
					 clusters.remove(c2);
					 c1.addAll(c2);
				}
			}
		}

		for (Element e : elements) {
			if (!e.used()) {
				clusters.add(new MinDistCluster(e));
			}
		}

		return clusters;
	}

	private static List<MinDistPair> minDistPairs(
			List<? extends Element> elements, double d) {
		List<MinDistPair> pairs = new ArrayList<MinDistPair>();

		for (int i = 0; i < elements.size() - 1; i++)
			for (int j = i + 1; j < elements.size(); j++) {
				double distance = elements.get(i).distance(elements.get(j));
				if (distance < d)
					pairs.add(new MinDistPair(elements.get(i), elements.get(j),
							distance));
			}

		return pairs;
	}
	
	public static List<Cluster> kMedoid(List<? extends Element> elements, int k) {
		List<Cluster> clusters = initKMedoidsClusters(elements, k);
		boolean done = false;
		while (!done) {
			done = true;
			for (Element e : elements) {
				Cluster original = e.cluster();
				Cluster closest = closestCluster(e, clusters);
				if (original != closest) {
					original.remove(e);
					closest.add(e);
					e.cluster(closest);
					done = false;
				}
			}
			for (Cluster c : clusters)
				((KMedoidCluster) c).updateMedoid();
		}

		return clusters;
	}

	private static Cluster closestCluster(Element element,
			List<Cluster> clusters) {
		double minDist;
		Cluster rv;

		if (element.cluster() != null && clusters.contains(element.cluster())) {
			minDist = ((KMedoidCluster) element.cluster()).distance(element);
			rv = element.cluster();
		} else {
			minDist = ((KMedoidCluster) clusters.get(0)).distance(element);
			rv = clusters.get(0);
		}

		for (Cluster c : clusters) {
			double tmp = ((KMedoidCluster) c).distance(element);
			if (tmp < minDist) {
				minDist = tmp;
				rv = c;
			}
		}

		return rv;
	}

	private static List<Cluster> initKMedoidsClusters(List<? extends Element> elements,
			int k) {
		List<Cluster> clusters = new ArrayList<Cluster>(k);
		Collections.shuffle(elements);

		for (int i = 0; i < k; i++) {
			KMedoidCluster cluster = new KMedoidCluster(elements.get(i));
			clusters.add(cluster);
		}

		for (Element e : elements) {
			Cluster cluster = closestCluster(e, clusters);
			cluster.add(e);
		}

		for (Cluster c : clusters)
			((KMedoidCluster) c).updateMedoid();

		return clusters;
	}
}
