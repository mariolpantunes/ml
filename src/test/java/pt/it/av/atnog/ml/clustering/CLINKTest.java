package pt.it.av.atnog.ml.clustering;

import org.junit.Test;
import pt.it.av.atnog.ml.clustering.hierarchical.CLINK;
import pt.it.av.atnog.ml.clustering.hierarchical.SLINK;
import pt.it.av.atnog.utils.structures.Point2D;

import java.util.ArrayList;
import java.util.List;


/**
 * Unit tests for Cluster.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class CLINKTest {
  public List<Point2D> dps = new ArrayList<>();

  @org.junit.Before
  public void setup() {
    /*dps.add(new Point2D(4,1));
    dps.add(new Point2D(3,3));
    dps.add(new Point2D(2,3));
    dps.add(new Point2D(1,2));
    dps.add(new Point2D(2,5));*/

    dps.add(new Point2D(1,1));
    dps.add(new Point2D(1.5,1.5));
    dps.add(new Point2D(5,5));
    dps.add(new Point2D(3,4));
    dps.add(new Point2D(4,4));
    dps.add(new Point2D(3,3.5));
  }

  @Test
  public void test_clustering() {
    CLINK alg = new CLINK();
    //int d[][] = alg.clustering(dps);
    AutoK.hiearchicalElbowTest(alg, dps);
  }
}
