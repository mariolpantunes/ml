package pt.it.av.atnog.ml.tm.dp;

import pt.it.av.atnog.utils.MathUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Implements a Distributional Profile optimizer based on P-value statistical significance.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPStatisticOptimization implements DPOptimization {
    private final int min, neighborhood;
    private final double alpha;

    public DPStatisticOptimization(final int min, final int neighborhood, final double alpha) {
        this.min = min;
        this.neighborhood = neighborhood;
        this.alpha = alpha;
    }

    @Override
    public List<DP.Coordinate> optimize(List<DP.Coordinate> coordinates) {
        if (coordinates.size() > min) {
            int vocabulary = coordinates.size(), total = 0;
            for (DP.Coordinate c : coordinates)
                total += c.value;
            int partitions = total / neighborhood;
            coordinates.removeIf(p -> probs((int) p.value, partitions, vocabulary) >= alpha);
        }
        return coordinates;
    }

    private double probs(int freq, int partitions, int vocabulary) {
        double pr = 0.0;
        //partitions = partitions < freq ? freq : partitions;
        //System.out.print("("+freq+"; "+partitions+"; "+vocabulary+")");
        for (int i = 0; i < freq; i++)
            pr += tprobs(i, partitions, vocabulary);
        //System.out.println(" = "+(1.0 - pr));
        return 1.0 - pr;
    }

    private double tprobs(int freq, int partitions, int vocabulary) {
        double pr = 0.0;
        try {
            //System.err.println("("+freq+"; "+partitions+"; "+vocabulary+")");
            BigDecimal C = MathUtils.binomialBD(partitions, freq);
            //System.err.println("C = "+C);

            BigDecimal bd1 = new BigDecimal(vocabulary - 1);
            bd1 = bd1.pow(partitions - freq);
            //System.err.println("(V-1)^(P-F) = "+bd1);

            BigDecimal bd2 = new BigDecimal(vocabulary);
            bd2 = bd2.pow(partitions);
            //System.err.println("V^P = "+bd2);

            bd1 = bd1.divide(bd2, 100, RoundingMode.HALF_UP);
            //System.err.println("(V-1)^(P-F) / V^P = "+bd1);

            pr = C.multiply(bd1).doubleValue();
            //System.err.println("Pr = "+pr);
        } catch (Exception e) {
            System.out.print("("+freq+"; "+partitions+"; "+vocabulary+")");
            throw e;
        }

        return pr;
    }
}
