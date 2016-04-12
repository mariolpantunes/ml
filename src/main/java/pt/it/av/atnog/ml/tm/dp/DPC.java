package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.utils.structures.Similarity;

import java.util.List;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public class DPC implements Similarity<DPC> {
    private final NGram term;
    private List<List<DPW.Coordinate>> categories;

    /**
     *
     * @param term
     * @param categories
     */
    public DPC(final NGram term, List<List<DPW.Coordinate>> categories) {
        this.term = term;
        this.categories = categories;
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
        return categories.size();
    }

    /**
     *
     * @param c
     * @return
     */
    public int size(int c) {
       return categories.get(c).size();
    }

    @Override
    public double similarityTo(DPC dpc) {
        double rv = 0.0;

        for(List<DPW.Coordinate> c1 : categories) {
            for(List<DPW.Coordinate> c2 : dpc.categories) {
                double s = DPW.similarity(c1, c2);
                if(s > rv)
                    rv = s;
            }
        }

        return rv;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(term.toString()+System.getProperty("line.separator"));
        for(List<DPW.Coordinate> context: categories)
            sb.append(DPW.coordinatesToString(context)+System.getProperty("line.separator"));
        return sb.toString();
    }
}
