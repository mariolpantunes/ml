package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.utils.bla.Vector;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Implements a Distributional Profile optimizer based on Elbow method.
 * @author Mário Antunes
 * @version 1.0
 */
public class DPElbowOptmization implements DPOptimization {
    private final int min;

    /**
     *
     * @param min
     */
    public DPElbowOptmization(final int min) {
        this.min = min;
    }

    @Override
    public List<DP.Coordinate> optimize(List<DP.Coordinate> coordinates) {
        if (coordinates.size() > min) {
            Comparator<DP.Coordinate> c = (DP.Coordinate a, DP.Coordinate b) -> (Double.compare(b.value, a.value));
            Collections.sort(coordinates, c);
            Vector v = new Vector(coordinates.size());
            int i = 0;
            for (DP.Coordinate p : coordinates)
                v.set(i++, p.value);
            double t = v.elbow();
            coordinates.removeIf(p -> p.value < t);
        }
        return coordinates;
    }
}
