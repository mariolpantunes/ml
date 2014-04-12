package pt.ua.it.atnog.ml.clustering;

import java.util.ArrayList;
import java.util.List;

public class MinDistCluster<T extends Element> extends Cluster<T> {
    private double icd;

    public MinDistCluster(T e) {
	super(e);
	icd = 0.0;
    }

    public void add(T e) {
	super.add(e);
	icd = icdWith(e);
    }

    public void addAll(MinDistCluster<T> c) {
	for (T e : c.elements)
	    super.add(e);
	
	icd = icdWith(c);
    }

    public double icdWith(T e) {
	double rv = 0.0;

	for (T el : elements)
	    rv += el.distance(e);

	rv = (icd * t(elements.size()) + rv) / t(elements.size() + 1);

	return rv;
    }

    public double icdWith(Cluster<T> c) {
	List<T> all = new ArrayList<T>(elements.size() + c.elements.size());
	all.addAll(elements);
	all.addAll(c.elements);
	return icd(all);
    }

    public double icdWithout(T e) {
	double rv = 0.0;

	for (T el : elements)
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
