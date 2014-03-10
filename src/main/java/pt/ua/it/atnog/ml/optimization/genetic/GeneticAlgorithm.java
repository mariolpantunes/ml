package pt.ua.it.atnog.ml.optimization.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pt.ua.it.atnog.ml.optimization.genetic.selection.Pair;
import pt.ua.it.atnog.ml.optimization.genetic.selection.Selection;
import pt.ua.it.atnog.ml.optimization.genetic.termination.Termination;

public class GeneticAlgorithm {

	private List<Chromosome> population;
	private Selection selection;
	private Termination termination;
	private boolean steadyState;
	private final double percentageSteadyState;
	private final int evenSize;

	public GeneticAlgorithm(List<Chromosome> population, Selection selection,
			Termination termination) {
		this.population = population;
		this.selection = selection;
		this.termination = termination;
		this.steadyState = false;
		this.percentageSteadyState = 0.0;

		evenSize = (population.size() / 2) * 2;
	}

	public GeneticAlgorithm(List<Chromosome> population, Selection selection,
			Termination termination, double percentageSteadyState) {
		this.population = population;
		this.selection = selection;
		this.termination = termination;
		this.steadyState = true;
		this.percentageSteadyState = percentageSteadyState;

		evenSize = (population.size() / 2) * 2;
	}

	public void execute() {
		List<Chromosome> newGeneration = new ArrayList<Chromosome>(evenSize);
		List<Chromosome> newPopulation = new ArrayList<Chromosome>(evenSize);
		List<Pair<Chromosome>> matingPool;

		while (!termination.termination(population)) {

			matingPool = selection.select(population);
			Iterator<Pair<Chromosome>> matingIterator = matingPool.iterator();

			while (matingIterator.hasNext()) {
				Pair<Chromosome> matingPair = matingIterator.next();
				Pair<Chromosome> childs = matingPair.first().crossover(
						matingPair.second());
				newGeneration.add(childs.first());
				newGeneration.add(childs.second());
			}

			if (steadyState) {
				Collections.sort(population);
				Collections.sort(newGeneration);
				int size = (int) Math.round(population.size()
						* percentageSteadyState);
				Iterator<Chromosome> itr = population.iterator();

				for (int i = 0; i < size; i++) {
					Chromosome solution = itr.next();
					solution.mutation();
					newPopulation.add(solution);
				}
				itr = newGeneration.iterator();
				for (int i = 0; i < (newGeneration.size() - size); i++) {
					Chromosome solution = itr.next();
					solution.mutation();
					newPopulation.add(solution);
				}
			} else {
				newPopulation.addAll(newGeneration);
			}
			
			population.clear();
			population.addAll(newPopulation);
			newPopulation.clear();
			newGeneration.clear();
		}
	}

	public Chromosome bestSolution() {
		return Collections.min(population);
	}

	public List<Chromosome> solutions() {
		return population;
	}
}
