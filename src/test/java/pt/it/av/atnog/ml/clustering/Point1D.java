package pt.it.av.atnog.ml.clustering;

/**
 * One-dimension point.
 * <p>Used to test the clustering algorithms.</p>
 */
public class Point1D implements Distance<Point1D> {
    double x;

    /**
     * 1D Point constructor.
     * @param x coordenate of the point
     */
    public Point1D(double x) {
        this.x = x;
    }

    @Override
    public double distance(Point1D d) {
        return Math.abs(x - d.x);
    }

    @Override
    public String toString() {
        return "(" + x + ")";
    }
}
