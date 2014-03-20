package pt.ua.it.atnog.ml.optimization.genetic;

import pt.ua.it.atnog.ml.utils.workers.Parallel;
import pt.ua.it.atnog.ml.utils.workers.Worker;

public class FitnessWorker extends Worker {	
	@Override
	public Parallel process(Parallel in) {
		Chromosome chromosome = (Chromosome) in;
		chromosome.fitness();
		return in;
	}

	@Override
	public Worker clone() {
		return new FitnessWorker();
	}
}