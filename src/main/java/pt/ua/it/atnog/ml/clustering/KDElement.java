package pt.ua.it.atnog.ml.clustering;

public abstract class KDElement extends Element {
    private int maxDim;

    public KDElement(int maxDim) {
	this.maxDim = maxDim;
    }

    public int maxDim() {
	return maxDim;
    }

    public abstract double coor(int dim);

    public boolean equals(Object o) {
	boolean rv = false;
	if (o != null && o instanceof KDElement) {
	    KDElement e = (KDElement) o;
	    if (maxDim == e.maxDim) {
		rv = true;
		for (int i = 0; i < e.maxDim && rv; i++)
		    if (coor(i) != e.coor(i))
			rv = false;
	    }
	}
	return rv;
    }
}
