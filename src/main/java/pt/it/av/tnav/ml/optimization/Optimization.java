package pt.it.av.tnav.ml.optimization;

import pt.it.av.tnav.utils.bla.Vector;
import pt.it.av.tnav.ml.optimization.genetic.Chromosome;
import pt.it.av.tnav.ml.optimization.genetic.selection.Selection;
import pt.it.av.tnav.ml.optimization.genetic.termination.Termination;
import pt.it.av.tnav.ml.optimization.gradienDescent.Gradient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mantunes on 11/27/14.
 */
public class Optimization {
    //TODO: Make them per algorithm
    private static final double ALPHA = 0.1, E = 1e-8;

    /**
     *
     * @param iTheta
     * @param alpha
     * @param g
     * @return
     */
    public static Vector GD(Vector iTheta, Gradient g, double alpha) {
        /*Vector theta = iTheta;
        int i = 0;
        do {
            i++;
            iTheta = theta;
            Vector delta = g.delta(theta);
            theta = theta.sub(delta.mul(alpha));
            Vector d = g.delta(theta);
            double grad_norm = d.norm(2);
            if(grad_norm < 1e-4)
                break;
        } while(!theta.equals(iTheta));
        System.err.println("I: "+i);
        return theta;*/
        return null;
    }

    public static Vector GD(Vector iTheta, Gradient g) {
        return GD(iTheta, g, ALPHA);
    }

    public static Vector ADAM(Vector iTheta, Gradient g) {


        //TODO:
        //if(Math.pow(1.0 - b1,2.0) > Math.sqrt(1.0-b2))



        return null;
    }

    /*public static Vector ADAGRAD(Vector iTheta, Gradient g) {
        Vector theta = iTheta, h = new Vector(iTheta.size());
        int i = 0;
        do {
            i++;
            iTheta = theta;
            Vector delta = g.delta(theta);
            h.uAdd(delta.power(2.0));
            delta = delta.div(h.add(E).power(0.5));
            theta = theta.sub(delta.mul(ALPHA));
            Vector d = g.delta(theta);
            double grad_norm = d.norm(2);
            if(grad_norm < 1e-3)
                break;
        } while(!theta.equals(iTheta));
        System.err.println("I: "+i);
        return theta;
    }*/

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
