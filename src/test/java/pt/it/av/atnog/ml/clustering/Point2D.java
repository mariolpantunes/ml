package pt.it.av.atnog.ml.clustering;

/**
 * Two-dimension point.
 * <p>Used to test the clustering algorithms.</p>
 */
public class Point2D implements Distance<Point2D> {
    double x, y;

    /**
     * 2D Point constructor.
     * @param x coordenate of the point
     * @param y coordenate of the point
     */
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double distance(Point2D d) {
        return Math.hypot(x - d.x, y - d.y);
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }
}