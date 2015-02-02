package pt.it.av.atnog.ml.optimization.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import pt.it.av.atnog.ml.optimization.genetic.selection.Selection;
import pt.it.av.atnog.ml.optimization.genetic.termination.Termination;
import pt.it.av.atnog.ml.utils.workers.WorkersPoll;

public class GPGA {

	public static List<Chromosome> optimize(List<Chromosome> population,
			Selection selection, Termination termination) {
		return optimize(population, selection, termination, 0.2, 4, null);
	}

	public static List<Chromosome> optimize(List<Chromosome> population,
			Selection selection, Termination termination,
			double mutationProbability, int nWorkers, Logger logger) {
		WorkersPoll workers = new WorkersPoll(new FitnessWorker(), nWorkers);

		workers.runParallel(population);
		workers.done();

		List<Chromosome> offspring = new ArrayList<Chromosome>(
				population.size());
		List<Chromosome> newGeneration = new ArrayList<Chromosome>(
				population.size());

		int it = 0;
		while (!termination.termination(population)) {
			selection.select(population, offspring);
			workers.runParallel(offspring);
			workers.done();
			if (offspring.size() > population.size()) {
				Collections.sort(offspring);
				for (int i = 0; i < population.size(); i++)
					newGeneration.add(offspring.get(i));
			} else {
				int steadyState = population.size() - offspring.size();
				if (steadyState > 0) {
					Collections.sort(population);
					newGeneration.addAll(offspring);
					for (int i = 0; i < steadyState; i++)
						newGeneration.add(population.get(i));
				} else {
					newGeneration.addAll(offspring);
				}
			}
			int numberMutations = (int) (newGeneration.size() * mutationProbability);
			Collections.shuffle(newGeneration);
			for (int i = 0; i < numberMutations; i++)
				newGeneration.get(i).mutation();

			workers.runParallel(newGeneration);
			workers.done();

			population.clear();
			population.addAll(newGeneration);
			offspring.clear();
			newGeneration.clear();

			if (logger != null) {
				Chromosome solution = Collections.min(population);
				logger.info(solution.toString());
			}
			it++;
		}
		workers.close();

		System.out.println("It: "+it);
		return population;
	}
}
