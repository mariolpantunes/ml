package pt.it.av.tnav.ml.sp;

import java.util.ArrayList;
import java.util.List;

import pt.it.av.tnav.utils.structures.point.Point1D;
import pt.it.av.tnav.utils.structures.point.Point2D;

public class SP {
    private SP() {}

    public static List<Point2D> first_derivative(List<Point2D> points) {
        List<Point2D> rv = new ArrayList<>();

        for (int i = 1; i < points.size() - 1; i++) {
            Point2D p0 = points.get(i-1), p1 =  points.get(i+1);            
            double y = (p1.y() - p0.y()) / (p1.x() - p0.x());
            rv.add(new Point2D(points.get(i).x(), y));
        }
        return rv;
    }

    public static double[] second_derivative(final double x[], final double y[]) {
        double sd[] = new double[y.length];
        for (int i = 1; i < sd.length - 1; i++) {
            sd[i] = (y[i + 1] - 2.0 * y[i] + y[i - 1]) / ((x[i + 1] - x[i]) * (x[i] - x[i - 1]));
        }
        return sd;
    }

    public static List<Point1D> getY(List<Point2D> points) {
        List<Point1D> rv = new ArrayList<>();
        for(Point2D point : points) {
            rv.add(new Point1D(point.y()));
        }
        return rv;
    }

    public static int[] findPeaks2D(List<Point2D> points) {
        List<Point1D> points1d = getY(points);
        return findPeaks1D(points1d);
    }

    public static List<Point1D> diff(List<Point1D> points, int lag) {
        List<Point1D> rv = new ArrayList<>();
        for(int i = lag; i < points.size()-lag; i++) {
            Point1D p0 = points.get(i-lag), p1 = points.get(i+lag);
            rv.add(new Point1D(p1.x() - p0.x()));
        }
        return rv;
    }

    public static List<Point1D> sign(List<Point1D> points) {
        List<Point1D> rv = new ArrayList<>();
        for(Point1D point : points) {
            double s = 0;
            if (point.x() > 0) {
                s = 1.0;
            } else if (point.x() < 0) {
                s = -1.0;
            }
            rv.add(new Point1D(s));
        }
        return rv;
    }

    public static int[] findPeaks1D(List<Point1D> points) {
        List<Point1D> shape = diff(sign(diff(points, 1)), 1);
        
        //https://stats.stackexchange.com/questions/22974/how-to-find-local-peaks-valleys-in-a-series-of-data
        
        for(int i = 0; i < shape.size(); i++) {
            Point1D s = shape.get(i);
            if (s.x() < 0) {

            }
        }

        return null;
    }
}
