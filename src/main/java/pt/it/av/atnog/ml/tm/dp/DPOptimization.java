package pt.it.av.atnog.ml.tm.dp;

import java.util.List;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public interface DPOptimization {
    List<DP.Coordinate> optimize(List<DP.Coordinate> coordinates);
}
