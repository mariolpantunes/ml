package pt.ua.it.atnog.ml.optimization.genetic.selection;

import java.util.Collections;
import java.util.List;

import pt.ua.it.atnog.ml.optimization.genetic.Chromosome;

public class TournamentSelection implements Selection {
	private Chromosome array[];

	public TournamentSelection() {
		array = new Chromosome[3];
	}

	private void swap(Chromosome array[], int i, int j) {
		Chromosome t = array[i];
		array[i] = array[j];
		array[j] = t;
	}

	private void sort(Chromosome array[]) {
		if (array[0].fitness() < array[1].fitness())
			swap(array, 0, 1);
		if (array[1].fitness() < array[2].fitness())
			swap(array, 1, 2);
		if (array[0].fitness() < array[1].fitness())
			swap(array, 0, 1);
	}

	public void select(List<? extends Chromosome> population, List<Chromosome> offspring) {
		int size = (population.size() / 3) * 3;
		for (int r = 0; r < 2; r++) {
			Collections.shuffle(population);
			for (int i = 0; i < size; i += 3) {
				array[0] = population.get(i);
				array[1] = population.get(i + 1);
				array[2] = population.get(i + 2);
				sort(array);
				offspring.add(array[0].crossover(array[1]));
			}
		}
	}
}
