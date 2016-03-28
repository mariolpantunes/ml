package pt.it.av.atnog.ml.tm.dp;

import java.util.List;

/**
 * Created by mantunes on 6/15/15.
 */
public interface DPOptimization {
    List<DP.Coordinate> optimize(List<DP.Coordinate> coordinates);
}
