package pt.ua.it.atnog.ml.utils;

public class Utils {

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
	return (T) o;
    }

    public static void shuffle(int array[]) {
	if (array.length > 1) {
	    for (int i = array.length - 1; i > 1; i--) {
		int j = randomBetween(0, i);
		int t = array[j];
		array[j] = array[i];
		array[i] = t;
	    }
	}
    }

    public static void shuffle(byte array[]) {
	if (array.length > 1) {
	    for (int i = array.length - 1; i > 1; i--) {
		int j = randomBetween(0, i);
		byte t = array[j];
		array[j] = array[i];
		array[i] = t;
	    }
	}
    }

    public static double precision(double TP, double FP) {
	double rv = 0.0;
	if (TP > 0)
	    rv = TP / (TP + FP);
	return rv;
    }

    public static double recall(double TP, double FN) {
	double rv = 0.0;
	if (TP > 0)
	    rv = TP / (TP + FN);
	return rv;
    }

    public static double fmeasure(double precision, double recall, double beta) {
	double rv = 0;
	if (precision > 0 || recall > 0)
	    rv = ((1.0 + Math.pow(beta, 2.0)) * precision * recall)
		    / ((Math.pow(beta, 2.0) * precision) + recall);
	return rv;
    }

    public static double randomBetween(double min, double max) {
	return min + (Math.random() * ((max - min) + 1.0));
    }

    public static int randomBetween(int min, int max) {
	return min + (int) (Math.random() * ((max - min) + 1));
    }
    
    public static long poisonDist(long lambda) {
	double L = Math.exp(-lambda), p = 1.0;
	long k = 0;

	do {
	    k++;
	    p *= Math.random();
	} while (p > L);

	return k - 1;
    }

    public static double round(double v, int ndp) {
	double mf = Math.pow(10, ndp);
	System.err.println(v*mf);
	return Math.round(v*mf) / (mf*10);
    }

    public static double round(double v) {
	int ndp = Math.abs((int) (Math.log10(v)) - 1);
	double mf = Math.pow(10, ndp);
	return Math.round(v * mf) / mf;
    }
}
