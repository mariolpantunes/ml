package pt.it.av.atnog.ml;

import pt.it.av.atnog.ml.optimization.Optimization;
import pt.it.av.atnog.ml.optimization.genetic.Chromosome;
import pt.it.av.atnog.ml.optimization.genetic.selection.TournamentSelection;
import pt.it.av.atnog.ml.optimization.genetic.termination.ConvergenceTermination;
import pt.it.av.atnog.utils.bla.Matrix;
import pt.it.av.atnog.utils.bla.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mantunes on 11/27/14.
 */
public class Test {

    public static void main(String[] args) {

        Vector theta = new Vector(2);
        theta = Optimization.GD(theta, (Vector t) -> {
            Vector delta = new Vector(2);
            delta.set(0, 2 * (t.get(0) - 2));
            delta.set(1, 2 * (t.get(1) + 3));
            //delta.set(0,2*t.get(0)+2);
            //delta.set(1,2*t.get(1)+3);
            return delta;
        });
        System.out.println("SGD: "+theta);

        theta = Optimization.ADAGRAD(new Vector(2), (Vector t) -> {
            Vector delta = new Vector(2);
            delta.set(0, 2 * (t.get(0) - 2));
            delta.set(1, 2 * (t.get(1) + 3));
            return delta;
        });
        System.out.println("AdaGrad: " + theta);

        theta = Optimization.ADAM(new Vector(2), (Vector t) -> {Vector delta = new Vector(2);
            delta.set(0, 2*(t.get(0)-2));
            delta.set(1, 2*(t.get(1)+3));
            return delta;});
        System.out.println("ADAM: " + theta);



        /*List<Chromosome> population = new ArrayList<>();
        for(int i = 0; i < 250; i++)
            population.add(new QueenChromosome(8));
        population = Optimization.ga(population, new TournamentSelection(), new ConvergenceTermination(5), 0.25);
        Chromosome bs = Collections.min(population);
        System.out.println("BS "+bs.fitness());*/

        double dataX[] = {0,1,2,3,4,5,6,7,8,9};
        Matrix X = new Matrix(10,1,dataX);
        double dataY[] = {0,1,2,3,4,5,6,7,8,9};
        Vector y = new Vector(dataY);

        System.out.println(X);
        System.out.println(y);

        Vector w = Regression.linearRegression(X, y);
        System.out.println(w);

    }
}
