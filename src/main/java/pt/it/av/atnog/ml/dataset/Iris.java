package pt.it.av.atnog.ml.dataset;

import pt.it.av.atnog.utils.structures.Point4D;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods to help deal with Iris dataset.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Iris implements Dataset<Point4D> {
  @Override
  public List<Point4D> load() {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    String fileName = classloader.getResource("iris.csv").getFile();
    //String fileName = getClass().getResource("iris.csv").getFile();
    List<Point4D> data = new ArrayList<>(150);
    try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line = br.readLine();
      while (line != null) {
        String split[] = line.split(",");
        data.add(new Point4D(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
            Double.parseDouble(split[2]), Double.parseDouble(split[3])));
        line = br.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data;
  }

  @Override
  public int classes() {
    return 3;
  }
}
