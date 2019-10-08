package pt.it.av.tnav.ml.regression;

import pt.it.av.tnav.utils.bla.Matrix;
import pt.it.av.tnav.utils.bla.Vector;
import pt.it.av.tnav.ml.optimization.Optimization;


/**
 * Test code for regression
 * TODO: do something usefull with this...
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Regression {

    public static Vector linearRegression(final Matrix X, final Vector y) {
        /*Matrix XC = X.addColumn(0, 1.0);
        Vector theta = new Vector(XC.columns());
        return Optimization.ADAM(theta, (Vector t) -> {return XC.transpose().mul(XC.mul(t).uSub
        (y)).div(XC.rows());});*/
        return null;
    }
}
