package pt.it.av.tnav.ml.dataset;

import pt.it.av.tnav.utils.structures.point.Point2D;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SSetsS1 implements Dataset<Point2D> {
  @Override
  public List<Point2D> load() {
    List<Point2D> dataset = new ArrayList<>(5000);
    String fileName = getClass().getResource("s1.csv").getFile();
    try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
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
    return 15;
  }
}
