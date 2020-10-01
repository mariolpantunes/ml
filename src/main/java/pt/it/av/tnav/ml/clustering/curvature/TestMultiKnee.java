package pt.it.av.tnav.ml.clustering.curvature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.it.av.tnav.ml.sp.Smoothing;
import pt.it.av.tnav.utils.PrintUtils;
import pt.it.av.tnav.utils.structures.point.Point2D;

public class TestMultiKnee {

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

    private static void rdp(List<Point2D> pointList, double epsilon, List<Point2D> out) {
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
            rdp(firstLine, epsilon, recResults1);
            rdp(lastLine, epsilon, recResults2);

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

    public static void findKmax(final double values[], final int idx[]) {
        // insert the indexes of the first elements
        for (int i = 0; i < idx.length; i++) {
            idx[i] = i;
        }

        // find the min value in idx
        int min_idx = 0;
        double min_value = values[idx[min_idx]];
        for (int i = 1; i < idx.length; i++) {
            double temp_min_value = values[idx[i]];
            if (temp_min_value < min_value) {
                min_value = temp_min_value;
                min_idx = idx[i];
            }
        }

        // loop the remaining array and swap the minimum
        for (int i = idx.length; i < values.length; i++) {
            if (values[i] > min_value) {
                idx[min_idx] = i;
                // find the min value in idx
                for (int j = 1; j < idx.length; j++) {
                    double temp_min_value = values[idx[j]];
                    if (temp_min_value < min_value) {
                        min_value = temp_min_value;
                        min_idx = idx[j];
                    }
                }
            }
        }
    }

    public static double[] first_derivative(final double x[], final double y[]) {
        System.out.println(y.length);

        double fd[] = new double[y.length];
        System.out.println(fd.length);
        for (int i = 1; i < fd.length - 1; i++) {
            if (i > 0 && i < 10) {
                System.out.println("YY: " + y[i + 1] + " - " + y[i - 1] + " = " + (y[i + 1] - y[i - 1]));
                System.out.println("XX: 2h = " + (x[i + 1] - x[i - 1]));
            }
            fd[i] = (y[i + 1] - y[i - 1]) / (x[i + 1] - x[i - 1]);
        }
        return fd;
    }

    public static double[] second_derivative(final double x[], final double y[]) {
        double sd[] = new double[y.length];
        for (int i = 1; i < sd.length - 1; i++) {
            sd[i] = (y[i + 1] - 2.0 * y[i] + y[i - 1]) / ((x[i + 1] - x[i]) * (x[i] - x[i - 1]));
        }
        return sd;
    }

    public static void main(String[] args) throws IOException {
        final String csvFile = "/home/mantunes/multiknee/plot00.csv";
        final String csvFileReduction = "/home/mantunes/multiknee/plot01.csv";
        final String csvFileSmooth = "/home/mantunes/multiknee/plot02.csv";
        final String csvFileDerivative = "/home/mantunes/multiknee/plot03.csv";
        final String csvFileCurvature = "/home/mantunes/multiknee/plot04.csv";
        String line = "";
        String cvsSplitBy = ",";

        // load csv data into a list of Point2D
        List<Point2D> points = new ArrayList<>(),points_out = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
            String[] values = line.split(cvsSplitBy);
            //System.out.println(PrintUtils.array(values));
            points.add(new Point2D(Double.parseDouble(values[0]), Double.parseDouble(values[1])));
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        // Apply Douglas-Peucker Algorithm in order to reduce complexity
        rdp(points, 0.01, points_out);

        System.out.println(points.size()+"/"+points_out.size());

        // output new csv
        FileWriter writer = new FileWriter(csvFileReduction);
        for(int i = 0; i < points_out.size(); i++) {
            writer.append(String.valueOf(points_out.get(i).x()));
            writer.append(',');
            writer.append(String.valueOf(points_out.get(i).y()));
            writer.append("\n");
        }
        writer.flush();
        writer.close();

        // convert lists into arrays (have to finish the CSV)
        double x[] = new double[points_out.size()], y[] =  new double[points_out.size()];
        for(int i = 0; i < points_out.size(); i++) {
            x[i] = points_out.get(i).x();
            y[i] = points_out.get(i).y();
        }

        // apply sma_linear
        double sy[] = new double[y.length];
        //Smoothing.sma_linear(y, x, 3, sy);

        // output new csv
        writer = new FileWriter(csvFileSmooth);
        for(int i = 0; i < x.length; i++) {
            writer.append(String.valueOf(x[i]));
            writer.append(',');
            writer.append(String.valueOf(sy[i]));
            writer.append("\n");
        }
        writer.flush();
        writer.close();

        // run DK method and output values
        //DKmethod c = new DKmethod();
        //c.elbow(x, sy);
        double fd[] = first_derivative(x, sy), sd[] = second_derivative(x, sy);
        
        // output the curvature values
        writer = new FileWriter(csvFileDerivative);
        for(int i = 0; i < x.length; i++) {
            writer.append(String.valueOf(x[i]));
            writer.append(',');
            writer.append(String.valueOf(fd[i]));
            writer.append(',');
            writer.append(String.valueOf(sd[i]));
            writer.append("\n");
        }
        writer.flush();
        writer.close();

        double curvature[] = new double[fd.length];
        for(int i = 0; i < curvature.length; i++) {
            curvature[i] = Math.abs(sd[i])/(Math.pow(1 + Math.pow(fd[i], 2.0), 1.5));
        }

        // output the curvature values
        writer = new FileWriter(csvFileCurvature);
        for(int i = 0; i < curvature.length; i++) {
            writer.append(String.valueOf(x[i]));
            writer.append(',');
            writer.append(String.valueOf(curvature[i]));
            writer.append("\n");
        }
        writer.flush();
        writer.close();

        // find top k max values
        int idx[] = new int[10];
        findKmax(curvature, idx);
        System.out.println("Max idx: "+PrintUtils.array(idx));
    }
}
