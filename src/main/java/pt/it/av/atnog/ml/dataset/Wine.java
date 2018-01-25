package pt.it.av.atnog.ml.dataset;

import pt.it.av.atnog.utils.bla.Vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Wine implements Dataset<Vector>{
  private final List<Vector> dataset = new ArrayList<>(178);

  public Wine(final String filename) {
    this(new File(filename));
  }

  public Wine() {
    this(ClassLoader.getSystemClassLoader().getResource("wine.csv").getFile());
  }

  public Wine(final File file) {
    try(BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line = br.readLine();
      while (line != null) {
        String split[] = line.split(",");

        double data[] = new double[split.length-1];
        for(int i = 1; i < split.length; i++) {
          data[i-1] = Double.parseDouble(split[i]);
        }

        dataset.add(new Vector(data));
        line = br.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public List<Vector> load() {
    return dataset;
  }
}
