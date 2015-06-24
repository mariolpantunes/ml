package pt.it.av.atnog.ml.optimization.genetic.termination;

import pt.it.av.atnog.ml.optimization.genetic.Chromosome;

import java.util.List;

/**
 * Created by mantunes on 6/22/15.
 */
public class IterationTermination implements Termination{
    private final int it;

    public IterationTermination(int it) {
        this.it = it;
    }

    @Override
    public boolean finished(int iteration, List<Chromosome> population) {
        return iteration >= it;
    }
}
