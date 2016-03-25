package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.utils.bla.Vector;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public class DP {
    private final NGram term;
    private List<Coordinate> coordinates;

    public DP(final NGram term, List<Coordinate> coordinates) {
        this.term = term;
        this.coordinates = coordinates;
    }

    public double similarity(DP dp) {
        double rv = 0.0;
        if(term.equals(dp.term))
            rv = 1.0;
        else {
            Comparator<Coordinate> c = (Coordinate a, Coordinate b) -> (a.term.compareTo(b.term));
            Collections.sort(coordinates, c);
            Collections.sort(dp.coordinates, c);
            double dataA[] = new double[coordinates.size() + dp.coordinates.size()],
                    dataB[] = new double[coordinates.size() + dp.coordinates.size()];
            int i = 0, j = 0, idx = 0;
            while (i < coordinates.size() && j < dp.coordinates.size()) {
                if (coordinates.get(i).term.compareTo(dp.coordinates.get(j).term) == 0) {
                    dataA[idx] = coordinates.get(i++).value;
                    dataB[idx] = dp.coordinates.get(j++).value;
                } else if (coordinates.get(i).term.compareTo(dp.coordinates.get(j).term) < 0) {
                    dataA[idx] = coordinates.get(i++).value;
                    dataB[idx] = 0.0;
                } else {
                    dataA[idx] = 0.0;
                    dataB[idx] = dp.coordinates.get(j++).value;
                }
                idx++;
            }

            while (i < coordinates.size()) {
                dataA[idx] = coordinates.get(i++).value;
                dataB[idx] = 0.0;
                idx++;
            }

            while (j < dp.coordinates.size()) {
                dataA[idx] = 0.0;
                dataB[idx] = dp.coordinates.get(j++).value;
                idx++;
            }

            Vector a = new Vector(dataA, 0, idx), b = new Vector(dataB, 0, idx);
            rv = a.cosine(b);
        }
        return rv;
    }


    @Override
    public String toString() {
        Comparator<Coordinate> c = (Coordinate a, Coordinate b) -> (Double.compare(b.value, a.value));
        Collections.sort(coordinates, c);
        StringBuilder sb = new StringBuilder();
        sb.append(term + " [");
        int i = 0;
        for (int t = coordinates.size() - 1; i < t; i++)
            sb.append(coordinates.get(i).toString() + "; ");
        if(!coordinates.isEmpty())
            sb.append(coordinates.get(i).toString());
        sb.append(']');
        return sb.toString();
    }

    /**
     *
     * @param dpOptimizer
     */
    public void optimize(DPOptimization dpOptimizer) {
        coordinates = dpOptimizer.optimize(coordinates);
    }

    public static class Coordinate {
        protected final NGram term;
        protected final double value;

        public Coordinate(final NGram term, final double value) {
            this.term = term;
            this.value = value;
        }

        @Override
        public String toString() {
            return "("+term.toString()+"; "+value+")";
        }
    }
}
