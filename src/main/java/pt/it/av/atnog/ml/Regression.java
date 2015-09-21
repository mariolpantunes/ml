package pt.it.av.atnog.ml;

import pt.it.av.atnog.ml.optimization.Optimization;
import pt.it.av.atnog.utils.bla.Matrix;
import pt.it.av.atnog.utils.bla.Vector;


/**
 * Created by mantunes on 9/14/15.
 */
public class Regression {

    public static Vector linearRegression(final Matrix X, final Vector y) {
        Matrix XC = X.addColumn(0, 1.0);
        Vector theta = new Vector(XC.columns());
        return Optimization.ADAM(theta, (Vector t) -> {return XC.transpose().mul(XC.mul(t).uSub(y)).div(XC.rows());});
    }
}
