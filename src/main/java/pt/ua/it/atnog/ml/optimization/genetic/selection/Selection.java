package pt.ua.it.atnog.ml.optimization.genetic.selection;

import java.util.List;

import pt.ua.it.atnog.ml.optimization.genetic.Chromosome;

public interface Selection {
	public void select(List<Chromosome> population, List<Chromosome> offspring);
}
