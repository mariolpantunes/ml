package pt.it.av.tnav.ml.clustering.curvature;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.it.av.tnav.utils.MathUtils;
import pt.it.av.tnav.utils.PrintUtils;
import pt.it.av.tnav.utils.csv.CSV;
import pt.it.av.tnav.utils.structures.point.Point2D;

public class TestMultiKnee {

    

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

    public static void main(String[] args) throws IOException {
        //final String csvFile = "/home/mantunes/multiknee/w01-vscsi2-minisim10000-lru-mrc.csv";
        //final String csvFile = "/home/mantunes/multiknee/w02-vscsi1-minisim10000-lru-mrc.csv";
        final String csvFile = "/home/mantunes/multiknee/w05-vscsi1-minisim10000-lru-mrc.csv";
        final String csvFileKnees = "/home/mantunes/multiknee/plot_knees.csv";

        // load csv data into a list of Point2D
        FileReader fr = new FileReader(csvFile);
        List<Point2D> points = Point2D.csvLoad(CSV.read(fr));
        System.out.println("Number of data points: " + points.size());

        // apply Douglas-Peucker Algorithm in order to reduce complexity
        // List<Point2D> points_rdp = rdp(points, 0.0001);
        // List<Point2D> points_rdp = points;
        

        // apply Kneedle
        //int idx[] = Kneedle.knee(points_rdp);
        int idx[] = Kneedle.knee(points, 1.0, Kneedle.Direction.Decreasing, Kneedle.Concavity.Clockwise);
        System.out.println("Max idx: " + PrintUtils.array(idx));

        // write knee to CSV
        List<Point2D> knees = new ArrayList<>();
        for (int i : idx) {
            knees.add(new Point2D(points.get(i).x(), 1.0));
        }

        CSV csv = new CSV();
        csv.addLines(knees);
        FileWriter w = new FileWriter(csvFileKnees);
        csv.write(w);
        w.flush();
        w.close();

        /*
         * 
         * // convert lists into arrays (have to finish the CSV) double x[] = new
         * double[points_rdp.size()], y[] = new double[points_rdp.size()]; for(int i =
         * 0; i < points_rdp.size(); i++) { x[i] = points_rdp.get(i).x(); y[i] =
         * points_rdp.get(i).y(); }
         * 
         * // apply sma_linear double sy[] = new double[y.length];
         * //Smoothing.sma_linear(y, x, 3, sy);
         * 
         * // output new csv writer = new FileWriter(csvFileSmooth); for(int i = 0; i <
         * x.length; i++) { writer.append(String.valueOf(x[i])); writer.append(',');
         * writer.append(String.valueOf(sy[i])); writer.append("\n"); } writer.flush();
         * writer.close();
         * 
         * // run DK method and output values //DKmethod c = new DKmethod();
         * //c.elbow(x, sy); double fd[] = first_derivative(x, sy), sd[] =
         * second_derivative(x, sy);
         * 
         * // output the curvature values writer = new FileWriter(csvFileDerivative);
         * for(int i = 0; i < x.length; i++) { writer.append(String.valueOf(x[i]));
         * writer.append(','); writer.append(String.valueOf(fd[i])); writer.append(',');
         * writer.append(String.valueOf(sd[i])); writer.append("\n"); } writer.flush();
         * writer.close();
         * 
         * double curvature[] = new double[fd.length]; for(int i = 0; i <
         * curvature.length; i++) { curvature[i] = Math.abs(sd[i])/(Math.pow(1 +
         * Math.pow(fd[i], 2.0), 1.5)); }
         * 
         * // output the curvature values writer = new FileWriter(csvFileCurvature);
         * for(int i = 0; i < curvature.length; i++) {
         * writer.append(String.valueOf(x[i])); writer.append(',');
         * writer.append(String.valueOf(curvature[i])); writer.append("\n"); }
         * writer.flush(); writer.close();
         * 
         * // find top k max values int idx[] = new int[10]; findKmax(curvature, idx);
         * System.out.println("Max idx: "+PrintUtils.array(idx));
         */
    }
}
