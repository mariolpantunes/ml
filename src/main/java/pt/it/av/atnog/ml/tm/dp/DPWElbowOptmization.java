package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.utils.bla.Vector;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Implements a Distributional Profile optimizer based on Elbow method.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWElbowOptmization implements DPWOptimization {
    private final int min;

    /**
     *
     * @param min
     */
    public DPWElbowOptmization(final int min) {
        this.min = min;
    }

    @Override
    public List<DPW.DpDimension> optimize(List<DPW.DpDimension> dpDimensions) {
        if (dpDimensions.size() > min) {
            Comparator<DPW.DpDimension> c = (DPW.DpDimension a, DPW.DpDimension b) -> (Double.compare(b.value, a.value));
            Collections.sort(dpDimensions, c);
            Vector v = new Vector(dpDimensions.size());
            int i = 0;
            for (DPW.DpDimension p : dpDimensions)
                v.set(i++, p.value);
            double t = v.elbow();
            dpDimensions.removeIf(p -> p.value < t);
        }
        return dpDimensions;
    }
}
