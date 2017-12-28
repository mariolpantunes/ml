package pt.it.av.atnog.ml;

import pt.it.av.atnog.utils.structures.Point4D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods to help deal with pt.it.av.atnog.ml.Iris dataset.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Iris {
  /**
   * Private constructor.
   * This class is a static library.
   */
  private Iris() {}

  /**
   * Returns a {@link List} of {@link Point4D} with the data from pt.it.av.atnog.ml.Iris dataset.
   *
   * @return a {@link List} of {@link Point4D} with the data from pt.it.av.atnog.ml.Iris dataset.
   */
  public static List<Point4D> load() {
    List<Point4D> rv = new ArrayList<>(150);

    try(BufferedReader br = new BufferedReader(new FileReader("iris.csv"))) {
      String line = br.readLine();
      //String split[] = line.split(",");
      //rv.add(new Point4D(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
      //    Double.parseDouble(split[2]), Double.parseDouble(split[3])));
      while (line != null) {
        String split[] = line.split(",");
        rv.add(new Point4D(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
            Double.parseDouble(split[2]), Double.parseDouble(split[3])));
        line = br.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return rv;
  }
}
