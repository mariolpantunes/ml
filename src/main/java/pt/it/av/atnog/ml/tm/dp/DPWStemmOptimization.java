package pt.it.av.atnog.ml.tm.dp;

import java.util.List;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWStemmOptimization implements DPWOptimization {
    @Override
    public List<DPW.Coordinate> optimize(List<DPW.Coordinate> coordinates) {
        for(int i = 0; i < coordinates.size() - 1; i++) {
            DPW.Coordinate a = coordinates.get(i);
            for(int j = i+1; j < coordinates.size(); j++) {
                DPW.Coordinate b = coordinates.get(j);
                if(a.stemm.equals(b.stemm)) {
                    double total = a.value + b.value;
                    if(a.term.length() < b.term.length())
                        coordinates.set(i, new DPW.Coordinate(a.term, a.stemm, total));
                    else
                        coordinates.set(i, new DPW.Coordinate(b.term, b.stemm, total));
                    a = coordinates.get(i);
                    coordinates.remove(j);
                }
            }
        }
        return coordinates;
    }
}
