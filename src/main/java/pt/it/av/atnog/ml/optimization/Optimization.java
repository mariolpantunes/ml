package pt.it.av.atnog.ml.optimization;

import pt.it.av.atnog.utils.bla.Vector;

/**
 * Created by mantunes on 11/27/14.
 */
public class Optimization {

    public static Vector gradientDescent(Vector iTheta, double alpha, Gradient gradient) {
        Vector theta = iTheta;
        int i = 0;
        do {
            i++;
            iTheta = theta;
            Vector delta = gradient.delta(theta);
            theta = theta.sub(delta.mul(alpha));
        } while(!theta.equals(iTheta));

        System.err.println("I: "+i);
        return theta;
    }
}
