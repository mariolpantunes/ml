package pt.it.av.atnog.ml.optimization.genetic.termination;

import java.util.List;

import pt.it.av.atnog.ml.optimization.genetic.Chromosome;


public interface Termination {
	boolean finished(int iteration, List<Chromosome> population);
}
