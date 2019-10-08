package pt.it.av.tnav.ml.optimization.genetic.termination;

import java.util.List;

import pt.it.av.tnav.ml.optimization.genetic.Chromosome;

public interface Termination {
	boolean finished(int iteration, List<Chromosome> population);
}
