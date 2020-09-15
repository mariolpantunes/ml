package pt.it.av.tnav.ml.clustering.curvature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.it.av.tnav.ml.sp.Smoothing;
import pt.it.av.tnav.utils.ArrayUtils;
import pt.it.av.tnav.utils.PrintUtils;

public class TestMultiKnee {

    public static void findKmax(final double values[], final int idx[]) {
        // insert the indexes of the first elements
        for(int i = 0; i < idx.length; i++) {
            idx[i] = i;
        }

        // find the min value in idx
        int min_idx = 0;
        double min_value = values[idx[min_idx]];
        for(int i = 1; i < idx.length; i++) {
            double temp_min_value = values[idx[i]];
            if(temp_min_value < min_value) {
                min_value = temp_min_value;
                min_idx = idx[i];
            }
        }

        // loop the remaining array and swap the minimum
        for(int i = idx.length; i < values.length; i++) {
            if(values[i] > min_value) {
                idx[min_idx] = i;
                // find the min value in idx
                for(int j = 1; j < idx.length; j++) {
                    double temp_min_value = values[idx[j]];
                    if(temp_min_value < min_value) {
                        min_value = temp_min_value;
                        min_idx = idx[j];
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        final String csvFile = "/home/mantunes/multiknee/plot00.csv";
        final String csvFileSmooth = "/home/mantunes/multiknee/plot01.csv";
        final String csvFileCurvature = "/home/mantunes/multiknee/plot02.csv";
        String line = "";
        String cvsSplitBy = ",";

        // load csv data into two Lists
        List<Double> lx = new ArrayList<>(),
        ly = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
            String[] values = line.split(cvsSplitBy);
            //System.out.println(PrintUtils.array(values));
            lx.add(Double.parseDouble(values[0]));
            ly.add(Double.parseDouble(values[1]));
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

        // convert lists into arrays (have to finish the CSV)
        double x[] = new double[lx.size()], y[] =  new double[ly.size()];
        for(int i = 0; i < lx.size(); i++) {
            x[i] = lx.get(i);
            y[i] = ly.get(i);
        }

        // apply sma_linear
        double sy[] = new double[y.length];
        Smoothing.sma_linear(y, x, 32, sy);

        // output new csv
        FileWriter writer = new FileWriter(csvFileSmooth);
        for(int i = 0; i < lx.size(); i++) {
            writer.append(String.valueOf(lx.get(i)));
            writer.append(',');
            writer.append(String.valueOf(sy[i]));
            writer.append("\n");
        }
        writer.flush();
        writer.close();

        // run DK method and output values
        //DKmethod c = new DKmethod();
        //c.elbow(x, sy);
        double fd[] = ArrayUtils.cfd(x,y), sd[] = ArrayUtils.csd(x,y);
        System.out.println(fd.length+"/"+sd.length+"/"+x.length);
        double curvature[] = new double[fd.length];
        for(int i = 0; i < curvature.length; i++) {
            curvature[i] = Math.abs(sd[i]/(Math.pow(1+Math.pow(fd[i],2),3/2)));
        }

        // output the curvature values
        writer = new FileWriter(csvFileCurvature);
        for(int i = 0; i < curvature.length; i++) {
            writer.append(String.valueOf(lx.get(i+1)));
            writer.append(',');
            writer.append(String.valueOf(curvature[i]));
            writer.append("\n");
        }
        writer.flush();
        writer.close();

        // find top k max values
        int idx[] = new int[1000];
        findKmax(curvature, idx);
        System.out.println("Max idx: "+PrintUtils.array(idx));
    }
}
