package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.structures.tuple.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mantunes on 3/20/15.
 */
public class DP {
    private final NGram term;
    private final List<Pair<NGram, Double>> profile;

    public DP(NGram term, List<Pair<NGram, Double>> profile) {
        this.term = term;
        this.profile = profile;
    }

    public void optimize_elbow() {
        if (profile.size() > 25) {
            Comparator<Pair<NGram, Double>> c = (Pair<NGram, Double> a, Pair<NGram, Double> b) -> (Double.compare(b.b, a.b));
            Collections.sort(profile, c);
            Vector v = new Vector(profile.size());
            int i = 0;
            for (Pair<NGram, Double> p : profile)
                v.set(i++, p.b);
            double t = v.elbow();
            profile.removeIf(p -> p.b < t);
        }
    }

    public double similarity(DP dp) {
        Comparator<Pair<NGram, Double>> c = (Pair<NGram, Double> a, Pair<NGram, Double> b) -> (a.a.compareTo(b.a));
        Collections.sort(profile, c);
        Collections.sort(dp.profile, c);
        double dataA[] = new double[profile.size() + dp.profile.size()],
                dataB[] = new double[profile.size() + dp.profile.size()];
        int i = 0, j = 0, idx = 0;
        while (i < profile.size() && j < dp.profile.size()) {
            if (profile.get(i).a.compareTo(dp.profile.get(j).a) == 0) {
                dataA[idx] = profile.get(i).b;
                dataB[idx] = dp.profile.get(j).b;
                i++;
                j++;
            } else if (profile.get(i).a.compareTo(dp.profile.get(j).a) < 0) {
                dataA[idx] = profile.get(i).b;
                dataB[idx] = 0.0;
                i++;
            } else {
                dataA[idx] = 0.0;
                dataB[idx] = dp.profile.get(j).b;
                j++;
            }
            idx++;
        }
        Vector a = new Vector(dataA, 0, idx), b = new Vector(dataB, 0, idx);
        return a.cosine(b);
    }


    public String toString() {
        Comparator<Pair<NGram, Double>> c = (Pair<NGram, Double> a, Pair<NGram, Double> b) -> (Double.compare(b.b, a.b));
        Collections.sort(profile, c);
        StringBuilder sb = new StringBuilder();
        sb.append(term + " [");
        int i = 0;
        for (int t = profile.size() - 1; i < t; i++)
            sb.append(profile.get(i).toString() + "; ");
        sb.append(profile.get(i).toString() + "]");
        return sb.toString();
    }
}
