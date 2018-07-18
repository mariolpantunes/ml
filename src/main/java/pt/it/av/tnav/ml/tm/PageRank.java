package pt.it.av.tnav.ml.tm;

import pt.it.av.tnav.utils.bla.Matrix;
import pt.it.av.tnav.utils.bla.Vector;

/**
 * Created by mantunes on 16/02/2015.
 */
public class PageRank {
    //TODO: Improved this implementation
    public static void pageRank(Matrix M, double bheta) {
        Vector v_old = Vector.ones(M.rows());

        //System.out.println(v_old);
        //System.out.println((double)1/(double)M.rows());

        Matrix R = v_old.outerProduct(v_old).mul((double) 1 / (double) M.rows()).mul(1.0-bheta);

        //System.out.println("R:\n" + R);

        //Matrix A = M.mul(bheta).add((1.0/M.rows())*(1.0-bheta));
        Matrix A = M;
        //System.out.println(M);
        System.out.println(A);

        //System.out.println(v_old);
        //v_old.uDiv(M.rows());
        System.out.println(v_old);

        boolean done = false;
        Vector v_new = null;

        int i = 0;
        while(!done) {
            v_new = A.mul(v_old);
            System.out.println("Iter: "+i);
            System.out.println(v_new);
            if(v_new.manhattanDistance(v_old) < 0.005) {
                done = true;
            } else {
                v_old = v_new;
            }
            i++;
        }
        System.out.println(v_new);
    }

}
