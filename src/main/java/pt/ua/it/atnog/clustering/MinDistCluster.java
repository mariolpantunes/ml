package pt.ua.it.atnog.clustering;

import java.util.ArrayList;
import java.util.List;

public class MinDistCluster extends Cluster {
	private double icd;

	public MinDistCluster(Element e) {
		super(e);
		icd = 0.0;
	}

	public void add(Element e) {
		icd = icdWith(e);
		super.add(e);
	}

	public void addAll(MinDistCluster c) {
		icd = icdWith(c);
		for (Element e : c.elements)
			super.add(e);
	}

	public double icdWith(Element e) {
		double rv = 0.0;

		for (Element el : elements)
			rv += el.distance(e);

		rv = (icd * t(elements.size()) + rv) / t(elements.size() + 1);

		return rv;
	}

	public double icdWith(MinDistCluster c) {
		List<Element> all = new ArrayList<Element>(elements.size()
				+ c.elements.size());
		all.addAll(elements);
		all.addAll(c.elements);
		return icd(all);
	}

	public double icdWithout(Element e) {
		double rv = 0.0;

		for (Element el : elements)
			if (!el.equals(e))
				rv += el.distance(e);

		rv = (icd * t(elements.size()) + rv) / t(elements.size() + 1);

		return rv;
	}

	public double icd() {
		return icd;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Cluster with " + elements.size() + " elements:\n");
		for (Element e : elements)
			sb.append(" -> " + e + "\n");

		return sb.toString();
	}
}
