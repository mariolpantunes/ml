package pt.it.av.atnog.ml.optimization.genetic.termination;

import java.util.Collections;
import java.util.List;

import pt.it.av.atnog.ml.optimization.genetic.Chromosome;


public class FitnessTermination extends Termination {

	private final double targetFitness;

	public FitnessTermination(double targetFitness, int numberGenerations) {
		super(numberGenerations);
		this.targetFitness = targetFitness;
	}

	public boolean termination(List<? extends Chromosome> population) {
		boolean ended = super.termination(population);

		if (!ended) {

			Chromosome bestSolution = Collections.min(population);

			if (bestSolution.fitness() >= targetFitness) {
				ended = true;
			}
		}
		return ended;
	}
}
