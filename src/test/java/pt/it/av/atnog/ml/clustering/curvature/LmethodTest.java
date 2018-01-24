package pt.it.av.atnog.ml.clustering.curvature;

import org.junit.Test;
import pt.it.av.atnog.utils.ArrayUtils;

import static org.junit.Assert.assertTrue;

public class LmethodTest {
  @Test
  public void test_elbow() {
    Curvature alg = new Lmethod();
    int elbow = alg.elbow(CurvatureTest.elbow_x, CurvatureTest.elbow_y);
    assertTrue(elbow == 3);
    //System.out.println(CurvatureTest.elbow_x[elbow]);
  }
}
