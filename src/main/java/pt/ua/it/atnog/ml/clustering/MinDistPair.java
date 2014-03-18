package pt.ua.it.atnog.ml.clustering;

public class MinDistPair implements Comparable<MinDistPair> {
	protected Element e1, e2;
	protected double dist;

	public MinDistPair(Element e1, Element e2, double dist) {
		this.e1 = e1;
		this.e2 = e2;
		this.dist = dist;
	}

	public int compareTo(MinDistPair o) {
		int rv = 0;

		if (this != o)
			rv = Double.compare(dist, o.dist);

		return rv;
	}

	public String toString() {
		return "Pair -> {" + e1 + "; " + e2 + "; " + dist + "}";
	}
}
