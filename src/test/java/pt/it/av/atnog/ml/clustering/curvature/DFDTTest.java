package pt.it.av.atnog.ml.clustering.curvature;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DFDTTest {

  @Test
  public void test_elbow() {
    Curvature alg = new DFDT();
    int elbow = alg.elbow(CurvatureTest.elbow_x, CurvatureTest.elbow_y);
    assertTrue(elbow == 6);
  }

  @Test
  public void test_knee() {
    Curvature alg = new DFDT();
    int knee = alg.knee(CurvatureTest.knee_x, CurvatureTest.knee_y);
    assertTrue(knee == 10);
  }
}
