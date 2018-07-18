package pt.it.av.tnav.ml.tm.dp;

import java.util.List;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public interface DPWOptimization {
    List<DPW.DpDimension> optimize(List<DPW.DpDimension> dpDimensions);
}
