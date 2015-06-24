package pt.it.av.atnog.ml.optimization.genetic.selection;

import java.util.Collections;
import java.util.List;

import pt.it.av.atnog.ml.optimization.genetic.Chromosome;

public class TournamentSelection implements Selection {

    //TODO: check utils...
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

    @Override
    public void select(List<Chromosome> population, List<Chromosome> offspring) {
        Chromosome[] buffer = new Chromosome[3];
        int size = (population.size() / 3) * 3;
        for (int r = 0; r < 2; r++) {
            Collections.shuffle(population);
            for (int i = 0; i < size; i += 3) {
                buffer[0] = population.get(i);
                buffer[1] = population.get(i + 1);
                buffer[2] = population.get(i + 2);
                sort(buffer);
                offspring.add(buffer[0].crossover(buffer[1]));
            }
        }
    }
}
