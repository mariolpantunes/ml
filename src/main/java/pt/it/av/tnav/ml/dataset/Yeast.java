package pt.it.av.tnav.ml.dataset;

import pt.it.av.tnav.utils.bla.Vector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Yeast implements Dataset<Vector> {
  @Override
  public List<Vector> load() {
    //String fileName = getClass().getResource("yeast.csv").getFile();
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    String fileName = classloader.getResource("yeast.csv").getFile();
    List<Vector> dataset = new ArrayList<>(1484);
    try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line = br.readLine();
      while (line != null) {
        String split[] = line.split(",");

        double data[] = new double[8];
        data[0] = Double.parseDouble(split[1]);
        data[1] = Double.parseDouble(split[2]);
        data[2] = Double.parseDouble(split[3]);
        data[3] = Double.parseDouble(split[4]);
        data[4] = Double.parseDouble(split[5]);
        data[5] = Double.parseDouble(split[6]);
        data[6] = Double.parseDouble(split[7]);
        data[7] = Double.parseDouble(split[8]);

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

  @Override
  public int classes() {
    return 10;
  }
}
