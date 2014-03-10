package pt.ua.it.atnog.ml.optimization.genetic;

import pt.ua.it.atnog.ml.optimization.genetic.selection.Pair;

public abstract class Chromosome implements Comparable<Chromosome> {

	public abstract double fitness();

	public abstract Pair<Chromosome> crossover(Chromosome parent);

	public abstract void mutation();

	public int compareTo(Chromosome o) {
		return Double.compare(o.fitness(), this.fitness());
	}
}
