package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.structures.tuple.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mantunes on 6/15/15.
 */
public class ElbowOptmizer implements DPOptimizer {
    private final int min;

    public ElbowOptmizer(final int min) {
        this.min = min;
    }

    @Override
    public List<Pair<NGram, Double>> optimize(List<Pair<NGram, Double>> profile) {
        if (profile.size() > min) {
            Comparator<Pair<NGram, Double>> c = (Pair<NGram, Double> a, Pair<NGram, Double> b) -> (Double.compare(b.b, a.b));
            Collections.sort(profile, c);
            Vector v = new Vector(profile.size());
            int i = 0;
            for (Pair<NGram, Double> p : profile)
                v.set(i++, p.b);
            double t = v.elbow();
            profile.removeIf(p -> p.b < t);
        }
        return profile;
    }
}
