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

    public int size() {
        return coordinates.size();
    }

    public List<Coordinate> coordinates() {
        return coordinates;
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
        public final NGram term;
        public final NGram stemm;
        public final double value;

        public Coordinate(final NGram term, final NGram stemm, final double value) {
            this.term = term;
            this.stemm = stemm;
            this.value = value;
        }

        @Override
        public String toString() {
            return "("+term.toString()+"["+stemm+"]; "+value+")";
        }

        @Override
        public int hashCode() {
            return term.hashCode() ^ stemm.hashCode() ^ Double.valueOf(value).hashCode();
        }

        @Override
        public boolean equals(Object o) {
            boolean rv = false;
            if (o != null) {
                if (o == this)
                    rv = true;
                else if (o instanceof Coordinate) {
                    Coordinate c = (Coordinate) o;
                    rv = this.term.equals(c.term) &&
                            this.stemm.equals(c.stemm) &&
                    this.value == c.value;
                }
            }
            return rv;
        }
    }
}
