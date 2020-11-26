package pt.it.av.tnav.ml.sp;

import java.util.ArrayList;
import java.util.List;

import pt.it.av.tnav.utils.structures.point.Point2D;

public class RDP {

    private static double perpendicularDistance(Point2D pt, Point2D lineStart, Point2D lineEnd) {
        double dx = lineEnd.x() - lineStart.x();
        double dy = lineEnd.y() - lineStart.y();

        // Normalize
        double mag = Math.hypot(dx, dy);
        if (mag > 0.0) {
            dx /= mag;
            dy /= mag;
        }
        double pvx = pt.x() - lineStart.x();
        double pvy = pt.y() - lineStart.y();

        // Get dot product (project pv onto normalized direction)
        double pvdot = dx * pvx + dy * pvy;

        // Scale line direction vector and subtract it from pv
        double ax = pvx - pvdot * dx;
        double ay = pvy - pvdot * dy;

        return Math.hypot(ax, ay);
    }

    public static List<Point2D> rdp(List<Point2D> pointList, double epsilon) {
        List<Point2D> rv = new ArrayList<>();
        rdp_rec(pointList, epsilon, rv);
        return rv;
    }

    private static void rdp_rec(List<Point2D> pointList, double epsilon, List<Point2D> out) {
        if (pointList.size() < 2)
            throw new IllegalArgumentException("Not enough points to simplify");

        // Find the point with the maximum distance from line between the start and end
        double dmax = 0.0;
        int index = 0;
        int end = pointList.size() - 1;
        for (int i = 1; i < end; ++i) {
            double d = perpendicularDistance(pointList.get(i), pointList.get(0), pointList.get(end));
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        // If max distance is greater than epsilon, recursively simplify
        if (dmax > epsilon) {
            List<Point2D> recResults1 = new ArrayList<>();
            List<Point2D> recResults2 = new ArrayList<>();
            List<Point2D> firstLine = pointList.subList(0, index + 1);
            List<Point2D> lastLine = pointList.subList(index, pointList.size());
            rdp_rec(firstLine, epsilon, recResults1);
            rdp_rec(lastLine, epsilon, recResults2);

            // build the result list
            out.addAll(recResults1.subList(0, recResults1.size() - 1));
            out.addAll(recResults2);
            if (out.size() < 2)
                throw new RuntimeException("Problem assembling output");
        } else {
            // Just return start and end points
            out.clear();
            out.add(pointList.get(0));
            out.add(pointList.get(pointList.size() - 1));
        }
    }

    public static List<Point2D> rdp(List<Point2D> pointList) {
        List<Point2D> rv = new ArrayList<>();
        rdp_rec(pointList, rv);
        return rv;
    }

    private static void rdp_rec(List<Point2D> pointList, List<Point2D> out) {
        if (pointList.size() < 2)
            throw new IllegalArgumentException("Not enough points to simplify");

        // Find the point with the maximum distance from line between the start and end
        double dmax = 0.0;
        int index = 0;
        int end = pointList.size() - 1;

        double avgY = 0;
        double tss =0, rss = 0;

        double x1 = pointList.get(0).x(), x2 = pointList.get(end).x(), y1 = pointList.get(0).y(), y2 = pointList.get(end).y();
        double m = (y2-y1) / (x2-x1), b = y1 - (m*x1);

        System.err.println("F(x) = ["+m+"; "+b+"]");

        avgY +=pointList.get(0).y();
        rss += Math.pow(pointList.get(0).y()-(pointList.get(0).x()*m + b), 2.0);

        for (int i = 1; i < end; ++i) {
            double d = perpendicularDistance(pointList.get(i), pointList.get(0), pointList.get(end));
            if (d > dmax) {
                index = i;
                dmax = d;
            }
            
            avgY += pointList.get(i).y();
            rss += Math.pow(pointList.get(i).y()-(pointList.get(i).x()*m + b), 2.0);
        }

        avgY +=pointList.get(end).y();
        rss += Math.pow(pointList.get(end).y()-(pointList.get(end).x()*m + b), 2.0);

        avgY /= (double)pointList.size();

        for (int i = 1; i < end; ++i) {
            tss += Math.pow((pointList.get(i).y() - avgY), 2.0);
        }

        System.err.println("R2 ["+rss+"; "+tss+"]");
        
        double r2 = (tss>0) ? (1.0 - (rss/tss)) : (1.0 - rss);

        System.err.println("R2 = "+r2);

        //double s = Math.sqrt(Math.pow(x2 - x1, 2.0) + Math.pow(y2 - y1, 2.0));
        //double dtol = .17;//(Math.asin(Math.sqrt(2)/s))/(2.0*s);
        //System.err.println("S; Dmax; Dtol = ["+s+"; "+ dmax + "; " + dtol + "]");

        // If max distance is greater than epsilon, recursively simplify
        //if (dmax > dtol) {
        if (r2 < 0.95) {
            List<Point2D> recResults1 = new ArrayList<>();
            List<Point2D> recResults2 = new ArrayList<>();
            List<Point2D> firstLine = pointList.subList(0, index + 1);
            List<Point2D> lastLine = pointList.subList(index, pointList.size());
            rdp_rec(firstLine, recResults1);
            rdp_rec(lastLine, recResults2);

            // build the result list
            out.addAll(recResults1.subList(0, recResults1.size() - 1));
            out.addAll(recResults2);
            if (out.size() < 2)
                throw new RuntimeException("Problem assembling output");
        } else {
            // Just return start and end points
            out.clear();
            out.add(pointList.get(0));
            out.add(pointList.get(pointList.size() - 1));
        }
    }
    
}
