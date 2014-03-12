package pt.ua.it.atnog.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Cluster {
	List<Element> elements = new ArrayList<Element>();

	public Cluster(Element e) {
		e.cluster(this);
		elements.add(e);
	}

	public void add(Element e) {
		e.cluster(this);
		elements.add(e);
	}

	public void remove(Element e) {
		elements.remove(e);
	}

	public void remove(int i) {
		if (i >= 0 && i < elements.size()) {
			elements.remove(i);
		}
	}

	public int size() {
		return elements.size();
	}

	public Element at(int i) {
		return elements.get(i);
	}

	protected double t(int n) {
		return (n * n - 1) / 2.0;
	}

	protected double icd(List<Element> l) {
		double rv = 0.0;

		for (int i = 0; i < l.size() - 1; i++)
			for (int j = i + 1; j < l.size(); j++)
				rv += l.get(i).distance(l.get(j));

		if(rv > 0.0)
			rv = rv / t(l.size());
		
		return rv;
	}

	public double icd() {
		return icd(elements);
	}

	public Element center() {
		Double[] dist = new Double[elements.size()];
		Arrays.fill(dist, 0.0);

		for (int i = 0; i < elements.size() - 1; i++)
			for (int j = i + 1; j < elements.size(); j++) {
				double d = elements.get(i).distance(elements.get(j));
				dist[i] += d;
				dist[j] += d;
			}

		List<Double> values = Arrays.asList(dist);
		int minIndex = values.indexOf(Collections.min(values));
		return elements.get(minIndex);
	}
}