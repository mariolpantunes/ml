package pt.ua.it.atnog.ml.optimization.genetic.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pt.ua.it.atnog.ml.optimization.genetic.Chromosome;

public class TournamentSelection implements Selection {

	private final int roundSize;
	private final double percentgeWinning;
	private Random random;
	private List<Pair<Chromosome>> matingPool;

	public TournamentSelection(double percentgeWinning) {
		this(2, percentgeWinning);
	}

	public TournamentSelection(int roundSize, double percentgeWinning) {
		this.roundSize = roundSize;
		this.percentgeWinning = percentgeWinning;
		random = new Random();
		matingPool = new ArrayList<Pair<Chromosome>>();
	}

	public List<Pair<Chromosome>> select(List<Chromosome> population) {
		int size = (population.size() / (2 * roundSize)) * (2 * roundSize);
		matingPool.clear();
		Chromosome championRound1, championRound2;

		for (int tournament = 0; tournament < roundSize; tournament++) {
			Collections.shuffle(population);

			for (int i = 0; i < size; i = i + (2 * roundSize)) {

				int j = i + 1;
				championRound1 = population.get(i);
				championRound2 = population.get(i + roundSize);

				for (; j < (i + roundSize); j++) {
					boolean winning = random.nextDouble() >= percentgeWinning;
					if (winning
							&& championRound1.fitness() < population.get(j)
									.fitness()) {
						championRound1 = population.get(j);
					}
				}

				for (; j < (i + (2 * roundSize)); j++) {
					boolean winning = random.nextDouble() >= percentgeWinning;
					if (winning
							&& championRound2.fitness() < population.get(j)
									.fitness()) {
						championRound2 = population.get(j);
					}
				}

				matingPool.add(new Pair<Chromosome>(championRound1,
						championRound2));

			}
		}

		Collections.shuffle(population);

		for (int i = 0; i < population.size() - size; i += 2) {
			matingPool.add(new Pair<Chromosome>(population.get(i), population
					.get(i + 1)));

		}

		return matingPool;
	}
}
