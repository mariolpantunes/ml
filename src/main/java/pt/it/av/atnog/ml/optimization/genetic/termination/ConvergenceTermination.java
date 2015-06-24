package pt.it.av.atnog.ml.optimization.genetic.termination;

import pt.it.av.atnog.ml.optimization.genetic.Chromosome;

import java.util.Collections;
import java.util.List;


public class ConvergenceTermination implements Termination {
    private double fitness;
    private int convergenceCount;
    private final int convergenceGenerations;

    public ConvergenceTermination(int convergenceGenerations) {
        this.convergenceGenerations = convergenceGenerations;
        fitness = 0.0;
        convergenceCount = 0;
    }

    public boolean finished(int iteration, List<Chromosome> population) {
        Chromosome bs = Collections.max(population);

        if(bs.fitness() != fitness) {
            fitness = bs.fitness();
            convergenceCount = 0;
        }

        if (fitness == bs.fitness())
            convergenceCount += 1;
        else
            convergenceCount = 0;

        return convergenceCount == convergenceGenerations;
    }
}
