package pt.it.av.atnog.ml.dataset;

import pt.it.av.atnog.utils.bla.Vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Vehicle implements Dataset<Vector>{
  @Override
  public List<Vector> load() {
    String fileName = getClass().getResource("vehicle.csv").getFile();
    List<Vector> dataset = new ArrayList<>(846);
    try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line = br.readLine();
      while (line != null) {
        String split[] = line.split(",");

        double data[] = new double[split.length-1];
        for(int i = 0; i < split.length-1; i++) {
          data[i] = Double.parseDouble(split[i]);
        }

        dataset.add(new Vector(data));
        line = br.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return dataset;
  }
}
