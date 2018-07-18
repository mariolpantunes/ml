package pt.it.av.tnav.ml.tm.dp;

import java.util.List;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWStemmOptimization implements DPWOptimization {
    @Override
    public List<DPW.DpDimension> optimize(List<DPW.DpDimension> dpDimensions) {
        for(int i = 0; i < dpDimensions.size() - 1; i++) {
            DPW.DpDimension a = dpDimensions.get(i);
            for(int j = i+1; j < dpDimensions.size(); j++) {
                DPW.DpDimension b = dpDimensions.get(j);
                if(a.stemm.equals(b.stemm)) {
                    double total = a.value + b.value;
                    if(a.term.length() < b.term.length())
                        dpDimensions.set(i, new DPW.DpDimension(a.term, a.stemm, total));
                    else
                        dpDimensions.set(i, new DPW.DpDimension(b.term, b.stemm, total));
                    a = dpDimensions.get(i);
                    dpDimensions.remove(j);
                }
            }
        }
        return dpDimensions;
    }
}
