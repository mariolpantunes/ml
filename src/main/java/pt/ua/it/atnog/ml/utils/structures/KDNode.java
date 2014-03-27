package pt.ua.it.atnog.ml.utils.structures;

import pt.ua.it.atnog.ml.clustering.KDElement;

public class KDNode <T extends KDElement>{
	public T data;
	public KDNode<T> left, right;

	public KDNode(T data) {
		this.data = data;
		left = null;
		right = null;
	}
	
	public String toString() {
		return data.toString();
	}
}
