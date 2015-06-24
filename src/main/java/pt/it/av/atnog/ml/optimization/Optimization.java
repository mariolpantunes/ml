package pt.it.av.atnog.ml.optimization;

import pt.it.av.atnog.ml.optimization.genetic.Chromosome;
import pt.it.av.atnog.ml.optimization.genetic.selection.Selection;
import pt.it.av.atnog.ml.optimization.genetic.termination.Termination;
import pt.it.av.atnog.utils.bla.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mantunes on 11/27/14.
 */
public class Optimization {
    /**
     *
     * @param iTheta
     * @param alpha
     * @param gradient
     * @return
     */
    public static Vector gradientDescent(Vector iTheta, double alpha, Gradient gradient) {
        Vector theta = iTheta;
        int i = 0;
        do {
            i++;
            iTheta = theta;
            Vector delta = gradient.delta(theta);
            theta = theta.sub(delta.mul(alpha));
        } while(!theta.equals(iTheta));
        //System.err.println("I: "+i);
        return theta;
    }

    /**
     *
     * @param pop
     * @param s
     * @param t
     * @param mp
     * @return
     */
    public static List<Chromosome> ga(List<Chromosome> pop, Selection s, Termination t, double mp) {
        List<Chromosome> offspring = new ArrayList<>(pop.size());
        List<Chromosome> newGeneration = new ArrayList<>(pop.size());

        int it = 0;
        while (!t.finished(it, pop)) {
            System.out.println("Selection...");
            s.select(pop, offspring);
            System.out.println("New Generation...");
            if (offspring.size() >= pop.size()) {
                Collections.sort(offspring);
                for (int i = 0; i < pop.size(); i++)
                    newGeneration.add(offspring.get(i));
            } else {
                newGeneration.addAll(offspring);
                int steadyState = pop.size() - offspring.size();
                Collections.sort(pop);
                for (int i = 0; i < steadyState; i++)
                    newGeneration.add(pop.get(i));
            }
            System.out.println("Mutation...");
            int numberMutations = (int) (newGeneration.size() * mp);
            Collections.shuffle(newGeneration);
            for (int i = 0; i < numberMutations; i++)
                newGeneration.get(i).mutation();
            pop.clear();
            pop.addAll(newGeneration);
            offspring.clear();
            newGeneration.clear();
            it++;
            Chromosome bs = Collections.max(pop);
            System.out.println("IT "+it+" FIT "+bs.fitness()+ " -> " + bs.toString());
        }
        return pop;
    }

    //TODO: Take advantage of workerpoll()
    /**
     *
     * @param pop
     * @param s
     * @param t
     * @param mp
     * @return
     */
    public static List<Chromosome> gpga(List<Chromosome> pop, Selection s, Termination t, double mp) {
        List<Chromosome> offspring = new ArrayList<>(pop.size());
        List<Chromosome> newGeneration = new ArrayList<>(pop.size());

        int it = 0;
        while (!t.finished(it, pop)) {
            s.select(pop, offspring);
            if (offspring.size() >= pop.size()) {
                Collections.sort(offspring);
                for (int i = 0; i < pop.size(); i++)
                    newGeneration.add(offspring.get(i));
            } else {
                newGeneration.addAll(offspring);
                int steadyState = pop.size() - offspring.size();
                Collections.sort(pop);
                for (int i = 0; i < steadyState; i++)
                    newGeneration.add(pop.get(i));
            }
            int numberMutations = (int) (newGeneration.size() * mp);
            Collections.shuffle(newGeneration);
            for (int i = 0; i < numberMutations; i++)
                newGeneration.get(i).mutation();
            pop.clear();
            pop.addAll(newGeneration);
            offspring.clear();
            newGeneration.clear();
            it++;
            Chromosome bs = Collections.max(pop);
            //System.out.println("IT "+it+" FIT "+bs.fitness());
        }
        return pop;
    }
}
