package pt.ua.it.atnog.ml.utils.structures;

import java.util.Comparator;

import pt.ua.it.atnog.ml.clustering.KDElement;

public class KDElementComparator implements Comparator<KDElement> {
	private int dim;

	public KDElementComparator(int dim) {
		this.dim = dim;
	}

	public int compare(KDElement e1, KDElement e2) {
		return Double.compare(e1.coor(dim), e2.coor(dim));
	}
}
