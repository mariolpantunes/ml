package pt.ua.it.atnog.ml.optimization.genetic.selection;

public class Pair<F, S> {
	protected F first;
	protected S second;

	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public F first() {
		return first;
	}

	public S second() {
		return second;
	}
}
