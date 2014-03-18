package pt.ua.it.atnog.ml.optimization.genetic.termination;

import java.util.List;

import pt.ua.it.atnog.ml.optimization.genetic.Chromosome;


public class Termination {

	private int generationsCount;
	private final int numberGenerations;

	public Termination(int numberGenerations) {
		this.numberGenerations = numberGenerations;
		generationsCount = 0;
	}

	public boolean termination(List<? extends Chromosome> population) {
		boolean ended = false;
		if (generationsCount == numberGenerations) {
			ended = true;
		} else {
			generationsCount++;
		}

		return ended;
	}

	public int numberGenerations() {
		return generationsCount;
	}
}
