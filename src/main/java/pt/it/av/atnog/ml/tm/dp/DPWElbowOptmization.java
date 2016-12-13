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
    public List<DPW.Coordinate> optimize(List<DPW.Coordinate> coordinates) {
        if (coordinates.size() > min) {
            Comparator<DPW.Coordinate> c = (DPW.Coordinate a, DPW.Coordinate b) -> (Double.compare(b.value, a.value));
            Collections.sort(coordinates, c);
            Vector v = new Vector(coordinates.size());
            int i = 0;
            for (DPW.Coordinate p : coordinates)
                v.set(i++, p.value);
            double t = v.elbow();
            coordinates.removeIf(p -> p.value < t);
        }
        return coordinates;
    }
}
