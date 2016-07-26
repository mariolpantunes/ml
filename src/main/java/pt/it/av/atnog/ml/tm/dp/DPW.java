package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.structures.Similarity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Distributional Profile of a Word.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPW implements Similarity<DPW> {
    private final NGram term;
    private List<Coordinate> coordinates;

    public DPW(final NGram term, List<Coordinate> coordinates) {
        this.term = term;
        this.coordinates = coordinates;
    }

    /**
     *
     * @return
     */
    public NGram term() {
        return term;
    }

    /**
     *
     * @return
     */
    public int size() {
        return coordinates.size();
    }

    /**
     *
     * @param term
     * @return
     */
    public double getCoor(NGram term) {
        double rv = 0.0;
        Comparator<Coordinate> c = (Coordinate a, Coordinate b) -> (a.term.compareTo(b.term));
        Collections.sort(coordinates, c);
        int idx = Collections.binarySearch(coordinates, new Coordinate(term,term, 0), c);
        if(idx >= 0)
            rv = coordinates().get(idx).value;
        return rv;
    }

    /**
     *
     * @return
     */
    public List<Coordinate> coordinates() {
        return coordinates;
    }

    @Override
    public double similarityTo(DPW dpw) {
        double rv = 0.0;
        if(term.equals(dpw.term))
            rv = 1.0;
        else
            rv = similarity(coordinates, dpw.coordinates);
        return rv;
    }

    /**
     *
     * @param c1
     * @param c2
     * @return
     */
    public static double similarity(List<Coordinate> coor1, List<Coordinate> coor2) {
        Comparator<Coordinate> c = (Coordinate a, Coordinate b) -> (a.term.compareTo(b.term));
        Collections.sort(coor1, c);
        Collections.sort(coor2, c);

        double dataA[] = new double[coor1.size() + coor2.size()],
                dataB[] = new double[coor1.size() + coor2.size()];

        int i = 0, j = 0, idx = 0;
        while (i < coor1.size() && j < coor2.size()) {
            if (coor1.get(i).term.compareTo(coor2.get(j).term) == 0) {
                dataA[idx] = coor1.get(i++).value;
                dataB[idx] = coor2.get(j++).value;
            } else if (coor1.get(i).term.compareTo(coor2.get(j).term) < 0) {
                dataA[idx] = coor1.get(i++).value;
                dataB[idx] = 0.0;
            } else {
                dataA[idx] = 0.0;
                dataB[idx] = coor2.get(j++).value;
            }
            idx++;
        }

        while (i < coor1.size()) {
            dataA[idx] = coor1.get(i++).value;
            dataB[idx] = 0.0;
            idx++;
        }

        while (j < coor2.size()) {
            dataA[idx] = 0.0;
            dataB[idx] = coor2.get(j++).value;
            idx++;
        }

        return (new Vector(dataA, 0, idx)).cosine(new Vector(dataB, 0, idx));
    }

    @Override
    public String toString() {
        return term.toString()+" "+coordinatesToString(coordinates);
    }

    /**
     *
     * @param coordinates
     * @return
     */
    protected static String coordinatesToString(List<Coordinate> coordinates) {
        Comparator<Coordinate> c = (Coordinate a, Coordinate b) -> (Double.compare(b.value, a.value));
        Collections.sort(coordinates, c);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
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
    public void optimize(DPWOptimization dpOptimizer) {
        coordinates = dpOptimizer.optimize(coordinates);
    }

    /**
     *
     */
    public static class Coordinate {
        public final NGram term;
        public final NGram stemm;
        public final double value;

        /**
         *
         * @param term
         * @param stemm
         * @param value
         */
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
