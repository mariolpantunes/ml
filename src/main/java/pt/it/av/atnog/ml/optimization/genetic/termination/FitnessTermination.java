package pt.it.av.atnog.ml.optimization.genetic.termination;

import java.util.Collections;
import java.util.List;

import pt.it.av.atnog.ml.optimization.genetic.Chromosome;


public class FitnessTermination implements Termination {
	private final double targetFitness;

	public FitnessTermination(double targetFitness) {
		this.targetFitness = targetFitness;
	}

	public boolean finished(int iteration, List<Chromosome> population) {
		Chromosome bs = Collections.min(population);
		return bs.fitness() >= targetFitness;
	}
}
