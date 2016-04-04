package pt.it.av.atnog.ml.tm.dp;

import java.util.List;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public class DPStemmOptimization implements DPOptimization {
    @Override
    public List<DP.Coordinate> optimize(List<DP.Coordinate> coordinates) {
        for(int i = 0; i < coordinates.size() - 1; i++) {
            DP.Coordinate a = coordinates.get(i);
            for(int j = i+1; j < coordinates.size(); j++) {
                DP.Coordinate b = coordinates.get(j);
                if(a.stemm.equals(b.stemm)) {
                    double total = a.value + b.value;
                    if(a.term.length() < b.term.length())
                        coordinates.set(i, new DP.Coordinate(a.term, a.stemm, total));
                    else
                        coordinates.set(i, new DP.Coordinate(b.term, b.stemm, total));
                    a = coordinates.get(i);
                    coordinates.remove(j);
                }
            }
        }
        return coordinates;
    }
}
