package pt.it.av.atnog.ml.thresholding;

import java.util.List;

public class Thresholding {
    public static double isoData(List<? extends Score> elements) {
        double iT = 0.0, fT = 0.0;
        boolean done = false;

        for (Score s : elements)
            iT += s.score();
        iT /= (double) elements.size();

        do {
            double avgG1 = 0.0, avgG2 = 0.0;
            int sizeG1 = 0, sizeG2 = 0;

            for (Score s : elements) {
                if (s.score() > iT) {
                    avgG1 += s.score();
                    sizeG1++;
                } else {
                    avgG2 += s.score();
                    sizeG2++;
                }
            }

            if (sizeG1 > 0)
                avgG1 /= (double) sizeG1;

            if (sizeG2 > 0)
                avgG2 /= (double) sizeG2;

            fT = (avgG1 + avgG2) / 2.0;

            if (fT == iT)
                done = true;
            else
                iT = fT;
        } while (!done);

        return fT;
    }
}
