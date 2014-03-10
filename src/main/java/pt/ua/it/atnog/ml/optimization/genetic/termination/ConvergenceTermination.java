package pt.ua.it.atnog.ml.optimization.genetic.termination;

import java.util.Collections;
import java.util.List;

import pt.ua.it.atnog.ml.optimization.genetic.Chromosome;


public class ConvergenceTermination extends Termination {

	private double fitness;
	private int convergenceCount;
	private final int convergenceGenerations;

	public ConvergenceTermination(int convergenceGenerations,
			int numberGenerations) {
		super(numberGenerations);
		this.convergenceGenerations = convergenceGenerations;
		fitness = 0.0;
		convergenceCount = 0;
	}

	public boolean termination(List<Chromosome> population) {
		boolean ended = super.termination(population);
		if (!ended) {
			Chromosome bestSolution = Collections.min(population);

			if (fitness == bestSolution.fitness()) {
				convergenceCount++;
			} else if (bestSolution.fitness() > fitness) {
				convergenceCount = 0;
				fitness = bestSolution.fitness();
			} else {
				convergenceCount = 0;
			}

			if (convergenceCount == convergenceGenerations) {
				ended = true;
			}
		}

		return ended;
	}
}
