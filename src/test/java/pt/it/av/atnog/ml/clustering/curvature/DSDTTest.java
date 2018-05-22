package pt.it.av.atnog.ml.clustering.curvature;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DSDTTest {

  @Test
  public void test_elbow() {
    Curvature alg = new DSDT();
    int elbow = alg.elbow(CurvatureTest.elbow_x, CurvatureTest.elbow_y);
    assertEquals(10, elbow);
  }

  @Test
  public void test_knee() {
    Curvature alg = new DSDT();
    int knee = alg.knee(CurvatureTest.knee_x, CurvatureTest.knee_y);
    assertEquals(14, knee);
  }
}
