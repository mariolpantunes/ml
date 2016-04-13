package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.utils.PrintUtils;
import pt.it.av.atnog.utils.structures.Similarity;

import java.util.List;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public class DPC implements Similarity<DPC> {
    private final NGram term;
    private List<Category> categories;

    /**
     * @param term
     * @param categories
     */
    public DPC(final NGram term, List<Category> categories) {
        this.term = term;
        this.categories = categories;
    }

    /**
     * @return
     */
    public NGram term() {
        return term;
    }

    /**
     * @return
     */
    public int size() {
        return categories.size();
    }

    /**
     * @param c
     * @return
     */
    public int size(int c) {
        return categories.get(c).size();
    }

    @Override
    public double similarityTo(DPC dpc) {
        double rv = 0.0;

        for (Category c1 : categories) {
            for (Category c2 : dpc.categories) {
                double s = DPW.similarity(c1.coordinates, c2.coordinates) * ((c1.affinity + c2.affinity) / 2.0);
                if (s > rv)
                    rv = s;
            }
        }

        return rv;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(term.toString() + System.getProperty("line.separator"));
        for (Category category : categories)
            sb.append(category.toString() + System.getProperty("line.separator"));
        return sb.toString();
    }

    public static class Category {
        public final List<DPW.Coordinate> coordinates;
        public final double affinity;


        public Category(final List<DPW.Coordinate> coordinates, final double affinity) {
            this.coordinates = coordinates;
            this.affinity = affinity;
        }


        public int size() {
            return coordinates.size();
        }

        @Override
        public String toString() {
            return "{" + affinity + "; " + PrintUtils.list(coordinates) + "}";
        }

        @Override
        public int hashCode() {
            return coordinates.hashCode() ^ Double.valueOf(affinity).hashCode();
        }

        @Override
        public boolean equals(Object o) {
            boolean rv = false;
            if (o != null) {
                if (o == this)
                    rv = true;
                else if (o instanceof Category) {
                    Category c = (Category) o;
                    rv = this.coordinates.equals(c.coordinates) && this.affinity == c.affinity;
                }
            }
            return rv;
        }
    }
}
