package pt.it.av.atnog.ml.dataset;

import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.structures.Point2D;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ASetsA3 implements Dataset<Vector> {
  @Override
  public List<Vector> load() {
    //String fileName = getClass().getResource("a3.csv").getFile();
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    String fileName = classloader.getResource("a3.csv").getFile();
    List<Vector> dataset = new ArrayList<>(7500);
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line = br.readLine();
      while (line != null) {
        String split[] = line.split(",");

        dataset.add(new Point2D(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
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
    return 50;
  }
}
