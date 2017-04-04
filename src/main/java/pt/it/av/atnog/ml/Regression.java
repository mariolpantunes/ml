package pt.it.av.atnog.ml;

import pt.it.av.atnog.ml.optimization.Optimization;
import pt.it.av.atnog.utils.bla.Matrix;
import pt.it.av.atnog.utils.bla.Vector;


/**
 * Test code for regression
 * TODO: do something usefull with this...
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Regression {

    public static Vector linearRegression(final Matrix X, final Vector y) {
        Matrix XC = X.addColumn(0, 1.0);
        Vector theta = new Vector(XC.columns());
        return Optimization.ADAM(theta, (Vector t) -> {return XC.transpose().mul(XC.mul(t).uSub(y)).div(XC.rows());});
    }
}
