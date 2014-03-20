package pt.ua.it.atnog.ml.clustering;

public class MinDistPair implements Comparable<MinDistPair> {
	protected Element e1, e2;
	protected double distance;
	private boolean hasDistance;
	private boolean endOfStream;

	public MinDistPair() {
		endOfStream = true;
	}

	public MinDistPair(Element e1, Element e2) {
		this.e1 = e1;
		this.e2 = e2;
		hasDistance = false;
		endOfStream = false;
	}

	public boolean endOfStream() {
		return endOfStream;
	}

	public double distance() {
		if (!hasDistance) {
			distance = e1.distance(e2);
			hasDistance = true;
		}
		return distance;
	}

	public int compareTo(MinDistPair o) {
		int rv = 0;

		if (this != o)
			rv = Double.compare(distance(), o.distance());

		return rv;
	}

	public String toString() {
		return "Pair -> {" + e1 + "; " + e2 + "; " + distance() + "}";
	}
}
