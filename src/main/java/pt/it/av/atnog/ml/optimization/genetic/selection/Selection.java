package pt.it.av.atnog.ml.optimization.genetic.selection;

import java.util.List;

import pt.it.av.atnog.ml.optimization.genetic.Chromosome;

public interface Selection {
	void select(List<Chromosome> population, List<Chromosome> offspring);
}
